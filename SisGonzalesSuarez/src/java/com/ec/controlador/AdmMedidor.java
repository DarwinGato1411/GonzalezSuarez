/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Medidor;
import com.ec.servicio.ServicioMedidor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

/**
 *
 * @author gato
 */
public class AdmMedidor {

    ServicioMedidor servicioMedidor = new ServicioMedidor();

    private List<Medidor> listaDatos = new ArrayList<Medidor>();

    private String buscarNombre = "";

    public AdmMedidor() {

        findMedidorPorNumero();

    }

    private void findMedidorPorNumero() {
        listaDatos = servicioMedidor.findLikeMedNumero(buscarNombre);
    }

    @Command
    @NotifyChange({"listaDatos", "buscarNombre"})
    public void buscarMedidorNumero() {
        findMedidorPorNumero();
    }

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

}
