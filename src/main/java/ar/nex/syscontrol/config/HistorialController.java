package ar.nex.syscontrol.config;

import ar.nex.app.MainApp;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class HistorialController implements Initializable {

    private HistorialService service;

    @FXML
    private TextField searchBox;
    @FXML
    private Button goMenu;
    @FXML
    private TableView<Historial> table;
    @FXML
    private TableColumn<?, ?> colId;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colEvento;

    ObservableList<Historial> data = FXCollections.observableArrayList();
    FilteredList<Historial> filteredData = new FilteredList<>(data);

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        searchBox.clear();
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEvento.setCellValueFactory(new PropertyValueFactory<>("evento"));

        service = new HistorialService();
        loadData();
        service.GuardarEvento("Ingreso al Listado de Eventos del Historial.");
    }

    private void loadData() {
        List<Historial> lst = service.ListHistorial();
        for (Historial item : lst) {
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
    private void goMenu(ActionEvent event) {
           try {           
            MainApp.showMe(102);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showOnClick(MouseEvent event) {
    }

}
