package ar.nex.compra;

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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class ProveedorDialogController implements Initializable {

    public ProveedorDialogController() {
    }

    public ProveedorDialogController(Proveedor p) {
        this.proveedor = p;
    }

    private Proveedor proveedor;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    @FXML
    private Label lblTitulo;

    @FXML
    private TextField boxCuit;
    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxObservacion;
    @FXML
    private TextField boxTelefono;
    @FXML
    private TextField boxDireccion;

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
            if (proveedor.getId() == 0) {
                lblTitulo.setText("Nuevo Proveedor");
            } else {
                lblTitulo.setText("Editar Proveedor");
            }

            boxNombre.setText(proveedor.getNombre());
            boxCuit.setText(proveedor.getCuit());
            boxTelefono.setText(proveedor.getTelefono());
            boxDireccion.setText(proveedor.getDireccion());
            boxObservacion.setText(proveedor.getObservacion());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void guardar(ActionEvent event) {
        try {
            proveedor.setNombre(boxNombre.getText());
            proveedor.setCuit(boxCuit.getText());
            proveedor.setTelefono(boxTelefono.getText());
            proveedor.setDireccion(boxDireccion.getText());
            proveedor.setObservacion(boxObservacion.getText());

            if (proveedor.getId() == 0) {
                GetPK pk = new GetPK();
                proveedor.setId(pk.Nuevo(Proveedor.class));
                ProveedorController.getInstance().getService().create(proveedor);
            } else {
                ProveedorController.getInstance().getService().edit(proveedor);
            }

            ((Node) (event.getSource())).getScene().getWindow().hide();
            
             
            ProveedorController.getInstance().loadDataProveedor();
            MainApp.showInformationAlertBox(proveedor.getNombre() + " - OK");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
