/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import ar.nex.stock.Historia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.stock.Stock;
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

    public void create(Historia historia) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stock stock = historia.getStock();
            if (stock != null) {
                stock = em.getReference(stock.getClass(), stock.getId());
                historia.setStock(stock);
            }
            em.persist(historia);
            if (stock != null) {
                stock.getHistoriaList().add(historia);
                stock = em.merge(stock);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findHistoria(historia.getId()) != null) {
                throw new PreexistingEntityException("Historia " + historia + " already exists.", ex);
            }
            throw ex;
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
            Stock stockOld = persistentHistoria.getStock();
            Stock stockNew = historia.getStock();
            if (stockNew != null) {
                stockNew = em.getReference(stockNew.getClass(), stockNew.getId());
                historia.setStock(stockNew);
            }
            historia = em.merge(historia);
            if (stockOld != null && !stockOld.equals(stockNew)) {
                stockOld.getHistoriaList().remove(historia);
                stockOld = em.merge(stockOld);
            }
            if (stockNew != null && !stockNew.equals(stockOld)) {
                stockNew.getHistoriaList().add(historia);
                stockNew = em.merge(stockNew);
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
            Stock stock = historia.getStock();
            if (stock != null) {
                stock.getHistoriaList().remove(historia);
                stock = em.merge(stock);
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
