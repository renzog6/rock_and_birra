package ar.nex.venta;

import ar.nex.app.MainApp;
import ar.nex.util.GetPK;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class ClienteDialogController implements Initializable {

    public ClienteDialogController() {
    }

    public ClienteDialogController(Cliente p) {
        this.cliente = p;
    }

    private Cliente cliente;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    @FXML
    private Label lblTitulo;

    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxTelefono;
    @FXML
    private TextField boxObservacion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxNombre.requestFocus();
            }
        });

        btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
        btnGuardar.setOnAction(e -> guardar(e));

        InitControls();
    }

    private void InitControls() {
        try {
            if (cliente.getId() == 0) {
                lblTitulo.setText("Nuevo Cliente");
            } else {
                lblTitulo.setText("Editar Cliente");
            }

            boxNombre.setText(cliente.getNombre());
            boxTelefono.setText(cliente.getTelefono());
            boxObservacion.setText(cliente.getObservacion());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void guardar(ActionEvent event) {
        try {
            cliente.setNombre(boxNombre.getText());
            cliente.setTelefono(boxTelefono.getText());
            cliente.setObservacion(boxObservacion.getText());

            if (cliente.getId() == 0) {
                GetPK pk = new GetPK();
                cliente.setId(pk.Nuevo(Cliente.class));
                ClienteController.getInstance().getService().create(cliente);
            } else {
                ClienteController.getInstance().getService().edit(cliente);
            }

            ((Node) (event.getSource())).getScene().getWindow().hide();

            ClienteController.getInstance().loadDataCliente();
            MainApp.showInformationAlertBox(cliente.getNombre() + " - OK");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
