package ar.nex.syscontrol.caja;

import ar.nex.syscontrol.cliente.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ar.nex.syscontrol.MainApp;
import ar.nex.syscontrol.partido.Partido;
import ar.nex.syscontrol.partido.PartidoJpaController;
import java.util.List;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class CajaMainController implements Initializable {

    @FXML
    TableView<Partido> tblPartido;
    ObservableList<Partido> dataPartido = FXCollections.observableArrayList();
    FilteredList<Partido> filteredPartido = new FilteredList<>(dataPartido);
    private Partido selectPartido;

    @FXML
    TableView<Cliente> tblJugador;
    ObservableList<Cliente> dataJugador = FXCollections.observableArrayList();
    FilteredList<Cliente> filteredJuagador = new FilteredList<>(dataJugador);
    private Cliente selectJugador;

    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colEstado;
    @FXML
    private TableColumn<?, ?> colComentario;
    @FXML
    private TableColumn<?, ?> colJugador;
    @FXML
    private TableColumn<?, ?> colAccion;

    @FXML
    Button addnewBtn;
    @FXML
    Button updateBtn;
    @FXML
    Button deleteBtn;
    @FXML
    TextField searchBox;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //Tabla Partido
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        //Tabla Jugador
        colJugador.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colAccion.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        InitService();
        loadDataPartido();
    }

    private EntityManagerFactory factory;
    private PartidoJpaController jpaPartido;
    private ClienteJpaController jpaJugador;

    public void InitService() {
        System.out.println("ar.sys.Cliente.ClienteService.<init>()");
        factory = Persistence.createEntityManagerFactory("SysControl-PU");
        jpaPartido = new PartidoJpaController(factory);
        jpaJugador = new ClienteJpaController(factory);
    }

    public void loadDataPartido() {
        System.out.println("ar.nex.syscontrol.partido.PartidoController.loadDataPartido()");
        try {
            dataPartido.clear();
            List<Partido> lst = jpaPartido.findPartidoEntities();
            for (Partido p : lst) {
                dataPartido.add(p);
                tblPartido.setItems(dataPartido);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataJugador() {
        System.out.println("ar.nex.syscontrol.partido.PartidoController.loadDataJugador()");
        try {
            dataJugador.clear();
            List<Cliente> lst = jpaJugador.findClienteEntities();
            for (Cliente p : lst) {
                dataJugador.add(p);
                tblJugador.setItems(dataJugador);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showOnClick() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.showOnClick()");
        try {
            //Cliente user = (Cliente) tblPartido.getSelectionModel().getSelectedItem();
            Partido p = (Partido) tblPartido.getSelectionModel().getSelectedItem();
            selectPartido = jpaPartido.findPartido(p.getId());
            //clienteSelect = jpaPartido.findCliente(user.getId());

            dataJugador.clear();
            List<Cliente> lst = (List<Cliente>) selectPartido.getClienteCollection();
            for (Cliente l : lst) {
                dataJugador.add(l);
                tblJugador.setItems(dataJugador);
            }

//            boxNombre.setText(user.getNombre());
//            boxTelefono.setText(user.getTelefono());
//            boxSaldo.setText(user.getSaldo().toString());
//            boxComentario.setText(user.getComentario());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void Delete() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.Delete()");
        try {
            if (confirmDialog()) {
                //jpaPartido.destroy(clienteSelect.getId());
                //MainApp.showInformationAlertBox("Cliente '" + clienteSelect.getNombre() + "' Deleted Successfully!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        //loadDatabaseData();
    }

    @FXML
    public void Update() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.Update()");
        try {
//            clienteSelect.setNombre(boxNombre.getText());
//            clienteSelect.setTelefono(boxTelefono.getText());
//            clienteSelect.setSaldo(Double.valueOf(boxSaldo.getText()));
//            clienteSelect.setComentario(boxComentario.getText());
            //jpaPartido.edit(clienteSelect);
            //MainApp.showInformationAlertBox("Cliente '" + boxNombre.getText() + "' Updated Successfully!");
            // loadDatabaseData();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public boolean confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        //alert.setHeaderText("Confirma que desea ELIMINAR al cliente: " + clienteSelect.getNombre());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     */
    @FXML
    public void AddPartido() throws Exception {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you ok with this?");

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField partido = new TextField();
        partido.setPromptText("Partido");
        TextField fecha = new TextField();
        fecha.setPromptText("Fecha");
        TextField hora = new TextField();
        hora.setPromptText("Hora");

        grid.add(new Label("Partido:"), 0, 0);
        grid.add(partido, 1, 0);
        grid.add(new Label("Fecha:"), 0, 1);
        grid.add(fecha, 1, 1);
        grid.add(new Label("Hora:"), 0, 2);
        grid.add(hora, 1, 2);

        alert.getDialogPane().setContent(grid);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Partido p = new Partido();
            p.setNombre(partido.getText());
            p.setFecha(fecha.getText());
            p.setHora(hora.getText());
            jpaPartido.create(p);
            loadDataPartido();
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    void goSignOut() throws IOException {
        MainApp.showLogin();
    }

    @FXML
    public void VerPartidos() {
        System.out.println("ar.nex.syscontrol.cliente.ClienteController.VerPartidos()");
        try {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Look, an Information Dialog");

//            String lst = "";
//            for (Partido p : clienteSelect.getPartidoCollection()) {
//                lst += p.toString() + "\n";
//            }
            // alert.setContentText("Partidos: " + lst);
            alert.showAndWait();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
