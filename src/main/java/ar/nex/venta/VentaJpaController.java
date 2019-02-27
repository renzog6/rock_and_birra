/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.venta;

import ar.nex.articulo.Articulo;
import ar.nex.articulo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Renzo
 */
public class VentaJpaController implements Serializable {

    public VentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venta venta) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo articuloID = venta.getArticuloID();
            if (articuloID != null) {
                articuloID = em.getReference(articuloID.getClass(), articuloID.getId());
                venta.setArticuloID(articuloID);
            }
            Cliente clienteID = venta.getClienteID();
            if (clienteID != null) {
                clienteID = em.getReference(clienteID.getClass(), clienteID.getId());
                venta.setClienteID(clienteID);
            }
            em.persist(venta);
            if (articuloID != null) {
                articuloID.getVentaList().add(venta);
                articuloID = em.merge(articuloID);
            }
            if (clienteID != null) {
                clienteID.getVentaList().add(venta);
                clienteID = em.merge(clienteID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Venta venta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta persistentVenta = em.find(Venta.class, venta.getId());
            Articulo articuloIDOld = persistentVenta.getArticuloID();
            Articulo articuloIDNew = venta.getArticuloID();
            Cliente clienteIDOld = persistentVenta.getClienteID();
            Cliente clienteIDNew = venta.getClienteID();
            if (articuloIDNew != null) {
                articuloIDNew = em.getReference(articuloIDNew.getClass(), articuloIDNew.getId());
                venta.setArticuloID(articuloIDNew);
            }
            if (clienteIDNew != null) {
                clienteIDNew = em.getReference(clienteIDNew.getClass(), clienteIDNew.getId());
                venta.setClienteID(clienteIDNew);
            }
            venta = em.merge(venta);
            if (articuloIDOld != null && !articuloIDOld.equals(articuloIDNew)) {
                articuloIDOld.getVentaList().remove(venta);
                articuloIDOld = em.merge(articuloIDOld);
            }
            if (articuloIDNew != null && !articuloIDNew.equals(articuloIDOld)) {
                articuloIDNew.getVentaList().add(venta);
                articuloIDNew = em.merge(articuloIDNew);
            }
            if (clienteIDOld != null && !clienteIDOld.equals(clienteIDNew)) {
                clienteIDOld.getVentaList().remove(venta);
                clienteIDOld = em.merge(clienteIDOld);
            }
            if (clienteIDNew != null && !clienteIDNew.equals(clienteIDOld)) {
                clienteIDNew.getVentaList().add(venta);
                clienteIDNew = em.merge(clienteIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = venta.getId();
                if (findVenta(id) == null) {
                    throw new NonexistentEntityException("The venta with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta venta;
            try {
                venta = em.getReference(Venta.class, id);
                venta.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            Articulo articuloID = venta.getArticuloID();
            if (articuloID != null) {
                articuloID.getVentaList().remove(venta);
                articuloID = em.merge(articuloID);
            }
            Cliente clienteID = venta.getClienteID();
            if (clienteID != null) {
                clienteID.getVentaList().remove(venta);
                clienteID = em.merge(clienteID);
            }
            em.remove(venta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Venta> findVentaEntities() {
        return findVentaEntities(true, -1, -1);
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return findVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> findVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venta.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Venta findVenta(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venta> rt = cq.from(Venta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
