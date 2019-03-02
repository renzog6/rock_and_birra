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
import ar.nex.compra.Compra;
import ar.nex.jpa.exceptions.NonexistentEntityException;
import ar.nex.jpa.exceptions.PreexistingEntityException;
import ar.nex.venta.Venta;
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
        if (articulo.getCompraList() == null) {
            articulo.setCompraList(new ArrayList<Compra>());
        }
        if (articulo.getVentaList() == null) {
            articulo.setVentaList(new ArrayList<Venta>());
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
            List<Compra> attachedCompraList = new ArrayList<Compra>();
            for (Compra compraListCompraToAttach : articulo.getCompraList()) {
                compraListCompraToAttach = em.getReference(compraListCompraToAttach.getClass(), compraListCompraToAttach.getId());
                attachedCompraList.add(compraListCompraToAttach);
            }
            articulo.setCompraList(attachedCompraList);
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : articulo.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getId());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            articulo.setVentaList(attachedVentaList);
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
            for (Compra compraListCompra : articulo.getCompraList()) {
                Articulo oldArticuloIDOfCompraListCompra = compraListCompra.getArticuloID();
                compraListCompra.setArticuloID(articulo);
                compraListCompra = em.merge(compraListCompra);
                if (oldArticuloIDOfCompraListCompra != null) {
                    oldArticuloIDOfCompraListCompra.getCompraList().remove(compraListCompra);
                    oldArticuloIDOfCompraListCompra = em.merge(oldArticuloIDOfCompraListCompra);
                }
            }
            for (Venta ventaListVenta : articulo.getVentaList()) {
                Articulo oldArticuloIDOfVentaListVenta = ventaListVenta.getArticuloID();
                ventaListVenta.setArticuloID(articulo);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldArticuloIDOfVentaListVenta != null) {
                    oldArticuloIDOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldArticuloIDOfVentaListVenta = em.merge(oldArticuloIDOfVentaListVenta);
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
            List<Compra> compraListOld = persistentArticulo.getCompraList();
            List<Compra> compraListNew = articulo.getCompraList();
            List<Venta> ventaListOld = persistentArticulo.getVentaList();
            List<Venta> ventaListNew = articulo.getVentaList();
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
            List<Compra> attachedCompraListNew = new ArrayList<Compra>();
            for (Compra compraListNewCompraToAttach : compraListNew) {
                compraListNewCompraToAttach = em.getReference(compraListNewCompraToAttach.getClass(), compraListNewCompraToAttach.getId());
                attachedCompraListNew.add(compraListNewCompraToAttach);
            }
            compraListNew = attachedCompraListNew;
            articulo.setCompraList(compraListNew);
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getId());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            articulo.setVentaList(ventaListNew);
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
            for (Compra compraListOldCompra : compraListOld) {
                if (!compraListNew.contains(compraListOldCompra)) {
                    compraListOldCompra.setArticuloID(null);
                    compraListOldCompra = em.merge(compraListOldCompra);
                }
            }
            for (Compra compraListNewCompra : compraListNew) {
                if (!compraListOld.contains(compraListNewCompra)) {
                    Articulo oldArticuloIDOfCompraListNewCompra = compraListNewCompra.getArticuloID();
                    compraListNewCompra.setArticuloID(articulo);
                    compraListNewCompra = em.merge(compraListNewCompra);
                    if (oldArticuloIDOfCompraListNewCompra != null && !oldArticuloIDOfCompraListNewCompra.equals(articulo)) {
                        oldArticuloIDOfCompraListNewCompra.getCompraList().remove(compraListNewCompra);
                        oldArticuloIDOfCompraListNewCompra = em.merge(oldArticuloIDOfCompraListNewCompra);
                    }
                }
            }
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    ventaListOldVenta.setArticuloID(null);
                    ventaListOldVenta = em.merge(ventaListOldVenta);
                }
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Articulo oldArticuloIDOfVentaListNewVenta = ventaListNewVenta.getArticuloID();
                    ventaListNewVenta.setArticuloID(articulo);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldArticuloIDOfVentaListNewVenta != null && !oldArticuloIDOfVentaListNewVenta.equals(articulo)) {
                        oldArticuloIDOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldArticuloIDOfVentaListNewVenta = em.merge(oldArticuloIDOfVentaListNewVenta);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = articulo.getId();
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

    public void destroy(Integer id) throws NonexistentEntityException {
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
            List<Compra> compraList = articulo.getCompraList();
            for (Compra compraListCompra : compraList) {
                compraListCompra.setArticuloID(null);
                compraListCompra = em.merge(compraListCompra);
            }
            List<Venta> ventaList = articulo.getVentaList();
            for (Venta ventaListVenta : ventaList) {
                ventaListVenta.setArticuloID(null);
                ventaListVenta = em.merge(ventaListVenta);
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

    public Articulo findArticulo(Integer id) {
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
