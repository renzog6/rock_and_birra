package ar.nex.articulo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ar.nex.app.MainApp;
import ar.nex.syscontrol.config.HistorialService;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javax.persistence.Persistence;

public class ArticuloController implements Initializable {

    HistorialService historial = new HistorialService();

    ObservableList<Articulo> data = FXCollections.observableArrayList();
    FilteredList<Articulo> filteredData = new FilteredList<>(data);

    @FXML
    private Button signOut;

    @FXML
    TableView<Articulo> table;

    @FXML
    private TableColumn<?, ?> colCodigo;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colImporte;
    @FXML
    private TableColumn<?, ?> colObservacion;

    @FXML
    Button btnAdd;
    @FXML
    Button updateBtn;
    @FXML
    Button deleteBtn;
    @FXML
    TextField searchBox;

    private ArticuloJpaController jpaArticulo;

    private Articulo articulo;
    
    private static ArticuloController instance;
    public static ArticuloController getInstance(){
        return instance;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));
        colObservacion.setCellValueFactory(new PropertyValueFactory<>("observacion"));

        instance = this;
        //btnArticulo.setOnAction(e -> MainApp.showMe(2));
        btnAdd.setOnAction(e -> new ArticuloDialog().add());

        InitService();
        loadDatabaseData();
    }

    public void InitService() {
        System.out.println("ar.nex.articulo.ArticuloController.InitService()");
        try {
            jpaArticulo = new ArticuloJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArticuloJpaController getService() {
        System.out.println("ar.nex.articulo.ArticuloController.getService()");
        return this.jpaArticulo;
    }

    public void loadDatabaseData() {
        System.out.println("ar.nex.articulo.ArticuloController.loadDatabaseData()");
        try {
            this.data.clear();
            List<Articulo> lst = jpaArticulo.findArticuloEntities();
            for (Articulo item : lst) {
                this.data.add(item);
                this.table.setItems(data);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    @FXML
    public void showOnClick() {
        try {
            //int id = table.getSelectionModel().getSelectedItem().getId();
            articulo = jpaArticulo.findArticulo(table.getSelectionModel().getSelectedItem().getId());
            System.out.println("Articulo: " + articulo.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void Delete() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.Delete()");
        try {
            if (confirmDialog()) {
                jpaArticulo.destroy(articulo.getId());
                MainApp.showInformationAlertBox("CajaMovTipo '" + articulo.getNombre() + "' Deleted Successfully!");
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

//            //  UpdatePendiente(articulo);
//            articulo.setNombre(boxNombre.getText());
//            articulo.setImporte(Double.parseDouble(boxImporte.getText().replace(",", ".")));
//            articulo.setObservacion(boxComentario.getText());
//            jpaArticulo.edit(articulo);
//            MainApp.showInformationAlertBox("CajaMovTipo '" + boxNombre.getText() + "' Updated Successfully!");
//
//            //UpdatePendiente(articulo);
//            historial.GuardarEvento("El Articulo " + articulo.toString() + " fue Actualizado.");
//            loadDatabaseData();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Articulo>) user -> {
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
        SortedList<Articulo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedData);
    }

    public boolean confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirma que desea ELIMINAR el articulo: " + articulo.getNombre());

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

}
