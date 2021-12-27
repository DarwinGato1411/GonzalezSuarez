/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ec.servicio;

import com.ec.entidad.Medidor;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author gato
 */
public class ServicioMedidor {

    private EntityManager em;

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public void crear(Medidor medidor) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.persist(medidor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar medidor " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void eliminar(Medidor medidor) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.remove(em.merge(medidor));
            em.getTransaction().commit();

        } catch (Exception e) {
            System.out.println("Error en eliminar  medidor " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public void modificar(Medidor medidor) {

        try {
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            em.merge(medidor);
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en insertar medidor " + e.getMessage());
        } finally {
            em.close();
        }

    }

    public List<Medidor> findLikeMedidorPropietario(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            String SQL = "SELECT  a FROM Medidor a WHERE CONCAT( a.idPredio.idPropietario.propNombre ,a.idPredio.idPropietario.propApellido) LIKE :propNombre ";
            String order = " ORDER BY a.medNumero ASC";
            String[] datos = valor.split(" ");
            System.out.println(" datos.length " + datos.length);

            switch (datos.length) {
                case 1:
                    SQL = SQL;
                    break;
                case 2:
                    SQL = SQL + " OR  (a.idPredio.idPropietario.propNombre LIKE :param1"
                            + " AND  a.idPredio.idPropietario.propApellido LIKE :param2 ) ";
                    break;
                case 3:

                    SQL = SQL + "OR  (a.idPredio.idPropietario.propNombre LIKE :param1 "
                            + "AND  a.idPredio.idPropietario.propApellido LIKE :param2  "
                            + "AND a.idPredio.idPropietario.propApellido LIKE :param3 ) ";
                    break;
                case 4:

                    SQL = SQL + "OR  (a.idPredio.idPropietario.propNombre LIKE :param1 "
                            + "AND  a.idPredio.idPropietario.propNombre LIKE :param2  "
                            + "AND  a.idPredio.idPropietario.propApellido LIKE :param3  "
                            + "AND a.idPredio.idPropietario.propApellido LIKE :param4 ) ";
                    break;
                default:

                    break;
            }
            Query query = em.createQuery(SQL + order);

            switch (datos.length) {
                case 1:
                    query.setParameter("propNombre", "%" + valor + "%");

                    break;
                case 2:
                    query.setParameter("propNombre", "%" + valor + "%");
                    query.setParameter("param1", "%" + datos[0] + "%");
                    query.setParameter("param2", "%" + datos[1] + "%");
                    break;
                case 3:

                    query.setParameter("propNombre", "%" + valor + "%");
                    query.setParameter("param1", "%" + datos[0] + "%");
                    query.setParameter("param2", "%" + datos[1] + "%");
                    query.setParameter("param3", "%" + datos[2] + "%");

                    break;
                case 4:
                    query.setParameter("propNombre", "%" + valor + "%");
                    query.setParameter("param1", "%" + datos[0] + "%");
                    query.setParameter("param2", "%" + datos[1] + "%");
                    query.setParameter("param3", "%" + datos[2] + "%");
                    query.setParameter("param4", "%" + datos[3] + "%");
                    break;

                default:

                    break;
            }
//            query.setParameter("propApellido", "%" + valor + "%");

            listaMedidors = (List<Medidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta medidor findLikeMedNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMedidors;
    }

    public List<Medidor> findMedidorForNumero(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a WHERE a.medNumero =:medNumero ORDER BY a.medNumero ASC");
            query.setParameter("medNumero", valor);
            //query.setMaxResults(200);
            listaMedidors = (List<Medidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta medidor findLikeMedNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMedidors;
    }

    public List<Medidor> findLikeMedNumero(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a WHERE a.medNumero LIKE :medNumero OR a.idPredio.idPropietario.propNombre LIKE :propNombre OR a.idPredio.idPropietario.propApellido LIKE :propApellido ORDER BY a.medNumero ASC");
            query.setParameter("medNumero", "%" + valor + "%");
            query.setParameter("propNombre", "%" + valor + "%");
            query.setParameter("propApellido", "%" + valor + "%");
            //query.setMaxResults(200);
            listaMedidors = (List<Medidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta medidor findLikeMedNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMedidors;
    }

    
     public List<Medidor> findLikeMedNumeroCedula(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a WHERE a.idPredio.idPropietario.porpCedula LIKE :porpCedula ORDER BY a.medNumero ASC");
            query.setParameter("porpCedula", "%" + valor + "%");
//            query.setParameter("propNombre", "%" + valor + "%");
//            query.setParameter("propApellido", "%" + valor + "%");
            //query.setMaxResults(200);
            listaMedidors = (List<Medidor>) query.getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta medidor findLikeMedNumero " + e.getMessage());
        } finally {
            em.close();
        }

        return listaMedidors;
    }
    public Medidor findMedNumero(String valor) {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        Medidor entidad = null;
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a WHERE a.medNumero =:medNumero");
            query.setParameter("medNumero", valor);
            listaMedidors = (List<Medidor>) query.getResultList();
            entidad = listaMedidors.size() > 0 ? listaMedidors.get(0) : null;

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta findMedNumero" + e.getMessage());
        } finally {
            em.close();
        }

        return entidad;
    }

    public Medidor findUltimoMedidorRegistrado() {

        List<Medidor> listaMedidors = new ArrayList<Medidor>();
        Medidor entidad = null;
        try {
            //Connection connection = em.unwrap(Connection.class);
            em = HelperPersistencia.getEMF();
            em.getTransaction().begin();
            Query query = em.createQuery("SELECT  a FROM Medidor a ORDER BY a.idMedidor desc");
            query.setMaxResults(1);
            listaMedidors = (List<Medidor>) query.getResultList();
            entidad = listaMedidors.size() > 0 ? listaMedidors.get(0) : null;

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error en lsa consulta findMedNumero" + e.getMessage());
        } finally {
            em.close();
        }

        return entidad;
    }
}
