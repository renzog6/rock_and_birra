package ar.nex.venta;

import ar.nex.compra.*;
import ar.nex.app.MainApp;
import ar.nex.jpa.ClienteJpaController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class ClienteController implements Initializable {

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnMenu;

    @FXML
    private TextField searchBox;

    ObservableList<Cliente> dataCliente = FXCollections.observableArrayList();
    FilteredList<Cliente> filteredCliente = new FilteredList<>(dataCliente);
    @FXML
    private TableView<Cliente> tableCliente;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colTelefono;
    @FXML
    private TableColumn<?, ?> colSaldo;
    @FXML
    private TableColumn<?, ?> colObservacion;

    private ClienteJpaController jpaCliente;

    private Cliente cliente;

    private static ClienteController instance;

    public static ClienteController getInstance() {
        return instance;
    }
    @FXML
    private DatePicker boxFecha;
    @FXML
    private ComboBox<?> cbxCliente;
    @FXML
    private TextField boxCantidad;
    @FXML
    private TextField boxPrecio;
    @FXML
    private Button btnAdd1;
    @FXML
    private ComboBox<?> cbxArticulo;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;
    @FXML
    private Label lvlTotalArticulo;
    @FXML
    private Label lblTotal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
        instance = this;

        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));        
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        colObservacion.setCellValueFactory(new PropertyValueFactory<>("observacion"));

        btnAdd.setOnAction(e -> this.add());
        btnEdit.setOnAction(e -> this.edit());
        btnMenu.setOnAction(e -> MainApp.showMe(102));

        InitService();
        loadDataCliente();
    }

    public void InitService() {
        System.out.println("ar.nex.compra.ClienteController.InitService()");
        try {
            jpaCliente = new ClienteJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ClienteJpaController getService() {
        return this.jpaCliente;
    }

    public void loadDataCliente() {
        System.out.println("ar.nex.compra.ClienteController.loadDataCliente()");
        try {
            this.dataCliente.clear();
            List<Cliente> lst = jpaCliente.findClienteEntities();
            for (Cliente item : lst) {                
                this.dataCliente.add(item);
            }
            this.tableCliente.setItems(dataCliente);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void add() {
        System.out.println("ar.nex.compra.ClienteController.add()");
        this.cliente = new Cliente();
        edit();
    }

    public void edit() {
        System.out.println("ar.nex.compra.ClienteController.edit()");
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/venta/ClienteDialog.fxml"));
            ClienteDialogController controller = new ClienteDialogController(cliente);
            loader.setController(controller);

            Scene scene = new Scene(loader.load());

            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);

            dialog.showAndWait();

        } catch (IOException e) {
            System.err.print(e);
        }
    }

    @FXML
    public void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredCliente.setPredicate((Predicate<? super Cliente>) user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (user.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (user.getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Cliente> sortedData = new SortedList<>(filteredCliente);
        sortedData.comparatorProperty().bind(tableCliente.comparatorProperty());
        tableCliente.setItems(sortedData);
    }

    @FXML
    private void showOnClick(MouseEvent event) {
        try {
            cliente = jpaCliente.findCliente(tableCliente.getSelectionModel().getSelectedItem().getId());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
