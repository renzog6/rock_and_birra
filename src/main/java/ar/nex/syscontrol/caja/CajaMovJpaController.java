/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.syscontrol.caja;

import ar.nex.syscontrol.caja.CajaMov;
import ar.nex.syscontrol.exceptions.NonexistentEntityException;
import ar.nex.syscontrol.exceptions.PreexistingEntityException;
import ar.nex.syscontrol.partido.Partido;
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
public class CajaMovJpaController implements Serializable {

    public CajaMovJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CajaMov cajaMov) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cajaMov);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCajaMov(cajaMov.getId()) != null) {
                throw new PreexistingEntityException("CajaMov " + cajaMov + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CajaMov cajaMov) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cajaMov = em.merge(cajaMov);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cajaMov.getId();
                if (findCajaMov(id) == null) {
                    throw new NonexistentEntityException("The cajaMov with id " + id + " no longer exists.");
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
            CajaMov cajaMov;
            try {
                cajaMov = em.getReference(CajaMov.class, id);
                cajaMov.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cajaMov with id " + id + " no longer exists.", enfe);
            }
            em.remove(cajaMov);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CajaMov> findCajaMovEntities() {
        return findCajaMovEntities(true, -1, -1);
    }

    public List<CajaMov> findCajaMovEntities(int maxResults, int firstResult) {
        return findCajaMovEntities(false, maxResults, firstResult);
    }

    private List<CajaMov> findCajaMovEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CajaMov.class));
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

    public CajaMov findCajaMov(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CajaMov.class, id);
        } finally {
            em.close();
        }
    }

    public CajaMov findLast() {
        EntityManager em = getEntityManager();
        try {            
            Query q = em.createQuery("SELECT p FROM CajaMov p ORDER BY p.id DESC", CajaMov.class);
            return (CajaMov) q.getResultList().get(0);
        } finally {
            em.close();
        }
    }

    public int getCajaMovCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CajaMov> rt = cq.from(CajaMov.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
