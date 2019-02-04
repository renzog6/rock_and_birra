/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.syscontrol.caja;

import ar.nex.syscontrol.caja.CajaMovCliente;
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
public class CajaMovClienteJpaController implements Serializable {

    public CajaMovClienteJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CajaMovCliente cajaMovCliente) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(cajaMovCliente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCajaMovCliente(cajaMovCliente.getId()) != null) {
                throw new PreexistingEntityException("CajaMovCliente " + cajaMovCliente + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CajaMovCliente cajaMovCliente) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            cajaMovCliente = em.merge(cajaMovCliente);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = cajaMovCliente.getId();
                if (findCajaMovCliente(id) == null) {
                    throw new NonexistentEntityException("The cajaMovCliente with id " + id + " no longer exists.");
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
            CajaMovCliente cajaMovCliente;
            try {
                cajaMovCliente = em.getReference(CajaMovCliente.class, id);
                cajaMovCliente.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cajaMovCliente with id " + id + " no longer exists.", enfe);
            }
            em.remove(cajaMovCliente);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CajaMovCliente> findCajaMovClienteEntities() {
        return findCajaMovClienteEntities(true, -1, -1);
    }

    public List<CajaMovCliente> findCajaMovClienteEntities(int maxResults, int firstResult) {
        return findCajaMovClienteEntities(false, maxResults, firstResult);
    }

    private List<CajaMovCliente> findCajaMovClienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CajaMovCliente.class));
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

    public List<CajaMovCliente> findCajaMovPendiente() {
        EntityManager em = getEntityManager();
        try {
            Query q;
            q = em.createQuery("SELECT p FROM CajaMovCliente p WHERE p.estado = 0", CajaMovCliente.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<CajaMovCliente> findCajaMovPendiente(int idCliente) {
        EntityManager em = getEntityManager();
        try {
            Query q;
            q = em.createQuery("SELECT p FROM CajaMovCliente p WHERE p.clienteId = " + idCliente + " and p.estado = 0", CajaMovCliente.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
        public List<CajaMovCliente> findCajaMovClienteAll(Integer idCliente) {
        EntityManager em = getEntityManager();
        try {
            Query q;
            q = em.createQuery("SELECT p FROM CajaMovCliente p WHERE p.clienteId = " + idCliente, CajaMovCliente.class);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CajaMovCliente findCajaMovCliente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CajaMovCliente.class, id);
        } finally {
            em.close();
        }
    }

    public int getCajaMovClienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CajaMovCliente> rt = cq.from(CajaMovCliente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
