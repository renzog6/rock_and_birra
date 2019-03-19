/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.articulo.Articulo;
import ar.nex.articulo.StockDetalle;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class StockDetalleJpaController implements Serializable {

    public StockDetalleJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(StockDetalle stockDetalle) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo articulo = stockDetalle.getArticulo();
            if (articulo != null) {
                articulo = em.getReference(articulo.getClass(), articulo.getId());
                stockDetalle.setArticulo(articulo);
            }
            em.persist(stockDetalle);
            if (articulo != null) {
                articulo.getStockDetalleList().add(stockDetalle);
                articulo = em.merge(articulo);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStockDetalle(stockDetalle.getId()) != null) {
                throw new PreexistingEntityException("StockDetalle " + stockDetalle + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(StockDetalle stockDetalle) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            StockDetalle persistentStockDetalle = em.find(StockDetalle.class, stockDetalle.getId());
            Articulo articuloOld = persistentStockDetalle.getArticulo();
            Articulo articuloNew = stockDetalle.getArticulo();
            if (articuloNew != null) {
                articuloNew = em.getReference(articuloNew.getClass(), articuloNew.getId());
                stockDetalle.setArticulo(articuloNew);
            }
            stockDetalle = em.merge(stockDetalle);
            if (articuloOld != null && !articuloOld.equals(articuloNew)) {
                articuloOld.getStockDetalleList().remove(stockDetalle);
                articuloOld = em.merge(articuloOld);
            }
            if (articuloNew != null && !articuloNew.equals(articuloOld)) {
                articuloNew.getStockDetalleList().add(stockDetalle);
                articuloNew = em.merge(articuloNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = stockDetalle.getId();
                if (findStockDetalle(id) == null) {
                    throw new NonexistentEntityException("The stockDetalle with id " + id + " no longer exists.");
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
            StockDetalle stockDetalle;
            try {
                stockDetalle = em.getReference(StockDetalle.class, id);
                stockDetalle.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stockDetalle with id " + id + " no longer exists.", enfe);
            }
            Articulo articulo = stockDetalle.getArticulo();
            if (articulo != null) {
                articulo.getStockDetalleList().remove(stockDetalle);
                articulo = em.merge(articulo);
            }
            em.remove(stockDetalle);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<StockDetalle> findStockDetalleEntities() {
        return findStockDetalleEntities(true, -1, -1);
    }

    public List<StockDetalle> findStockDetalleEntities(int maxResults, int firstResult) {
        return findStockDetalleEntities(false, maxResults, firstResult);
    }

    private List<StockDetalle> findStockDetalleEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(StockDetalle.class));
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

    public StockDetalle findStockDetalle(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(StockDetalle.class, id);
        } finally {
            em.close();
        }
    }

    public int getStockDetalleCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<StockDetalle> rt = cq.from(StockDetalle.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
