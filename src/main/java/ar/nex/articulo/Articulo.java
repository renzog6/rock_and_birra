package ar.nex.articulo;

import ar.nex.compra.Proveedor;
import ar.nex.compra.Compra;
import ar.nex.stock.Stock;
import ar.nex.venta.Venta;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Articulo.findAll", query = "SELECT a FROM Articulo a"),
    @NamedQuery(name = "Articulo.findById", query = "SELECT a FROM Articulo a WHERE a.id = :id"),
    @NamedQuery(name = "Articulo.findByCodigo", query = "SELECT a FROM Articulo a WHERE a.codigo = :codigo"),
    @NamedQuery(name = "Articulo.findByNombre", query = "SELECT a FROM Articulo a WHERE a.nombre = :nombre"),
    @NamedQuery(name = "Articulo.findByPrecioCompra", query = "SELECT a FROM Articulo a WHERE a.precioCompra = :precioCompra"),
    @NamedQuery(name = "Articulo.findByPrecioVenta", query = "SELECT a FROM Articulo a WHERE a.precioVenta = :precioVenta"),
    @NamedQuery(name = "Articulo.findByObservacion", query = "SELECT a FROM Articulo a WHERE a.observacion = :observacion"),
    @NamedQuery(name = "Articulo.findByCompraVenta", query = "SELECT a FROM Articulo a WHERE a.compraVenta = :compraVenta"),
    @NamedQuery(name = "Articulo.findByOtro", query = "SELECT a FROM Articulo a WHERE a.otro = :otro")})
public class Articulo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Column(name = "codigo")
    private String codigo;
    @Column(name = "nombre")
    private String nombre;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "precio_compra")
    private Double precioCompra;
    @Column(name = "precio_venta")
    private Double precioVenta;
    @Column(name = "observacion")
    private String observacion;
    @Column(name = "compra_venta")
    private Integer compraVenta;
    @Column(name = "otro")
    private Integer otro;

    @JoinTable(name = "articulo_proveedor", joinColumns = {
        @JoinColumn(name = "articulo", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "proveedor", referencedColumnName = "id")})
    @ManyToMany
    private List<Proveedor> proveedorList;

    @OneToMany(mappedBy = "articuloID")
    private List<Compra> compraList;

    @OneToMany(mappedBy = "articuloID")
    private List<Venta> ventaList;

    @JoinColumn(name = "categoriaID", referencedColumnName = "id")
    @ManyToOne
    private Categoria categoriaID;

//    @OneToMany(mappedBy = "articuloID")
//    private List<Stock> stockList;
    @OneToOne(mappedBy = "articulo", cascade = CascadeType.ALL)
    private Stock stock;

    public Articulo() {
        this.id = 0;
        this.codigo = "";
        this.nombre = "";
        this.precioCompra = 0.0;
        this.precioVenta = 0.0;
        this.observacion = "";
        this.compraVenta = 0;
        this.otro = 0;
    }

    public Articulo(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(Double precioCompra) {
        this.precioCompra = precioCompra;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public Integer getCompraVenta() {
        return compraVenta;
    }

    public void setCompraVenta(Integer compraVenta) {
        this.compraVenta = compraVenta;
    }

    public Integer getOtro() {
        return otro;
    }

    public void setOtro(Integer otro) {
        this.otro = otro;
    }

    @XmlTransient
    public List<Proveedor> getProveedorList() {
        return proveedorList;
    }

    public void setProveedorList(List<Proveedor> proveedorList) {
        this.proveedorList = proveedorList;
    }

    @XmlTransient
    public List<Compra> getCompraList() {
        return compraList;
    }

    public void setCompraList(List<Compra> compraList) {
        this.compraList = compraList;
    }

    @XmlTransient
    public List<Venta> getVentaList() {
        return ventaList;
    }

    public void setVentaList(List<Venta> ventaList) {
        this.ventaList = ventaList;
    }

    public Categoria getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(Categoria categoriaID) {
        this.categoriaID = categoriaID;
    }

//    @XmlTransient
//    public List<Stock> getStockList() {
//        return stockList;
//    }
//
//    public void setStockList(List<Stock> stockList) {
//        this.stockList = stockList;
//    }
    public Stock getStock() {
        return stock;
    }

//    public void setStock(Stock stock) {
//        this.stock = stock;
//    }
    public void setStock(Stock stock) {
        if (stock == null) {
            if (this.stock != null) {
                this.stock.setArticulo(null);
            }
        } else {
            stock.setArticulo(this);
        }
        this.stock = stock;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.id);
        hash = 89 * hash + Objects.hashCode(this.codigo);
        hash = 89 * hash + Objects.hashCode(this.nombre);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Articulo other = (Articulo) obj;
        if (!Objects.equals(this.codigo, other.codigo)) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }

}
