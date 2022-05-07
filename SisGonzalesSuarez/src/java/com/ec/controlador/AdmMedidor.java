/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Medidor;
import com.ec.entidad.Propietario;
import com.ec.servicio.ServicioMedidor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.internet.ParseException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Filedownload;

/**
 *
 * @author gato
 */
public class AdmMedidor {

    ServicioMedidor servicioMedidor = new ServicioMedidor();
    private Medidor entidad = new Medidor();
    private List<Medidor> listaDatos = new ArrayList<Medidor>();

    private String buscarNombre = "";
    private String buscar = "";

    public AdmMedidor() {

        findMedidorPorNumero();

    }

    private void findMedidorPorNumero() {
        listaDatos = servicioMedidor.findLikeMedNumero(buscarNombre);
    }

//    private void findLikeBarrios() {
//        listaDatos = servicioMedidor.findLikeBarrios(buscar);
//    }
    
    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void buscarMedidorNumero() {
        findMedidorPorNumero();
    }
//     @Command
//    @NotifyChange({"listaDatos", "buscar"})
//    public void buscarBarrios() {
//        findLikeBarrios();
//    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void nuevo() {
        buscarNombre = "";
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/medidor.zul", null, null);
        window.doModal();
        findMedidorPorNumero();
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void actualizar(@BindingParam("valor") Medidor valor) {
        buscarNombre = "";
        final HashMap<String, Medidor> map = new HashMap<String, Medidor>();
        map.put("valor", valor);
        org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                "/nuevo/medidor.zul", null, map);
        window.doModal();
        findMedidorPorNumero();
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
            System.out.println("ERROR AL DESCARGAR MEDIDOR" + e.getMessage());
        }
    }

    private String exportarExcel() throws FileNotFoundException, IOException, ParseException {
        String directorioReportes = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/reportes");

        Date date = new Date();
        SimpleDateFormat fhora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sm = new SimpleDateFormat("yyy-MM-dd");
        String strDate = sm.format(date);

        String pathSalida = directorioReportes + File.separator + "medidores.xls";
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
            HSSFSheet s = wb.createSheet("Medidores");

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
                c1.setCellValue(new HSSFRichTextString(item.getMedBarrio()));

                HSSFCell c11 = r.createCell(i++);
                c11.setCellValue(new HSSFRichTextString(item.getIdPredio().getIdPropietario().getPropEdad() != null ? item.getIdPredio().getIdPropietario().getPropEdad().toString() : ""));

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
  
    public String getBuscarNombre() {
        return buscarNombre;
    }

    public List<Medidor> getListaDatos() {
        return listaDatos;
    }

    public void setListaDatos(List<Medidor> listaDatos) {
        this.listaDatos = listaDatos;
    }

    public void setBuscarNombre(String buscarNombre) {
        this.buscarNombre = buscarNombre;
    }

    public Medidor getEntidad() {
        return entidad;
    }

    public void setEntidad(Medidor entidad) {
        this.entidad = entidad;
    }

    public String getBuscar() {
        return buscar;
    }

    public void setBuscar(String buscar) {
        this.buscar = buscar;
    }
    
}
