package ar.nex.syscontrol.caja;

import ar.nex.syscontrol.config.Historial;
import ar.nex.syscontrol.config.HistorialJpaController;
import ar.nex.syscontrol.config.HistorialService;
import java.io.Serializable;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class CajaMovService implements Serializable {

    private static final long serialVersionUID = 1L;

    private EntityManagerFactory factory;
    private CajaMovJpaController service;
    private HistorialService srvHistorial;

    public CajaMovService() {
        this.factory = Persistence.createEntityManagerFactory("SysControl-PU");
        this.service = new CajaMovJpaController(factory);

    }

    public CajaMovJpaController Get() {
        return service;
    }

    public void cobroCliente(String cliente, String fecha, double importe) {
        try {
            CajaMov mov = new CajaMov();
            mov.setFecha(fecha);
            mov.setMov("Cobro > " + cliente);
            mov.setImporte(importe);
            mov.setSaldo(service.findLast().getSaldo() + importe);
            service.create(mov);
            srvHistorial = new HistorialService();
            srvHistorial.GuardarEvento(mov.toHsitorial());
        } catch (Exception ex) {
            Logger.getLogger(CajaMovService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addMov(String fecha, String tipo, String detalle,double importe) {
        try {
            CajaMov mov = new CajaMov();
            mov.setFecha(fecha);
            mov.setMov(tipo + " > " + detalle);
            if (tipo.contains("Pago")) {
               importe = -1*importe;
            }
            mov.setImporte(importe);
            mov.setSaldo(service.findLast().getSaldo() + importe);
            service.create(mov);
            srvHistorial = new HistorialService();
            srvHistorial.GuardarEvento(mov.toHsitorial());
        } catch (Exception ex) {
            Logger.getLogger(CajaMovService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void updateSaldo() {

    }
}
