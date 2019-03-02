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
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import ar.nex.stock.Historia;
import ar.nex.stock.Stock;
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

    public void create(Stock stock) throws PreexistingEntityException, Exception {
        if (stock.getHistoriaList() == null) {
            stock.setHistoriaList(new ArrayList<Historia>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo articulo = stock.getArticulo();
            if (articulo != null) {
                articulo = em.getReference(articulo.getClass(), articulo.getId());
                stock.setArticulo(articulo);
            }
            List<Historia> attachedHistoriaList = new ArrayList<Historia>();
            for (Historia historiaListHistoriaToAttach : stock.getHistoriaList()) {
                historiaListHistoriaToAttach = em.getReference(historiaListHistoriaToAttach.getClass(), historiaListHistoriaToAttach.getId());
                attachedHistoriaList.add(historiaListHistoriaToAttach);
            }
            stock.setHistoriaList(attachedHistoriaList);
            em.persist(stock);
            if (articulo != null) {
                Stock oldStockOfArticulo = articulo.getStock();
                if (oldStockOfArticulo != null) {
                    oldStockOfArticulo.setArticulo(null);
                    oldStockOfArticulo = em.merge(oldStockOfArticulo);
                }
                articulo.setStock(stock);
                articulo = em.merge(articulo);
            }
            for (Historia historiaListHistoria : stock.getHistoriaList()) {
                Stock oldStockOfHistoriaListHistoria = historiaListHistoria.getStock();
                historiaListHistoria.setStock(stock);
                historiaListHistoria = em.merge(historiaListHistoria);
                if (oldStockOfHistoriaListHistoria != null) {
                    oldStockOfHistoriaListHistoria.getHistoriaList().remove(historiaListHistoria);
                    oldStockOfHistoriaListHistoria = em.merge(oldStockOfHistoriaListHistoria);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findStock(stock.getId()) != null) {
                throw new PreexistingEntityException("Stock " + stock + " already exists.", ex);
            }
            throw ex;
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
            Articulo articuloOld = persistentStock.getArticulo();
            Articulo articuloNew = stock.getArticulo();
            List<Historia> historiaListOld = persistentStock.getHistoriaList();
            List<Historia> historiaListNew = stock.getHistoriaList();
            if (articuloNew != null) {
                articuloNew = em.getReference(articuloNew.getClass(), articuloNew.getId());
                stock.setArticulo(articuloNew);
            }
            List<Historia> attachedHistoriaListNew = new ArrayList<Historia>();
            for (Historia historiaListNewHistoriaToAttach : historiaListNew) {
                historiaListNewHistoriaToAttach = em.getReference(historiaListNewHistoriaToAttach.getClass(), historiaListNewHistoriaToAttach.getId());
                attachedHistoriaListNew.add(historiaListNewHistoriaToAttach);
            }
            historiaListNew = attachedHistoriaListNew;
            stock.setHistoriaList(historiaListNew);
            stock = em.merge(stock);
            if (articuloOld != null && !articuloOld.equals(articuloNew)) {
                articuloOld.setStock(null);
                articuloOld = em.merge(articuloOld);
            }
            if (articuloNew != null && !articuloNew.equals(articuloOld)) {
                Stock oldStockOfArticulo = articuloNew.getStock();
                if (oldStockOfArticulo != null) {
                    oldStockOfArticulo.setArticulo(null);
                    oldStockOfArticulo = em.merge(oldStockOfArticulo);
                }
                articuloNew.setStock(stock);
                articuloNew = em.merge(articuloNew);
            }
            for (Historia historiaListOldHistoria : historiaListOld) {
                if (!historiaListNew.contains(historiaListOldHistoria)) {
                    historiaListOldHistoria.setStock(null);
                    historiaListOldHistoria = em.merge(historiaListOldHistoria);
                }
            }
            for (Historia historiaListNewHistoria : historiaListNew) {
                if (!historiaListOld.contains(historiaListNewHistoria)) {
                    Stock oldStockOfHistoriaListNewHistoria = historiaListNewHistoria.getStock();
                    historiaListNewHistoria.setStock(stock);
                    historiaListNewHistoria = em.merge(historiaListNewHistoria);
                    if (oldStockOfHistoriaListNewHistoria != null && !oldStockOfHistoriaListNewHistoria.equals(stock)) {
                        oldStockOfHistoriaListNewHistoria.getHistoriaList().remove(historiaListNewHistoria);
                        oldStockOfHistoriaListNewHistoria = em.merge(oldStockOfHistoriaListNewHistoria);
                    }
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
            Articulo articulo = stock.getArticulo();
            if (articulo != null) {
                articulo.setStock(null);
                articulo = em.merge(articulo);
            }
            List<Historia> historiaList = stock.getHistoriaList();
            for (Historia historiaListHistoria : historiaList) {
                historiaListHistoria.setStock(null);
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
