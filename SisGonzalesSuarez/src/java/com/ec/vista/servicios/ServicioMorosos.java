/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vista.servicios;

import com.ec.servicio.HelperPersistencia;
import com.ec.vistas.PropietariosMorosos;
import com.ec.vistas.SriCatastro;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioMorosos {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public List<PropietariosMorosos> findMorosos(String propNombre) {

        List<PropietariosMorosos> listado = new ArrayList<PropietariosMorosos>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM PropietariosMorosos a WHERE a.propNombre LIKE :propNombre ORDER BY a.propApellido ASC");
            query.setParameter("propNombre", "%" + propNombre + "%");
            listado = (List<PropietariosMorosos>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en las consulta SriCatastro " + e.getMessage());
        } finally {
            em.close();
        }

        return listado;
    }

}
