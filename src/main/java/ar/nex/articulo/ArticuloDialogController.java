package ar.nex.articulo;

import ar.nex.app.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;

/**
 *
 * @author Renzo
 */
public class ArticuloDialogController implements Initializable {

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    @FXML
    private TextField boxCodigo;
    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxCompra;
    @FXML
    private TextField boxVenta;
    @FXML
    private TextField boxStock;
    @FXML
    private TextField boxObservacion;

    @FXML
    private ToggleGroup group;
    @FXML
    private RadioButton rbSi;
    @FXML
    private RadioButton rbNo;

    //private ArticuloDialog ad;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxCodigo.requestFocus();
            }
        });

        boxCompra.setText("0.0");
        boxVenta.setText("0.0");
        boxStock.setText("0");
        
        btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
        
        group = new ToggleGroup();
        rbSi.setToggleGroup(group);
        rbSi.setSelected(true);
        rbNo.setToggleGroup(group);
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
                // Has selection.
                if (group.getSelectedToggle() != null) {
                    RadioButton button = (RadioButton) group.getSelectedToggle();
                    
                    System.out.println("Button: " + button.getText());                    
                }
            }
        });

        //ad = new ArticuloDialog();
    }

    public void edit(Articulo a) {
        System.out.println("ar.nex.articulo.ArticuloDialogController.edit()");
        boxCodigo.setText(a.getCodigo());
    }

    @FXML
    public void guardar(ActionEvent event) {
        System.out.println("ar.nex.articulo.ArticuloDialogController.guardar()");
        try {
            Articulo u = new Articulo();

            u.setCodigo(boxCodigo.getText());
            u.setNombre(boxNombre.getText());
            u.setPrecioCompra(Double.parseDouble(boxCompra.getText().replace(",", ".")));
            u.setPrecioVenta(Double.parseDouble(boxVenta.getText().replace(",", ".")));            
            u.setObservacion(boxObservacion.getText());
            
            if (Integer.valueOf(boxStock.getText()) != 0) {
                System.out.println("Crear STOCK");
            }

            ArticuloController.getInstance().getService().create(u);
        } catch (Exception e) {
            System.out.println(e);
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
        ArticuloController.getInstance().loadDatabaseData();
        MainApp.showInformationAlertBox("Nuevo Aticulo: " + "VAR ACA" + " Added Successfully!");
    }

}
