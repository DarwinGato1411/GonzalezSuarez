/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.controlador;

import com.ec.entidad.Medidor;
import com.ec.entidad.Predio;
import com.ec.entidad.Propietario;
import com.ec.entidad.Tarifa;
import com.ec.servicio.ServicioGeneral;
import com.ec.servicio.ServicioMedidor;
import com.ec.servicio.ServicioPredio;
import com.ec.servicio.ServicioTarifa;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.ExecutionArgParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Window;

/**
 *
 * @author gato
 */
public class NuevoMedidor {

    @Wire
    Window wMedidor;
    private ListModelList<Predio> listaPredioModel;
    private List<Predio> listaPredios = new ArrayList<Predio>();
    private Set<Predio> registrosSeleccionados = new HashSet<Predio>();
    ServicioPredio servicioPredio = new ServicioPredio();
    private Medidor entidad = new Medidor();
    ServicioMedidor servicioMedidor = new ServicioMedidor();
    private String accion = "create";
    private String buscarPredio = "";

    ServicioTarifa servicioTarifa = new ServicioTarifa();
    private List<Tarifa> listatarifas = new ArrayList<Tarifa>();
    private Tarifa tarifaSelected = null;

    ServicioGeneral servicioGeneral = new ServicioGeneral();

    @AfterCompose
    public void afterCompose(@ExecutionArgParam("valor") Medidor valor, @ContextParam(ContextType.VIEW) Component view) {
        Selectors.wireComponents(view, this, false);
        if (valor != null) {
            this.entidad = valor;
            //como capturar los datos
//            Propietario propietario=   valor.getIdPredio().getIdPropietario();
            accion = "update";
            tarifaSelected = valor.getIdTarifa();
            listaPredios.add(valor.getIdPredio());
            setListaPredioModel(new ListModelList<Predio>(getListaPredios()));
            ((ListModelList<Predio>) listaPredioModel).setMultiple(false);
            listaPredioModel.setSelection(listaPredios);

        } else {
            this.entidad = new Medidor();
            entidad.setMedFechaRegistro(new Date());
            entidad.setMedFechaInstala(new Date());
            entidad.setMedNumero("");
            accion = "create";
            Medidor ultimoMedidor = servicioMedidor.findUltimoMedidorRegistrado();
            if (ultimoMedidor != null) {
                try {
                    entidad.setMedNumero(String.valueOf(Integer.valueOf(ultimoMedidor.getMedNumero().trim()) + 1));
                } catch (NumberFormatException e) {
                    entidad.setMedNumero("");
                }
            }
            obtenerPredios();
        }

        listatarifas = servicioTarifa.findLikeTariDescripcion("");
    }

    private void findLikePredio() {
        listaPredios = servicioPredio.findLikePreDireccionCedulaNombre(buscarPredio);
    }

    private void obtenerPredios() {
        findLikePredio();
        setListaPredioModel(new ListModelList<Predio>(getListaPredios()));
        ((ListModelList<Predio>) listaPredioModel).setMultiple(false);
        //listaPropietariosModel.addToSelection(entidad.getIdPropietario());
    }

    @Command
    @NotifyChange("entidad")
    public void seleccionarRegistros() {
        Predio predioSelected = null;

        registrosSeleccionados = ((ListModelList<Predio>) getListaPredioModel()).getSelection();
        for (Predio item : registrosSeleccionados) {
            predioSelected = item;

        }
        entidad.setIdPredio(predioSelected);

    }

    @Command
    @NotifyChange({"listaPredioModel", "buscarPredio"})
    public void buscarPropietario() {
        obtenerPredios();
    }

    @Command
    public void guardar() {
        entidad.setIdTarifa(tarifaSelected);
        if (entidad.getIdTarifa() != null
                && entidad.getIdPredio() != null
                && !entidad.getMedNumero().equals("")) {

            if (accion.equals("create")) {
                System.out.println("MES: " + entidad.getMedFechaRegistro().getMonth());
                System.out.println("FECHA: " + entidad.getMedFechaRegistro());
                servicioMedidor.crear(entidad);

                servicioGeneral.iniciarLecturaMedidor(entidad.getMedFechaRegistro().getMonth() + 1, entidad.getMedFechaRegistro());
            } else {
                System.out.println("MES dddd: " + entidad.getMedFechaRegistro().getMonth());
                System.out.println("FECHA  ddd: " + entidad.getMedFechaRegistro());
                servicioMedidor.modificar(entidad);
            }
            wMedidor.detach();
        } else {

            Clients.showNotification("Verifique la informacion requerida",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    @Command
    @NotifyChange({"listaPropietarios", "buscar"})
    public void actualizar() {

        if (entidad.getIdPredio() != null) {
            final HashMap<String, Propietario> map = new HashMap<String, Propietario>();
            map.put("valor", entidad.getIdPredio().getIdPropietario());
            org.zkoss.zul.Window window = (org.zkoss.zul.Window) Executions.createComponents(
                    "/nuevo/propietario.zul", null, map);
            window.doModal();
        }else {
            Clients.showNotification("Debe selecionar un propietario",
                    Clients.NOTIFICATION_TYPE_ERROR, null, "end_center", 3000, true);
        }
    }

    public ListModelList<Predio> getListaPredioModel() {
        return listaPredioModel;
    }

    public void setListaPredioModel(ListModelList<Predio> listaPredioModel) {
        this.listaPredioModel = listaPredioModel;
    }

    public List<Predio> getListaPredios() {
        return listaPredios;
    }

    public void setListaPredios(List<Predio> listaPredios) {
        this.listaPredios = listaPredios;
    }

    public Set<Predio> getRegistrosSeleccionados() {
        return registrosSeleccionados;
    }

    public void setRegistrosSeleccionados(Set<Predio> registrosSeleccionados) {
        this.registrosSeleccionados = registrosSeleccionados;
    }

    public String getBuscarPredio() {
        return buscarPredio;
    }

    public void setBuscarPredio(String buscarPredio) {
        this.buscarPredio = buscarPredio;
    }

    public Medidor getEntidad() {
        return entidad;
    }

    public void setEntidad(Medidor entidad) {
        this.entidad = entidad;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public List<Tarifa> getListatarifas() {
        return listatarifas;
    }

    public void setListatarifas(List<Tarifa> listatarifas) {
        this.listatarifas = listatarifas;
    }

    public Tarifa getTarifaSelected() {
        return tarifaSelected;
    }

    public void setTarifaSelected(Tarifa tarifaSelected) {
        this.tarifaSelected = tarifaSelected;
    }

}
