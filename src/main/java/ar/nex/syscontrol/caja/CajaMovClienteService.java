package ar.nex.syscontrol.caja;

import ar.nex.syscontrol.config.*;
import ar.nex.syscontrol.caja.CajaMovClienteJpaController;
import ar.nex.syscontrol.cliente.Cliente;
import ar.nex.syscontrol.cliente.ClienteJpaController;
import com.mysql.fabric.xmlrpc.Client;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class CajaMovClienteService implements Serializable {

    private static final long serialVersionUID = 1L;

    private EntityManagerFactory factory;
    private CajaMovClienteJpaController service;
    private CajaMovCliente mov;
    private ClienteJpaController clienteService;

    public CajaMovClienteService() {
        this.factory = Persistence.createEntityManagerFactory("SysControl-PU");
        this.service = new CajaMovClienteJpaController(factory);
        this.clienteService = new ClienteJpaController(factory);
    }

    public void UpdateMovCLiente(CajaMovTipo m) {
        System.out.println("ar.nex.syscontrol.caja.CajaMovClienteService.UpdateMovCLiente()" + m.toString());
        try {
            List<CajaMovCliente> lst = service.findCajaMovPendiente();
            for (CajaMovCliente p : lst) {
                String art = p.getArticulo();
                if (art.contains(m.getNombre()) && !(art.contains("Saldo"))) {
                    p.setImporte(m.getImporte());
                    service.edit(p);
                    UpdateSaldoCliente(p.getClienteId());
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(CajaMovClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void UpdateSaldoCliente(Integer clienteID) {
        try {
            Cliente cliente = clienteService.findCliente(clienteID);
            List<CajaMovCliente> lst = service.findCajaMovPendiente(clienteID);
            double saldo = 0.0;
            for (CajaMovCliente p : lst) {
                saldo = saldo + p.getImporte();
            }
            cliente.setSaldo(saldo);
            clienteService.edit(cliente);
        } catch (Exception ex) {
            Logger.getLogger(CajaMovClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<CajaMovCliente> ListCajaMovCliente() {

        return this.service.findCajaMovClienteEntities();
    }

}
