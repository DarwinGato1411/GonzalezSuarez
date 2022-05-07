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
import java.util.Map;
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
public class ReporteMedidorBarrio {

    ServicioMedidor servicioMedidor = new ServicioMedidor();
    private List<Medidor> listaDatos = new ArrayList<Medidor>();

    private String buscar = "";
    private String terceraedad = "";
    AMedia fileContent = null;
    Connection con = null;

    public ReporteMedidorBarrio() {

        findMedidoresBarrio();

    }

    private void findMedidoresBarrio() {
        listaDatos = servicioMedidor.findLikeBarrios(buscar,terceraedad);
    }

    private void findTerceraEdad() {
        listaDatos = servicioMedidor.findTerceraEdad();
    }

    @Command
    @NotifyChange({"listaDatos", "buscar"})
    public void buscarPorbarrio() {
        findMedidoresBarrio();
    }

    @Command
    @NotifyChange({"listaDatos"})
    public void buscarTerceraEdad() {

        findTerceraEdad();
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }

    public List<Medidor> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<Medidor> listaDatos) {
        this.listaDatos = listaDatos;
    }

    public String getTerceraedad() {
        return terceraedad;
    }

    public void setTerceraedad(String terceraedad) {
        this.terceraedad = terceraedad;
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

        String pathSalida = directorioReportes + File.separator + "propietarios.xls";
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
            chfe.setCellValue(new HSSFRichTextString("N°Medidor"));
            chfe.setCellStyle(estiloCelda);
            
            HSSFCell ch1 = r.createCell(j++);
            ch1.setCellValue(new HSSFRichTextString("CEDULA"));
            ch1.setCellStyle(estiloCelda);

            HSSFCell ch2 = r.createCell(j++);
            ch2.setCellValue(new HSSFRichTextString("PROPIETARIOS"));
            ch2.setCellStyle(estiloCelda);

            HSSFCell ch3 = r.createCell(j++);
            ch3.setCellValue(new HSSFRichTextString("BARRIOS /SECTOR"));
            ch3.setCellStyle(estiloCelda);

            HSSFCell ch4 = r.createCell(j++);
            ch4.setCellValue(new HSSFRichTextString("EDAD"));
            ch4.setCellStyle(estiloCelda);

            HSSFCell ch5 = r.createCell(j++);
            ch5.setCellValue(new HSSFRichTextString("CATEGORIA EDAD"));
            ch5.setCellStyle(estiloCelda);

            int rownum = 1;
            int i = 0;

            for (Medidor item : listaDatos) {
                i = 0;

                r = s.createRow(rownum);
                
                HSSFCell cf = r.createCell(i++);
                cf.setCellValue(new HSSFRichTextString(item.getMedNumero()));

                HSSFCell c0 = r.createCell(i++);
                c0.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPorpCedula()));

                HSSFCell c1 = r.createCell(i++);
                c1.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropNombre() + " " + item.getIdPredio().getIdPropietario().getPropApellido()));

                HSSFCell c2 = r.createCell(i++);
                c2.setCellValue(new HSSFRichTextString(item.getMedBarrio()));

                HSSFCell c3 = r.createCell(i++);
                c3.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropEdad() != null ? item.getIdPredio().getIdPropietario().getPropEdad().toString() : ""));

                HSSFCell c4 = r.createCell(i++);
                c4.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropEdad() > 60 ? "TERCERA EDAD" : "NORMAL"));

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

    @Command
    public void reporteProveedoresGeneral() throws JRException, IOException, NamingException, SQLException {
        reporteGeneral();
    }

    public void reporteGeneral() throws JRException, IOException, NamingException, SQLException {
        EntityManager emf = HelperPersistencia.getEMF();
        try {

            emf.getTransaction().begin();
            con = emf.unwrap(Connection.class);
            String reportFile = Executions.getCurrent().getDesktop().getWebApp()
                    .getRealPath("/reportes");
            String reportPath = "";
            reportPath = reportFile + File.separator + "medidorbarrio.jasper";

            if (con != null) {
                System.out.println("Conexión Realizada Correctamenteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
            }
            FileInputStream is = null;
            is = new FileInputStream(reportPath);

            Map<String, Object> parametros = new HashMap<String, Object>();

            //  parametros.put("codUsuario", String.valueOf(credentialLog.getAdUsuario().getCodigoUsuario()));
            parametros.put("barrio", "%" + buscar + "%");
            parametros.put("terceraedad", "%" + terceraedad + "%");

            byte[] buf = JasperRunManager.runReportToPdf(is, parametros, con);
            InputStream mediais = new ByteArrayInputStream(buf);
            AMedia amedia = new AMedia("Reporte", "pdf", "application/pdf", mediais);
            fileContent = amedia;
            final HashMap<String, AMedia> map = new HashMap<String, AMedia>();
//para pasar al visor
            map.put("pdf", fileContent);
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/venta/contenedorReporte.zul", null, map);
            window.doModal();
        } catch (Exception e) {
            System.out.println("ERROR EL PRESENTAR EL REPORTE " + e.getMessage());
        } finally {
            if (emf != null) {
                emf.getTransaction().commit();
            }

        }

    }

}
