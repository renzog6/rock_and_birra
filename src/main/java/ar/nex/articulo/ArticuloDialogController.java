package ar.nex.articulo;

import ar.nex.app.MainApp;
import ar.nex.jpa.ArticuloJpaController;
import ar.nex.stock.StockController;
import ar.nex.util.GetPK;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class ArticuloDialogController implements Initializable {

    public ArticuloDialogController() {
    }

    public ArticuloDialogController(Articulo a) {
        System.out.println("ar.nex.articulo.ArticuloDialogController.<init>() : " + a.toString());
        this.articulo = a;
    }

    private Articulo articulo;

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
    private DatePicker boxFecha;

    @FXML
    private ToggleGroup group;
    @FXML
    private RadioButton rbSi;
    @FXML
    private RadioButton rbNo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxCodigo.requestFocus();
            }
        });

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

        InitControls();
        InitService();
    }

    private ArticuloJpaController jpaArticulo;

    public void InitService() {
        System.out.println("ar.nex.articulo.ArticuloController.InitService()");
        try {
            jpaArticulo = new ArticuloJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void InitControls() {
        try {
            boxCodigo.setText(articulo.getCodigo());
            boxNombre.setText(articulo.getNombre());
            boxCompra.setText(articulo.getPrecioCompra().toString());
            boxVenta.setText(articulo.getPrecioVenta().toString());

            if (articulo.getId() == 0) {
                boxStock.setText("0");
            } else {
                boxStock.setText(articulo.getStock().getCantidad().toString());
                boxStock.setDisable(true);
                boxFecha.setDisable(true);
            }
            
            if (articulo.getCompraVenta() == 0) {
                rbSi.setSelected(true);
            } else {
                rbNo.setSelected(true);
            }
            
            boxFecha.setValue(LocalDate.now());

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
        try {

            articulo.setCodigo(boxCodigo.getText());
            articulo.setNombre(boxNombre.getText());
            articulo.setPrecioCompra(Double.parseDouble(boxCompra.getText().replace(",", ".")));
            articulo.setPrecioVenta(Double.parseDouble(boxVenta.getText().replace(",", ".")));
            articulo.setObservacion(boxObservacion.getText());

            if (articulo.getId() == 0) {
                GetPK pk = new GetPK();
                articulo.setId(pk.Nuevo(Articulo.class));
                ArticuloController.getInstance().getService().create(articulo);

                new StockController().crearStock(articulo, boxFecha.getValue().toString(),Integer.valueOf(boxStock.getText()));
            } else {                
                ArticuloController.getInstance().getService().edit(articulo);
            }

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
        ArticuloController.getInstance().loadDatabaseData();
        MainApp.showInformationAlertBox("Nuevo Aticulo: " + "VAR ACA" + " Added Successfully!");
    }

}
