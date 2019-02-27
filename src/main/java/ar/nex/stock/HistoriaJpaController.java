/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.stock;

import ar.nex.articulo.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class HistoriaJpaController implements Serializable {

    public HistoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Historia historia) {
        if (historia.getStockList() == null) {
            historia.setStockList(new ArrayList<Stock>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Stock> attachedStockList = new ArrayList<Stock>();
            for (Stock stockListStockToAttach : historia.getStockList()) {
                stockListStockToAttach = em.getReference(stockListStockToAttach.getClass(), stockListStockToAttach.getId());
                attachedStockList.add(stockListStockToAttach);
            }
            historia.setStockList(attachedStockList);
            em.persist(historia);
            for (Stock stockListStock : historia.getStockList()) {
                stockListStock.getHistoriaList().add(historia);
                stockListStock = em.merge(stockListStock);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Historia historia) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historia persistentHistoria = em.find(Historia.class, historia.getId());
            List<Stock> stockListOld = persistentHistoria.getStockList();
            List<Stock> stockListNew = historia.getStockList();
            List<Stock> attachedStockListNew = new ArrayList<Stock>();
            for (Stock stockListNewStockToAttach : stockListNew) {
                stockListNewStockToAttach = em.getReference(stockListNewStockToAttach.getClass(), stockListNewStockToAttach.getId());
                attachedStockListNew.add(stockListNewStockToAttach);
            }
            stockListNew = attachedStockListNew;
            historia.setStockList(stockListNew);
            historia = em.merge(historia);
            for (Stock stockListOldStock : stockListOld) {
                if (!stockListNew.contains(stockListOldStock)) {
                    stockListOldStock.getHistoriaList().remove(historia);
                    stockListOldStock = em.merge(stockListOldStock);
                }
            }
            for (Stock stockListNewStock : stockListNew) {
                if (!stockListOld.contains(stockListNewStock)) {
                    stockListNewStock.getHistoriaList().add(historia);
                    stockListNewStock = em.merge(stockListNewStock);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = historia.getId();
                if (findHistoria(id) == null) {
                    throw new NonexistentEntityException("The historia with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Historia historia;
            try {
                historia = em.getReference(Historia.class, id);
                historia.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The historia with id " + id + " no longer exists.", enfe);
            }
            List<Stock> stockList = historia.getStockList();
            for (Stock stockListStock : stockList) {
                stockListStock.getHistoriaList().remove(historia);
                stockListStock = em.merge(stockListStock);
            }
            em.remove(historia);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Historia> findHistoriaEntities() {
        return findHistoriaEntities(true, -1, -1);
    }

    public List<Historia> findHistoriaEntities(int maxResults, int firstResult) {
        return findHistoriaEntities(false, maxResults, firstResult);
    }

    private List<Historia> findHistoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Historia.class));
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

    public Historia findHistoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Historia.class, id);
        } finally {
            em.close();
        }
    }

    public int getHistoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Historia> rt = cq.from(Historia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
