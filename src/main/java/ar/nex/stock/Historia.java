/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ar.nex.stock;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Renzo
 */
@Entity
@Table(name = "historia")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Historia.findAll", query = "SELECT h FROM Historia h"),
    @NamedQuery(name = "Historia.findById", query = "SELECT h FROM Historia h WHERE h.id = :id"),
    @NamedQuery(name = "Historia.findByFecha", query = "SELECT h FROM Historia h WHERE h.fecha = :fecha"),
    @NamedQuery(name = "Historia.findByEvento", query = "SELECT h FROM Historia h WHERE h.evento = :evento"),
    @NamedQuery(name = "Historia.findByCantidad", query = "SELECT h FROM Historia h WHERE h.cantidad = :cantidad")})
public class Historia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "fecha")
    private String fecha;
    @Column(name = "evento")
    private String evento;
    @Column(name = "cantidad")
    private Integer cantidad;
    @JoinTable(name = "stock_historia", joinColumns = {
        @JoinColumn(name = "historia", referencedColumnName = "id")}, inverseJoinColumns = {
        @JoinColumn(name = "stock", referencedColumnName = "id")})
    @ManyToMany
    private List<Stock> stockList;

    public Historia() {
    }

    public Historia(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @XmlTransient
    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Historia)) {
            return false;
        }
        Historia other = (Historia) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ar.nex.articulo.Historia[ id=" + id + " ]";
    }
    
}
