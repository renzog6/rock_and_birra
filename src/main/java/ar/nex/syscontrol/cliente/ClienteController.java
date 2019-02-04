package ar.nex.syscontrol.cliente;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ar.nex.syscontrol.MainApp;
import ar.nex.syscontrol.partido.Partido;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ClienteController implements Initializable {

    ObservableList<Cliente> data = FXCollections.observableArrayList();
    FilteredList<Cliente> filteredData = new FilteredList<>(data);

    @FXML
    TableView<Cliente> table;

    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colTelefono;
    @FXML
    private TableColumn<Cliente, Double> colSaldo;
    @FXML
    private TableColumn<?, ?> colComentario;

    @FXML
    Button addnewBtn;
    @FXML
    Button updateBtn;
    @FXML
    Button deleteBtn;
    @FXML
    TextField searchBox;

    @FXML
    TextField boxTelefono;
    @FXML
    TextField boxNombre;
    @FXML
    TextField boxSaldo;
    @FXML
    TextField boxComentario;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        ClienteService();
        loadDatabaseData();
    }

    private EntityManagerFactory factory;// = Persistence.createEntityManagerFactory("SysServices-PU");
    private ClienteJpaController dao;// = new ClienteJpaController(factory);

    public void ClienteService() {
        System.out.println("ar.sys.Cliente.ClienteService.<init>()");
        factory = Persistence.createEntityManagerFactory("SysControl-PU");
        dao = new ClienteJpaController(factory);
    }

    public void loadDatabaseData() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.loadDatabaseData()");
        try {
            boxNombre.clear();
            boxTelefono.clear();
            boxSaldo.clear();
            boxComentario.clear();
            data.clear();

            //ClienteService();
            List<Cliente> lst = dao.findClienteEntities();
            for (Cliente cliente : lst) {
                data.add(cliente);
                table.setItems(data);
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @FXML
    public void Add() throws Exception {
        System.out.println("ar.nex.syscontrol.config.ConfigController.Add()");

        String name = boxNombre.getText();
        String user = boxTelefono.getText();
        String pass = boxSaldo.getText();

        try {
            Cliente u = new Cliente();
            u.setNombre(name);
            u.setTelefono(user);
            u.setComentario(pass);
            dao.create(u);
        } catch (Exception e) {
            System.out.println(e);
        }
        loadDatabaseData();
        MainApp.showInformationAlertBox("New Cliente '" + name + "' Added Successfully!");
    }

    static Cliente clienteSelect;
    @FXML
    public void showOnClick() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.showOnClick()");
        try {
            Cliente user = (Cliente) table.getSelectionModel().getSelectedItem();
            clienteSelect = dao.findCliente(user.getId());

            boxNombre.setText(user.getNombre());
            boxTelefono.setText(user.getTelefono());
            boxSaldo.setText(user.getSaldo().toString());
            boxComentario.setText(user.getComentario());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void Delete() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.Delete()");
        try {
            if (confirmDialog()) {
                dao.destroy(clienteSelect.getId());
                MainApp.showInformationAlertBox("Cliente '" + clienteSelect.getNombre() + "' Deleted Successfully!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        loadDatabaseData();
    }

    @FXML
    public void Update() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.Update()");
        try {
            clienteSelect.setNombre(boxNombre.getText());
            clienteSelect.setTelefono(boxTelefono.getText());
            clienteSelect.setSaldo(Double.valueOf(boxSaldo.getText()));
            clienteSelect.setComentario(boxComentario.getText());
            dao.edit(clienteSelect);
            MainApp.showInformationAlertBox("Cliente '" + boxNombre.getText() + "' Updated Successfully!");
            loadDatabaseData();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Cliente>) user -> {
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
        SortedList<Cliente> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    public boolean confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirma que desea ELIMINAR al cliente: " + clienteSelect.getNombre());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }
    
    @FXML
    void goSignOut() throws IOException {
        MainApp.showMainMenu();
    }

    @FXML
    public void VerPartidos() {
        System.out.println("ar.nex.syscontrol.cliente.ClienteController.VerPartidos()");
        try {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Look, an Information Dialog");

            String lst = "";
            for (Partido p : clienteSelect.getPartidoCollection()) {
                lst += p.toString() + "\n";
            }
            alert.setContentText("Partidos: " + lst);

            alert.showAndWait();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
