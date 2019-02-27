/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.compra;

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
public class CompraJpaController implements Serializable {

    public CompraJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Compra compra) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo articuloID = compra.getArticuloID();
            if (articuloID != null) {
                articuloID = em.getReference(articuloID.getClass(), articuloID.getId());
                compra.setArticuloID(articuloID);
            }
            Proveedor proveedorID = compra.getProveedorID();
            if (proveedorID != null) {
                proveedorID = em.getReference(proveedorID.getClass(), proveedorID.getId());
                compra.setProveedorID(proveedorID);
            }
            em.persist(compra);
            if (articuloID != null) {
                articuloID.getCompraList().add(compra);
                articuloID = em.merge(articuloID);
            }
            if (proveedorID != null) {
                proveedorID.getCompraList().add(compra);
                proveedorID = em.merge(proveedorID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compra compra) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra persistentCompra = em.find(Compra.class, compra.getId());
            Articulo articuloIDOld = persistentCompra.getArticuloID();
            Articulo articuloIDNew = compra.getArticuloID();
            Proveedor proveedorIDOld = persistentCompra.getProveedorID();
            Proveedor proveedorIDNew = compra.getProveedorID();
            if (articuloIDNew != null) {
                articuloIDNew = em.getReference(articuloIDNew.getClass(), articuloIDNew.getId());
                compra.setArticuloID(articuloIDNew);
            }
            if (proveedorIDNew != null) {
                proveedorIDNew = em.getReference(proveedorIDNew.getClass(), proveedorIDNew.getId());
                compra.setProveedorID(proveedorIDNew);
            }
            compra = em.merge(compra);
            if (articuloIDOld != null && !articuloIDOld.equals(articuloIDNew)) {
                articuloIDOld.getCompraList().remove(compra);
                articuloIDOld = em.merge(articuloIDOld);
            }
            if (articuloIDNew != null && !articuloIDNew.equals(articuloIDOld)) {
                articuloIDNew.getCompraList().add(compra);
                articuloIDNew = em.merge(articuloIDNew);
            }
            if (proveedorIDOld != null && !proveedorIDOld.equals(proveedorIDNew)) {
                proveedorIDOld.getCompraList().remove(compra);
                proveedorIDOld = em.merge(proveedorIDOld);
            }
            if (proveedorIDNew != null && !proveedorIDNew.equals(proveedorIDOld)) {
                proveedorIDNew.getCompraList().add(compra);
                proveedorIDNew = em.merge(proveedorIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = compra.getId();
                if (findCompra(id) == null) {
                    throw new NonexistentEntityException("The compra with id " + id + " no longer exists.");
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
            Compra compra;
            try {
                compra = em.getReference(Compra.class, id);
                compra.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The compra with id " + id + " no longer exists.", enfe);
            }
            Articulo articuloID = compra.getArticuloID();
            if (articuloID != null) {
                articuloID.getCompraList().remove(compra);
                articuloID = em.merge(articuloID);
            }
            Proveedor proveedorID = compra.getProveedorID();
            if (proveedorID != null) {
                proveedorID.getCompraList().remove(compra);
                proveedorID = em.merge(proveedorID);
            }
            em.remove(compra);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Compra> findCompraEntities() {
        return findCompraEntities(true, -1, -1);
    }

    public List<Compra> findCompraEntities(int maxResults, int firstResult) {
        return findCompraEntities(false, maxResults, firstResult);
    }

    private List<Compra> findCompraEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Compra.class));
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

    public Compra findCompra(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Compra.class, id);
        } finally {
            em.close();
        }
    }

    public int getCompraCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Compra> rt = cq.from(Compra.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
