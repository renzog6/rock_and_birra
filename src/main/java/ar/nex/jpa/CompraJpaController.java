/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.compra.Compra;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.compra.Proveedor;
import ar.nex.articulo.Pedido;
import ar.nex.jpa.exceptions.IllegalOrphanException;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

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

    public void create(Compra compra) throws PreexistingEntityException, Exception {
        if (compra.getPedidoList() == null) {
            compra.setPedidoList(new ArrayList<Pedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Proveedor proveedor = compra.getProveedor();
            if (proveedor != null) {
                proveedor = em.getReference(proveedor.getClass(), proveedor.getId());
                compra.setProveedor(proveedor);
            }
            List<Pedido> attachedPedidoList = new ArrayList<Pedido>();
            for (Pedido pedidoListPedidoToAttach : compra.getPedidoList()) {
                pedidoListPedidoToAttach = em.getReference(pedidoListPedidoToAttach.getClass(), pedidoListPedidoToAttach.getId());
                attachedPedidoList.add(pedidoListPedidoToAttach);
            }
            compra.setPedidoList(attachedPedidoList);
            em.persist(compra);
            if (proveedor != null) {
                proveedor.getCompraList().add(compra);
                proveedor = em.merge(proveedor);
            }
            for (Pedido pedidoListPedido : compra.getPedidoList()) {
                Compra oldCompraOfPedidoListPedido = pedidoListPedido.getCompra();
                pedidoListPedido.setCompra(compra);
                pedidoListPedido = em.merge(pedidoListPedido);
                if (oldCompraOfPedidoListPedido != null) {
                    oldCompraOfPedidoListPedido.getPedidoList().remove(pedidoListPedido);
                    oldCompraOfPedidoListPedido = em.merge(oldCompraOfPedidoListPedido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCompra(compra.getId()) != null) {
                throw new PreexistingEntityException("Compra " + compra + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Compra compra) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Compra persistentCompra = em.find(Compra.class, compra.getId());
            Proveedor proveedorOld = persistentCompra.getProveedor();
            Proveedor proveedorNew = compra.getProveedor();
            List<Pedido> pedidoListOld = persistentCompra.getPedidoList();
            List<Pedido> pedidoListNew = compra.getPedidoList();
            List<String> illegalOrphanMessages = null;
            for (Pedido pedidoListOldPedido : pedidoListOld) {
                if (!pedidoListNew.contains(pedidoListOldPedido)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedido " + pedidoListOldPedido + " since its compra field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (proveedorNew != null) {
                proveedorNew = em.getReference(proveedorNew.getClass(), proveedorNew.getId());
                compra.setProveedor(proveedorNew);
            }
            List<Pedido> attachedPedidoListNew = new ArrayList<Pedido>();
            for (Pedido pedidoListNewPedidoToAttach : pedidoListNew) {
                pedidoListNewPedidoToAttach = em.getReference(pedidoListNewPedidoToAttach.getClass(), pedidoListNewPedidoToAttach.getId());
                attachedPedidoListNew.add(pedidoListNewPedidoToAttach);
            }
            pedidoListNew = attachedPedidoListNew;
            compra.setPedidoList(pedidoListNew);
            compra = em.merge(compra);
            if (proveedorOld != null && !proveedorOld.equals(proveedorNew)) {
                proveedorOld.getCompraList().remove(compra);
                proveedorOld = em.merge(proveedorOld);
            }
            if (proveedorNew != null && !proveedorNew.equals(proveedorOld)) {
                proveedorNew.getCompraList().add(compra);
                proveedorNew = em.merge(proveedorNew);
            }
            for (Pedido pedidoListNewPedido : pedidoListNew) {
                if (!pedidoListOld.contains(pedidoListNewPedido)) {
                    Compra oldCompraOfPedidoListNewPedido = pedidoListNewPedido.getCompra();
                    pedidoListNewPedido.setCompra(compra);
                    pedidoListNewPedido = em.merge(pedidoListNewPedido);
                    if (oldCompraOfPedidoListNewPedido != null && !oldCompraOfPedidoListNewPedido.equals(compra)) {
                        oldCompraOfPedidoListNewPedido.getPedidoList().remove(pedidoListNewPedido);
                        oldCompraOfPedidoListNewPedido = em.merge(oldCompraOfPedidoListNewPedido);
                    }
                }
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

    public void destroy(int id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            List<Pedido> pedidoListOrphanCheck = compra.getPedidoList();
            for (Pedido pedidoListOrphanCheckPedido : pedidoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Compra (" + compra + ") cannot be destroyed since the Pedido " + pedidoListOrphanCheckPedido + " in its pedidoList field has a non-nullable compra field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Proveedor proveedor = compra.getProveedor();
            if (proveedor != null) {
                proveedor.getCompraList().remove(compra);
                proveedor = em.merge(proveedor);
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
