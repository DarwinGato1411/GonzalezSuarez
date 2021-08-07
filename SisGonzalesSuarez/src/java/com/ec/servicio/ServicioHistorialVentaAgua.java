/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.HistorialPagoAgua;
import com.ec.entidad.VistaVentaDiaria;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioHistorialVentaAgua {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<HistorialPagoAgua> findHistorial(String cedula) {

        List<HistorialPagoAgua> listaVistaVentaDiarias = new ArrayList<HistorialPagoAgua>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM HistorialPagoAgua a WHERE a.cliCedula=:cliCedula ");
            query.setParameter("cliCedula", cedula);
            listaVistaVentaDiarias = (List<HistorialPagoAgua>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta ventaDiaria "+ e.getMessage());
        } finally {
            em.close();
        }

        return listaVistaVentaDiarias;
    }


}
