package ar.nex.syscontrol.caja;

import ar.nex.syscontrol.exceptions.NonexistentEntityException;
import ar.nex.syscontrol.exceptions.PreexistingEntityException;
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
public class CajaMovTipoJpaController implements Serializable {

    public CajaMovTipoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CajaMovTipo cajaMovTipo) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cajaMovTipo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCajaMovTipo(cajaMovTipo.getId()) != null) {
                throw new PreexistingEntityException("CajaMovTipo " + cajaMovTipo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CajaMovTipo cajaMovTipo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cajaMovTipo = em.merge(cajaMovTipo);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cajaMovTipo.getId();
                if (findCajaMovTipo(id) == null) {
                    throw new NonexistentEntityException("The cajaMovTipo with id " + id + " no longer exists.");
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
            CajaMovTipo cajaMovTipo;
            try {
                cajaMovTipo = em.getReference(CajaMovTipo.class, id);
                cajaMovTipo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cajaMovTipo with id " + id + " no longer exists.", enfe);
            }
            em.remove(cajaMovTipo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CajaMovTipo> findCajaMovTipoEntities() {
        return findCajaMovTipoEntities(true, -1, -1);
    }

    public List<CajaMovTipo> findCajaMovTipoEntities(int maxResults, int firstResult) {
        return findCajaMovTipoEntities(false, maxResults, firstResult);
    }

    private List<CajaMovTipo> findCajaMovTipoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CajaMovTipo.class));
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

    public CajaMovTipo findCajaMovTipo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CajaMovTipo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCajaMovTipoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CajaMovTipo> rt = cq.from(CajaMovTipo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
