/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.articulo;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.articulo.Articulo;
import ar.nex.articulo.Categoria;
import ar.nex.stock.exceptions.NonexistentEntityException;
import ar.nex.stock.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class CategoriaJpaController implements Serializable {

    public CategoriaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Categoria categoria) throws PreexistingEntityException, Exception {
        if (categoria.getArticuloList() == null) {
            categoria.setArticuloList(new ArrayList<Articulo>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Articulo> attachedArticuloList = new ArrayList<Articulo>();
            for (Articulo articuloListArticuloToAttach : categoria.getArticuloList()) {
                articuloListArticuloToAttach = em.getReference(articuloListArticuloToAttach.getClass(), articuloListArticuloToAttach.getId());
                attachedArticuloList.add(articuloListArticuloToAttach);
            }
            categoria.setArticuloList(attachedArticuloList);
            em.persist(categoria);
            for (Articulo articuloListArticulo : categoria.getArticuloList()) {
                Categoria oldCategoriaIDOfArticuloListArticulo = articuloListArticulo.getCategoriaID();
                articuloListArticulo.setCategoriaID(categoria);
                articuloListArticulo = em.merge(articuloListArticulo);
                if (oldCategoriaIDOfArticuloListArticulo != null) {
                    oldCategoriaIDOfArticuloListArticulo.getArticuloList().remove(articuloListArticulo);
                    oldCategoriaIDOfArticuloListArticulo = em.merge(oldCategoriaIDOfArticuloListArticulo);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCategoria(categoria.getId()) != null) {
                throw new PreexistingEntityException("Categoria " + categoria + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Categoria categoria) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria persistentCategoria = em.find(Categoria.class, categoria.getId());
            List<Articulo> articuloListOld = persistentCategoria.getArticuloList();
            List<Articulo> articuloListNew = categoria.getArticuloList();
            List<Articulo> attachedArticuloListNew = new ArrayList<Articulo>();
            for (Articulo articuloListNewArticuloToAttach : articuloListNew) {
                articuloListNewArticuloToAttach = em.getReference(articuloListNewArticuloToAttach.getClass(), articuloListNewArticuloToAttach.getId());
                attachedArticuloListNew.add(articuloListNewArticuloToAttach);
            }
            articuloListNew = attachedArticuloListNew;
            categoria.setArticuloList(articuloListNew);
            categoria = em.merge(categoria);
            for (Articulo articuloListOldArticulo : articuloListOld) {
                if (!articuloListNew.contains(articuloListOldArticulo)) {
                    articuloListOldArticulo.setCategoriaID(null);
                    articuloListOldArticulo = em.merge(articuloListOldArticulo);
                }
            }
            for (Articulo articuloListNewArticulo : articuloListNew) {
                if (!articuloListOld.contains(articuloListNewArticulo)) {
                    Categoria oldCategoriaIDOfArticuloListNewArticulo = articuloListNewArticulo.getCategoriaID();
                    articuloListNewArticulo.setCategoriaID(categoria);
                    articuloListNewArticulo = em.merge(articuloListNewArticulo);
                    if (oldCategoriaIDOfArticuloListNewArticulo != null && !oldCategoriaIDOfArticuloListNewArticulo.equals(categoria)) {
                        oldCategoriaIDOfArticuloListNewArticulo.getArticuloList().remove(articuloListNewArticulo);
                        oldCategoriaIDOfArticuloListNewArticulo = em.merge(oldCategoriaIDOfArticuloListNewArticulo);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = categoria.getId();
                if (findCategoria(id) == null) {
                    throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.");
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
            Categoria categoria;
            try {
                categoria = em.getReference(Categoria.class, id);
                categoria.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The categoria with id " + id + " no longer exists.", enfe);
            }
            List<Articulo> articuloList = categoria.getArticuloList();
            for (Articulo articuloListArticulo : articuloList) {
                articuloListArticulo.setCategoriaID(null);
                articuloListArticulo = em.merge(articuloListArticulo);
            }
            em.remove(categoria);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Categoria> findCategoriaEntities() {
        return findCategoriaEntities(true, -1, -1);
    }

    public List<Categoria> findCategoriaEntities(int maxResults, int firstResult) {
        return findCategoriaEntities(false, maxResults, firstResult);
    }

    private List<Categoria> findCategoriaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Categoria.class));
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

    public Categoria findCategoria(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoriaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Categoria> rt = cq.from(Categoria.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
