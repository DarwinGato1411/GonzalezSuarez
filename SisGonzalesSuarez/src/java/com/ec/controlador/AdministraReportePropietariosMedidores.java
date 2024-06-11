
package com.ec.controlador;

import com.ec.entidad.Medidor;
import com.ec.entidad.Propietario;
import com.ec.servicio.HelperPersistencia;
import com.ec.servicio.ServicioMedidor;
import com.ec.servicio.ServicioPropietario;
import com.ec.vista.servicios.ServicioMorosos;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.internet.ParseException;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.util.media.AMedia;

/**
 *
 * @author gato
 */
public class AdministraReportePropietariosMedidores {

    
    ServicioMedidor servicioMedidor = new ServicioMedidor();
    private List<Medidor> listaDatos = new ArrayList<Medidor>();
    ServicioMorosos servicioMorosos = new ServicioMorosos();
    private String buscar = "";
    private String buscarMorosos = "";
    AMedia fileContent = null;
    Connection con = null;
    
    public AdministraReportePropietariosMedidores() {

        findLikeCedulaNombre();

    }

    private void findLikeCedulaNombre() {
        listaDatos = servicioMedidor.findLikeMedidorPropietario(buscar);
    }
    
  
    
     @Command
    @NotifyChange({"listaPropietarios", "buscar"})
    public void buscarPropietario() {
        findLikeCedulaNombre();
    }

  
    
   
    public List<Medidor> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<Medidor> listaDatos) {
        this.listaDatos = listaDatos;
    }



    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

    public String getBuscarMorosos() {
        return buscarMorosos;
    }

    public void setBuscarMorosos(String buscarMorosos) {
        this.buscarMorosos = buscarMorosos;
    }

    
    /*EXPORTAR EXCEL*/
    @Command
    public void exportEXCEL() throws Exception {
        try {
            File dosfile = new File(exportarExcel());
            if (dosfile.exists()) {
                FileInputStream inputStream = new FileInputStream(dosfile);
                Filedownload.save(inputStream, new MimetypesFileTypeMap().getContentType(dosfile), dosfile.getName());
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR AL DESCARGAR PROPIETARIOS" + e.getMessage());
        }
    }

    private String exportarExcel() throws FileNotFoundException, IOException, ParseException {
        String directorioReportes = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reportes");

        Date date = new Date();
        SimpleDateFormat fhora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
        String strDate = sm.format(date);

        String pathSalida = directorioReportes + File.separator + "propietarios_medidores.xls";
        System.out.println("Direccion del reporte de propietarios " + pathSalida);
        try {
            int j = 1;
            File archivoXLS = new File(pathSalida);
            if (archivoXLS.exists()) {
                archivoXLS.delete();
            }
            archivoXLS.createNewFile();
            FileOutputStream archivo = new FileOutputStream(archivoXLS);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet s = wb.createSheet("Propietarios");

            HSSFFont fuente = wb.createFont();
            fuente.setBoldweight((short) 700);
            HSSFCellStyle estiloCelda = wb.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setAlignment((short) 2);
            estiloCelda.setFont(fuente);

            HSSFCellStyle estiloCeldaInterna = wb.createCellStyle();
            estiloCeldaInterna.setWrapText(true);
            estiloCeldaInterna.setAlignment((short) 5);
            estiloCeldaInterna.setFont(fuente);

            HSSFCellStyle estiloCelda1 = wb.createCellStyle();
            estiloCelda1.setWrapText(true);
            estiloCelda1.setFont(fuente);

            HSSFRow r = null;

            HSSFCell c = null;
            r = s.createRow(0);

            HSSFCell chfe = r.createCell(0);
            chfe.setCellValue(new HSSFRichTextString("CEDULA"));
            chfe.setCellStyle(estiloCelda);

            HSSFCell ch1 = r.createCell(j++);
            ch1.setCellValue(new HSSFRichTextString("PROPIETARIOS"));
            ch1.setCellStyle(estiloCelda);

            HSSFCell ch2 = r.createCell(j++);
            ch2.setCellValue(new HSSFRichTextString("SECTOR"));
            ch2.setCellStyle(estiloCelda);

            HSSFCell ch21 = r.createCell(j++);
            ch21.setCellValue(new HSSFRichTextString("EDAD"));
            ch21.setCellStyle(estiloCelda);

            int rownum = 1;
            int i = 0;

            for (Medidor item : listaDatos) {
                i = 0;

                r = s.createRow(rownum);

                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPorpCedula()));

                HSSFCell c0 = r.createCell(i++);
                c0.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropNombre() + " " + item.getIdPredio().getIdPropietario().getPropApellido()));

                HSSFCell c1 = r.createCell(i++);
                c1.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropSector()));

                HSSFCell c11 = r.createCell(i++);
                c11.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropEdad()!=null?item.getIdPredio().getIdPropietario().getPropEdad().toString():""));

                /*autemta la siguiente fila*/
                rownum += 1;

            }
            for (int k = 0; k <= listaDatos.size(); k++) {
                s.autoSizeColumn(k);
            }
            wb.write(archivo);
            archivo.close();

        } catch (IOException e) {
            System.out.println("error " + e.getMessage());
        }
        return pathSalida;

    }
    
//    @Command
//    public void reporteProveedoresGeneral() throws JRException, IOException, NamingException, SQLException {
//        reporteGeneral();
//    }
//    
//    public void reporteGeneral() throws JRException, IOException, NamingException, SQLException {
//        EntityManager emf = HelperPersistencia.getEMF();
//        try {
//
//            emf.getTransaction().begin();
//            con = emf.unwrap(Connection.class);
//            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
//                    .getRealPath("/reportes");
//            String reportPath = "";
//            reportPath = reportFile + File.separator + "reporteclientesgeneral.jasper";
//
//            if (con != null) {
//                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
//            }
//            FileInputStream is = null;
//            is = new FileInputStream(reportPath);
//
//            byte[] buf = JasperRunManager.runReportToPdf(is, null, con);
//            InputStream mediais = new ByteArrayInputStream(buf);
//            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
//            fileContent = amedia;
//            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
////para pasar al visor
//            map.put("pdf", fileContent);
//            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
//                    "/venta/contenedorReporte.zul", null, map);
//            window.doModal();
//        } catch (Exception e) {
//            System.out.println("ERROR EL PRESENTAR EL REPORTE "+e.getMessage());
//        } finally {
//            if (emf != null) {
//                emf.getTransaction().commit();
//            }
//
//        }
//
//    }

}
