package ar.nex.syscontrol.caja;

import ar.nex.syscontrol.config.*;
import ar.nex.syscontrol.MainApp;
import ar.nex.syscontrol.cliente.ClienteJpaController;
import ar.nex.syscontrol.login.LoginController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class CajaDetalleController implements Initializable {

    private EntityManagerFactory factory;
    private CajaMovClienteJpaController movCliente;
    private HistorialService historialService;

    @FXML
    private Label lblCliente;

    @FXML
    private TextField searchBox;
    @FXML
    private Button goMenu;

    ObservableList<CajaMovCliente> data = FXCollections.observableArrayList();
    FilteredList<CajaMovCliente> filteredData = new FilteredList<>(data);

    @FXML
    private TableView<CajaMovCliente> table;

    @FXML
    private TableColumn<?, ?> colFCompra;
    @FXML
    private TableColumn<?, ?> colDetalle;
    @FXML
    private TableColumn<?, ?> colImporte;
    @FXML
    private TableColumn<?, ?> colFPago;
    @FXML
    private TableColumn<?, ?> colComentario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("ar.nex.syscontrol.caja.CajaDetalleController.initialize()");
        lblCliente.setText("Detalle del cliente: "+CajaMovClienteController.getSelectCliente().getNombre());
        
        searchBox.clear();
        InitTable();
        InitService();
        LoadData();
        historialService.GuardarEvento("El usuario < " + LoginController.getUserLogin() + " > ingreso al Detalle de Caja del Cliente: ");
    }

    public void InitTable() {
        System.out.println("ar.nex.syscontrol.caja.CajaDetalleController.InitTable()");
        colFCompra.setCellValueFactory(new PropertyValueFactory<>("fechaCompra"));
        colDetalle.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        colImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));        
        colFPago.setCellValueFactory(new PropertyValueFactory<>("fechaPago"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));        
    }

    public void InitService() {
        System.out.println("ar.nex.syscontrol.caja.CajaDetalleController.InitService()");
        factory = Persistence.createEntityManagerFactory("SysControl-PU");
        movCliente = new CajaMovClienteJpaController(factory);
        historialService = new HistorialService();
    }

    private void LoadData() {
        System.out.println("ar.nex.syscontrol.caja.CajaDetalleController.LoadData()");
        List<CajaMovCliente> lst = movCliente.findCajaMovClienteAll(CajaMovClienteController.getSelectCliente().getId());
        for (CajaMovCliente item : lst) {
            data.add(item);
            table.setItems(data);
        }
    }

    @FXML
    private void Search(InputMethodEvent event) {
    }

    @FXML
    private void Search(KeyEvent event) {
    }

    @FXML
    private void goSalir(ActionEvent event) {
        try {
            // MainApp.showMainMenu();
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            app_stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showOnClick(MouseEvent event) {
    }

}
