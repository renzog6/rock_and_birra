package ar.nex.syscontrol.config;

import ar.nex.syscontrol.config.HistorialJpaController;
import ar.nex.syscontrol.login.LoginController;
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
public class HistorialService implements Serializable {

    private static final long serialVersionUID = 1L;

    private EntityManagerFactory factory;// = Persistence.createEntityManagerFactory("SysServices-PU");
    private HistorialJpaController service;// = new HistorialJpaController(factory);
    private Historial evento;
    private Date fecha;

    public HistorialService() {
        this.factory = Persistence.createEntityManagerFactory("SysControl-PU");
        this.service = new HistorialJpaController(factory);
        this.evento = null;
        this.fecha = null;
    }

    public void GuardarEvento(String txt) {
        try {
            String u = LoginController.getUserLogin().getName();

            this.evento = new Historial();
            this.evento.setEvento(u + " : [ " + txt + " ]");

            this.fecha = new Date();
            DateFormat formato = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
            this.evento.setFecha(formato.format(fecha));

            this.service.create(evento);
        } catch (Exception ex) {
            Logger.getLogger(HistorialService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Historial> ListHistorial() {
        return this.service.findHistorialEntities();
    }

}
