package ar.nex.compra;

import ar.nex.app.MainApp;
import ar.nex.articulo.Articulo;
import ar.nex.jpa.ArticuloJpaController;
import ar.nex.stock.StockController;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class ProveedorController implements Initializable {

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button deleteBtn;
    @FXML
    private TextField searchBox;
    @FXML
    private Button btnMenu;

    ObservableList<Proveedor> dataProveedor = FXCollections.observableArrayList();
    FilteredList<Proveedor> filteredProveedor = new FilteredList<>(dataProveedor);
    @FXML
    private TableView<Proveedor> tableProveedor;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colCuit;
    @FXML
    private TableColumn<?, ?> colTelefono;
    @FXML
    private TableColumn<?, ?> colDireccion;
    @FXML
    private TableColumn<?, ?> colObservacion;

    private ProveedorJpaController jpaProveedor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        btnMenu.setOnAction(e -> {
            try {
                MainApp.showMainMenu();
            } catch (IOException ex) {
                Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCuit.setCellValueFactory(new PropertyValueFactory<>("cuit"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colObservacion.setCellValueFactory(new PropertyValueFactory<>("observacion"));

        InitService();
        loadDataProveedor();
    }

    public void InitService() {
        System.out.println("ar.nex.compra.ProveedorController.InitService()");
        try {
            jpaProveedor = new ProveedorJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataProveedor() {
        System.out.println("ar.nex.compra.ProveedorController.loadDataProveedor()");
        try {
            this.dataProveedor.clear();
            List<Proveedor> lst = jpaProveedor.findProveedorEntities();
            for (Proveedor item : lst) {                
                this.dataProveedor.add(item);                
                this.tableProveedor.setItems(dataProveedor);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    @FXML
    private void Delete(ActionEvent event) {
    }

    @FXML
    private void Search(InputMethodEvent event) {
    }

    @FXML
    private void Search(KeyEvent event) {
    }

    @FXML
    private void goSignOut(ActionEvent event) {
    }

    @FXML
    private void showOnClick(MouseEvent event) {
    }

}
