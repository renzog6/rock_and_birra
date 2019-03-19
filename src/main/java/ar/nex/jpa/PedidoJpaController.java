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
import ar.nex.compra.Compra;
import ar.nex.articulo.Pedido;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import ar.nex.venta.Venta;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class PedidoJpaController implements Serializable {

    public PedidoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedido pedido) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo articulo = pedido.getArticulo();
            if (articulo != null) {
                articulo = em.getReference(articulo.getClass(), articulo.getId());
                pedido.setArticulo(articulo);
            }
            Compra compra = pedido.getCompra();
            if (compra != null) {
                compra = em.getReference(compra.getClass(), compra.getId());
                pedido.setCompra(compra);
            }
            Venta venta = pedido.getVenta();
            if (venta != null) {
                venta = em.getReference(venta.getClass(), venta.getId());
                pedido.setVenta(venta);
            }
            em.persist(pedido);
            if (articulo != null) {
                articulo.getPedidoList().add(pedido);
                articulo = em.merge(articulo);
            }
            if (compra != null) {
                compra.getPedidoList().add(pedido);
                compra = em.merge(compra);
            }
            if (venta != null) {
                venta.getPedidoList().add(pedido);
                venta = em.merge(venta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPedido(pedido.getId()) != null) {
                throw new PreexistingEntityException("Pedido " + pedido + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pedido pedido) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pedido persistentPedido = em.find(Pedido.class, pedido.getId());
            Articulo articuloOld = persistentPedido.getArticulo();
            Articulo articuloNew = pedido.getArticulo();
            Compra compraOld = persistentPedido.getCompra();
            Compra compraNew = pedido.getCompra();
            Venta ventaOld = persistentPedido.getVenta();
            Venta ventaNew = pedido.getVenta();
            if (articuloNew != null) {
                articuloNew = em.getReference(articuloNew.getClass(), articuloNew.getId());
                pedido.setArticulo(articuloNew);
            }
            if (compraNew != null) {
                compraNew = em.getReference(compraNew.getClass(), compraNew.getId());
                pedido.setCompra(compraNew);
            }
            if (ventaNew != null) {
                ventaNew = em.getReference(ventaNew.getClass(), ventaNew.getId());
                pedido.setVenta(ventaNew);
            }
            pedido = em.merge(pedido);
            if (articuloOld != null && !articuloOld.equals(articuloNew)) {
                articuloOld.getPedidoList().remove(pedido);
                articuloOld = em.merge(articuloOld);
            }
            if (articuloNew != null && !articuloNew.equals(articuloOld)) {
                articuloNew.getPedidoList().add(pedido);
                articuloNew = em.merge(articuloNew);
            }
            if (compraOld != null && !compraOld.equals(compraNew)) {
                compraOld.getPedidoList().remove(pedido);
                compraOld = em.merge(compraOld);
            }
            if (compraNew != null && !compraNew.equals(compraOld)) {
                compraNew.getPedidoList().add(pedido);
                compraNew = em.merge(compraNew);
            }
            if (ventaOld != null && !ventaOld.equals(ventaNew)) {
                ventaOld.getPedidoList().remove(pedido);
                ventaOld = em.merge(ventaOld);
            }
            if (ventaNew != null && !ventaNew.equals(ventaOld)) {
                ventaNew.getPedidoList().add(pedido);
                ventaNew = em.merge(ventaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = pedido.getId();
                if (findPedido(id) == null) {
                    throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.");
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
            Pedido pedido;
            try {
                pedido = em.getReference(Pedido.class, id);
                pedido.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedido with id " + id + " no longer exists.", enfe);
            }
            Articulo articulo = pedido.getArticulo();
            if (articulo != null) {
                articulo.getPedidoList().remove(pedido);
                articulo = em.merge(articulo);
            }
            Compra compra = pedido.getCompra();
            if (compra != null) {
                compra.getPedidoList().remove(pedido);
                compra = em.merge(compra);
            }
            Venta venta = pedido.getVenta();
            if (venta != null) {
                venta.getPedidoList().remove(pedido);
                venta = em.merge(venta);
            }
            em.remove(pedido);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pedido> findPedidoEntities() {
        return findPedidoEntities(true, -1, -1);
    }

    public List<Pedido> findPedidoEntities(int maxResults, int firstResult) {
        return findPedidoEntities(false, maxResults, firstResult);
    }

    private List<Pedido> findPedidoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedido.class));
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

    public Pedido findPedido(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedido.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedido> rt = cq.from(Pedido.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
