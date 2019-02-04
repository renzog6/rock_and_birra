package ar.nex.syscontrol.caja;

import ar.nex.syscontrol.cliente.ClienteJpaController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
//import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class ArticuloAddClienteController implements Initializable {

    @FXML
    private Button btnAceptar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField lstArticulo;

    private CajaMovTipo articulo;

    private EntityManagerFactory factory;
    private CajaMovTipoJpaController jpaArticulo;
    private CajaMovClienteJpaController jpaCliente;

    public void InitService() {
        System.out.println("ar.sys.Cliente.ClienteService.<init>()");
        factory = Persistence.createEntityManagerFactory("SysControl-PU");
        jpaArticulo = new CajaMovTipoJpaController(factory);
        jpaCliente = new CajaMovClienteJpaController(factory);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        InitService();
        List<CajaMovTipo> lst = jpaArticulo.findCajaMovTipoEntities();

        TextFields.bindAutoCompletion(lstArticulo, lst).setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<CajaMovTipo>>() {
            @Override
            public void handle(AutoCompletionBinding.AutoCompletionEvent<CajaMovTipo> event) {
                System.out.println(".handle()" + event.getCompletion().getNombre());
                articulo = event.getCompletion();
            }
        });
    }

    @FXML
    private void addArticulo(ActionEvent event) throws IOException, Exception {
        System.out.println("addArticulo(ActionEvent event): " + articulo.toString());
        
        CajaMovCliente m = new CajaMovCliente();
        m.setClienteId(1);
        m.setArticulo(articulo.getNombre());
        m.setImporte(articulo.getImporte());
        m.setEstado(0);
        
        jpaCliente.create(m);
                
    }

}
