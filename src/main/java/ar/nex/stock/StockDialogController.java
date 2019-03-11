package ar.nex.stock;

import ar.nex.app.MainApp;
import ar.nex.articulo.Articulo;
import ar.nex.articulo.ArticuloController;
import ar.nex.jpa.StockJpaController;
import ar.nex.util.GetPK;
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
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class StockDialogController implements Initializable {

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

        InitService();
    }

    private StockJpaController jpaStock;

    public void InitService() {
        System.out.println("ar.nex.articulo.ArticuloController.InitService()");
        try {
            jpaStock = new StockJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void edit(Articulo a) {
        System.out.println("ar.nex.articulo.ArticuloDialogController.edit()");
        boxCodigo.setText(a.getCodigo());
    }

    @FXML
    public void guardar(ActionEvent event) {
        System.out.println("ar.nex.articulo.ArticuloDialogController.guardar()");
        GetPK pk = new GetPK();
        try {
            Articulo articulo = new Articulo();
            articulo.setId(pk.Nuevo(Articulo.class));

            articulo.setCodigo(boxCodigo.getText());
            articulo.setNombre(boxNombre.getText());
            articulo.setPrecioCompra(Double.parseDouble(boxCompra.getText().replace(",", ".")));
            articulo.setPrecioVenta(Double.parseDouble(boxVenta.getText().replace(",", ".")));
            articulo.setObservacion(boxObservacion.getText());

            ArticuloController.getInstance().getService().create(articulo);
          
            new StockController().crearStock(articulo, "fecha",Integer.valueOf(boxStock.getText()));                             
                        
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
        ArticuloController.getInstance().loadDatabaseData();
        MainApp.showInformationAlertBox("Nuevo Aticulo: " + "VAR ACA" + " Added Successfully!");
    }

}
