/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.stock;

import ar.nex.articulo.Articulo;
import ar.nex.stock.exceptions.NonexistentEntityException;
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
public class StockJpaController implements Serializable {

    public StockJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Stock stock) {
        if (stock.getHistoriaList() == null) {
            stock.setHistoriaList(new ArrayList<Historia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo articuloID = stock.getArticuloID();
            if (articuloID != null) {
                articuloID = em.getReference(articuloID.getClass(), articuloID.getId());
                stock.setArticuloID(articuloID);
            }
            List<Historia> attachedHistoriaList = new ArrayList<Historia>();
            for (Historia historiaListHistoriaToAttach : stock.getHistoriaList()) {
                historiaListHistoriaToAttach = em.getReference(historiaListHistoriaToAttach.getClass(), historiaListHistoriaToAttach.getId());
                attachedHistoriaList.add(historiaListHistoriaToAttach);
            }
            stock.setHistoriaList(attachedHistoriaList);
            em.persist(stock);
            if (articuloID != null) {
                articuloID.getStockList().add(stock);
                articuloID = em.merge(articuloID);
            }
            for (Historia historiaListHistoria : stock.getHistoriaList()) {
                historiaListHistoria.getStockList().add(stock);
                historiaListHistoria = em.merge(historiaListHistoria);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Stock stock) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Stock persistentStock = em.find(Stock.class, stock.getId());
            Articulo articuloIDOld = persistentStock.getArticuloID();
            Articulo articuloIDNew = stock.getArticuloID();
            List<Historia> historiaListOld = persistentStock.getHistoriaList();
            List<Historia> historiaListNew = stock.getHistoriaList();
            if (articuloIDNew != null) {
                articuloIDNew = em.getReference(articuloIDNew.getClass(), articuloIDNew.getId());
                stock.setArticuloID(articuloIDNew);
            }
            List<Historia> attachedHistoriaListNew = new ArrayList<Historia>();
            for (Historia historiaListNewHistoriaToAttach : historiaListNew) {
                historiaListNewHistoriaToAttach = em.getReference(historiaListNewHistoriaToAttach.getClass(), historiaListNewHistoriaToAttach.getId());
                attachedHistoriaListNew.add(historiaListNewHistoriaToAttach);
            }
            historiaListNew = attachedHistoriaListNew;
            stock.setHistoriaList(historiaListNew);
            stock = em.merge(stock);
            if (articuloIDOld != null && !articuloIDOld.equals(articuloIDNew)) {
                articuloIDOld.getStockList().remove(stock);
                articuloIDOld = em.merge(articuloIDOld);
            }
            if (articuloIDNew != null && !articuloIDNew.equals(articuloIDOld)) {
                articuloIDNew.getStockList().add(stock);
                articuloIDNew = em.merge(articuloIDNew);
            }
            for (Historia historiaListOldHistoria : historiaListOld) {
                if (!historiaListNew.contains(historiaListOldHistoria)) {
                    historiaListOldHistoria.getStockList().remove(stock);
                    historiaListOldHistoria = em.merge(historiaListOldHistoria);
                }
            }
            for (Historia historiaListNewHistoria : historiaListNew) {
                if (!historiaListOld.contains(historiaListNewHistoria)) {
                    historiaListNewHistoria.getStockList().add(stock);
                    historiaListNewHistoria = em.merge(historiaListNewHistoria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = stock.getId();
                if (findStock(id) == null) {
                    throw new NonexistentEntityException("The stock with id " + id + " no longer exists.");
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
            Stock stock;
            try {
                stock = em.getReference(Stock.class, id);
                stock.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The stock with id " + id + " no longer exists.", enfe);
            }
            Articulo articuloID = stock.getArticuloID();
            if (articuloID != null) {
                articuloID.getStockList().remove(stock);
                articuloID = em.merge(articuloID);
            }
            List<Historia> historiaList = stock.getHistoriaList();
            for (Historia historiaListHistoria : historiaList) {
                historiaListHistoria.getStockList().remove(stock);
                historiaListHistoria = em.merge(historiaListHistoria);
            }
            em.remove(stock);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Stock> findStockEntities() {
        return findStockEntities(true, -1, -1);
    }

    public List<Stock> findStockEntities(int maxResults, int firstResult) {
        return findStockEntities(false, maxResults, firstResult);
    }

    private List<Stock> findStockEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Stock.class));
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

    public Stock findStock(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Stock.class, id);
        } finally {
            em.close();
        }
    }

    public int getStockCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Stock> rt = cq.from(Stock.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
