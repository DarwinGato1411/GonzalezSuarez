/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.vista.servicios;

import com.ec.entidad.Propietario;
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

    public List<PropietariosMorosos> findMorosos(String propietario) {

        List<PropietariosMorosos> listaMorosos = new ArrayList<PropietariosMorosos>();
        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT a FROM PropietariosMorosos a WHERE a.propNombre LIKE :propNombre ORDER BY a.propNombre ASC");      
            query.setParameter("propNombre", "%" + propietario + "%");
            listaMorosos = (List<PropietariosMorosos>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en las consulta SriCatastro " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMorosos;
    }
    
    public List<PropietariosMorosos> findLikeCedulaNombreMorosos(String valor) {

        List<PropietariosMorosos> listaMorosos = new ArrayList<PropietariosMorosos>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM PropietariosMorosos a WHERE a.porpCedula LIKE :porpCedula OR a.propNombre LIKE :propNombre OR a.propApellido LIKE :propApellido ORDER BY a.propNombre ASC");
            query.setParameter("porpCedula", "%" + valor + "%");
            query.setParameter("propNombre", "%" + valor + "%");
            query.setParameter("propApellido", "%" + valor + "%");
            //query.setMaxResults(200);
            listaMorosos = (List<PropietariosMorosos>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta propietario findLikeCedulaNombre " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMorosos;
    }


}
