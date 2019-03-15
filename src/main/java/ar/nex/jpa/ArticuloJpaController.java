/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.jpa;

import ar.nex.articulo.Articulo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ar.nex.articulo.Categoria;
import ar.nex.stock.Stock;
import ar.nex.compra.Proveedor;
import java.util.ArrayList;
import java.util.List;
import ar.nex.compra.Pedido;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Renzo
 */
public class ArticuloJpaController implements Serializable {

    public ArticuloJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Articulo articulo) throws PreexistingEntityException, Exception {
        if (articulo.getProveedorList() == null) {
            articulo.setProveedorList(new ArrayList<Proveedor>());
        }
        if (articulo.getPedidoList() == null) {
            articulo.setPedidoList(new ArrayList<Pedido>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Categoria categoriaID = articulo.getCategoriaID();
            if (categoriaID != null) {
                categoriaID = em.getReference(categoriaID.getClass(), categoriaID.getId());
                articulo.setCategoriaID(categoriaID);
            }
            Stock stock = articulo.getStock();
            if (stock != null) {
                stock = em.getReference(stock.getClass(), stock.getId());
                articulo.setStock(stock);
            }
            List<Proveedor> attachedProveedorList = new ArrayList<Proveedor>();
            for (Proveedor proveedorListProveedorToAttach : articulo.getProveedorList()) {
                proveedorListProveedorToAttach = em.getReference(proveedorListProveedorToAttach.getClass(), proveedorListProveedorToAttach.getId());
                attachedProveedorList.add(proveedorListProveedorToAttach);
            }
            articulo.setProveedorList(attachedProveedorList);
            List<Pedido> attachedPedidoList = new ArrayList<Pedido>();
            for (Pedido pedidoListPedidoToAttach : articulo.getPedidoList()) {
                pedidoListPedidoToAttach = em.getReference(pedidoListPedidoToAttach.getClass(), pedidoListPedidoToAttach.getId());
                attachedPedidoList.add(pedidoListPedidoToAttach);
            }
            articulo.setPedidoList(attachedPedidoList);
            em.persist(articulo);
            if (categoriaID != null) {
                categoriaID.getArticuloList().add(articulo);
                categoriaID = em.merge(categoriaID);
            }
            if (stock != null) {
                Articulo oldArticuloOfStock = stock.getArticulo();
                if (oldArticuloOfStock != null) {
                    oldArticuloOfStock.setStock(null);
                    oldArticuloOfStock = em.merge(oldArticuloOfStock);
                }
                stock.setArticulo(articulo);
                stock = em.merge(stock);
            }
            for (Proveedor proveedorListProveedor : articulo.getProveedorList()) {
                proveedorListProveedor.getArticuloList().add(articulo);
                proveedorListProveedor = em.merge(proveedorListProveedor);
            }
            for (Pedido pedidoListPedido : articulo.getPedidoList()) {
                Articulo oldArticuloOfPedidoListPedido = pedidoListPedido.getArticulo();
                pedidoListPedido.setArticulo(articulo);
                pedidoListPedido = em.merge(pedidoListPedido);
                if (oldArticuloOfPedidoListPedido != null) {
                    oldArticuloOfPedidoListPedido.getPedidoList().remove(pedidoListPedido);
                    oldArticuloOfPedidoListPedido = em.merge(oldArticuloOfPedidoListPedido);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findArticulo(articulo.getId()) != null) {
                throw new PreexistingEntityException("Articulo " + articulo + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Articulo articulo) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Articulo persistentArticulo = em.find(Articulo.class, articulo.getId());
            Categoria categoriaIDOld = persistentArticulo.getCategoriaID();
            Categoria categoriaIDNew = articulo.getCategoriaID();
            Stock stockOld = persistentArticulo.getStock();
            Stock stockNew = articulo.getStock();
            List<Proveedor> proveedorListOld = persistentArticulo.getProveedorList();
            List<Proveedor> proveedorListNew = articulo.getProveedorList();
            List<Pedido> pedidoListOld = persistentArticulo.getPedidoList();
            List<Pedido> pedidoListNew = articulo.getPedidoList();
            if (categoriaIDNew != null) {
                categoriaIDNew = em.getReference(categoriaIDNew.getClass(), categoriaIDNew.getId());
                articulo.setCategoriaID(categoriaIDNew);
            }
            if (stockNew != null) {
                stockNew = em.getReference(stockNew.getClass(), stockNew.getId());
                articulo.setStock(stockNew);
            }
            List<Proveedor> attachedProveedorListNew = new ArrayList<Proveedor>();
            for (Proveedor proveedorListNewProveedorToAttach : proveedorListNew) {
                proveedorListNewProveedorToAttach = em.getReference(proveedorListNewProveedorToAttach.getClass(), proveedorListNewProveedorToAttach.getId());
                attachedProveedorListNew.add(proveedorListNewProveedorToAttach);
            }
            proveedorListNew = attachedProveedorListNew;
            articulo.setProveedorList(proveedorListNew);
            List<Pedido> attachedPedidoListNew = new ArrayList<Pedido>();
            for (Pedido pedidoListNewPedidoToAttach : pedidoListNew) {
                pedidoListNewPedidoToAttach = em.getReference(pedidoListNewPedidoToAttach.getClass(), pedidoListNewPedidoToAttach.getId());
                attachedPedidoListNew.add(pedidoListNewPedidoToAttach);
            }
            pedidoListNew = attachedPedidoListNew;
            articulo.setPedidoList(pedidoListNew);
            articulo = em.merge(articulo);
            if (categoriaIDOld != null && !categoriaIDOld.equals(categoriaIDNew)) {
                categoriaIDOld.getArticuloList().remove(articulo);
                categoriaIDOld = em.merge(categoriaIDOld);
            }
            if (categoriaIDNew != null && !categoriaIDNew.equals(categoriaIDOld)) {
                categoriaIDNew.getArticuloList().add(articulo);
                categoriaIDNew = em.merge(categoriaIDNew);
            }
            if (stockOld != null && !stockOld.equals(stockNew)) {
                stockOld.setArticulo(null);
                stockOld = em.merge(stockOld);
            }
            if (stockNew != null && !stockNew.equals(stockOld)) {
                Articulo oldArticuloOfStock = stockNew.getArticulo();
                if (oldArticuloOfStock != null) {
                    oldArticuloOfStock.setStock(null);
                    oldArticuloOfStock = em.merge(oldArticuloOfStock);
                }
                stockNew.setArticulo(articulo);
                stockNew = em.merge(stockNew);
            }
            for (Proveedor proveedorListOldProveedor : proveedorListOld) {
                if (!proveedorListNew.contains(proveedorListOldProveedor)) {
                    proveedorListOldProveedor.getArticuloList().remove(articulo);
                    proveedorListOldProveedor = em.merge(proveedorListOldProveedor);
                }
            }
            for (Proveedor proveedorListNewProveedor : proveedorListNew) {
                if (!proveedorListOld.contains(proveedorListNewProveedor)) {
                    proveedorListNewProveedor.getArticuloList().add(articulo);
                    proveedorListNewProveedor = em.merge(proveedorListNewProveedor);
                }
            }
            for (Pedido pedidoListOldPedido : pedidoListOld) {
                if (!pedidoListNew.contains(pedidoListOldPedido)) {
                    pedidoListOldPedido.setArticulo(null);
                    pedidoListOldPedido = em.merge(pedidoListOldPedido);
                }
            }
            for (Pedido pedidoListNewPedido : pedidoListNew) {
                if (!pedidoListOld.contains(pedidoListNewPedido)) {
                    Articulo oldArticuloOfPedidoListNewPedido = pedidoListNewPedido.getArticulo();
                    pedidoListNewPedido.setArticulo(articulo);
                    pedidoListNewPedido = em.merge(pedidoListNewPedido);
                    if (oldArticuloOfPedidoListNewPedido != null && !oldArticuloOfPedidoListNewPedido.equals(articulo)) {
                        oldArticuloOfPedidoListNewPedido.getPedidoList().remove(pedidoListNewPedido);
                        oldArticuloOfPedidoListNewPedido = em.merge(oldArticuloOfPedidoListNewPedido);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = articulo.getId();
                if (findArticulo(id) == null) {
                    throw new NonexistentEntityException("The articulo with id " + id + " no longer exists.");
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
            Articulo articulo;
            try {
                articulo = em.getReference(Articulo.class, id);
                articulo.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The articulo with id " + id + " no longer exists.", enfe);
            }
            Categoria categoriaID = articulo.getCategoriaID();
            if (categoriaID != null) {
                categoriaID.getArticuloList().remove(articulo);
                categoriaID = em.merge(categoriaID);
            }
            Stock stock = articulo.getStock();
            if (stock != null) {
                stock.setArticulo(null);
                stock = em.merge(stock);
            }
            List<Proveedor> proveedorList = articulo.getProveedorList();
            for (Proveedor proveedorListProveedor : proveedorList) {
                proveedorListProveedor.getArticuloList().remove(articulo);
                proveedorListProveedor = em.merge(proveedorListProveedor);
            }
            List<Pedido> pedidoList = articulo.getPedidoList();
            for (Pedido pedidoListPedido : pedidoList) {
                pedidoListPedido.setArticulo(null);
                pedidoListPedido = em.merge(pedidoListPedido);
            }
            em.remove(articulo);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Articulo> findArticuloEntities() {
        return findArticuloEntities(true, -1, -1);
    }

    public List<Articulo> findArticuloEntities(int maxResults, int firstResult) {
        return findArticuloEntities(false, maxResults, firstResult);
    }

    private List<Articulo> findArticuloEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Articulo.class));
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

    public Articulo findArticulo(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Articulo.class, id);
        } finally {
            em.close();
        }
    }

    public int getArticuloCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Articulo> rt = cq.from(Articulo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
