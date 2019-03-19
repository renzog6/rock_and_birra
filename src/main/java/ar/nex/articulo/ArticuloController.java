package ar.nex.articulo;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ar.nex.app.MainApp;
import ar.nex.jpa.ArticuloJpaController;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.Persistence;

public class ArticuloController implements Initializable {

//    HistorialService historial = new HistorialService();

    ObservableList<Articulo> data = FXCollections.observableArrayList();
    FilteredList<Articulo> filteredData = new FilteredList<>(data);

    @FXML
    private Button btnMenu;

    @FXML
    TableView<Articulo> table;

    @FXML
    private TableColumn<?, ?> colCodigo;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colPCompra;
    @FXML
    private TableColumn<?, ?> colPVenta;
    @FXML
    private TableColumn<?, ?> colStock;
    @FXML
    private TableColumn<?, ?> colObservacion;

    @FXML
    Button btnAdd;
    @FXML
    Button btnEdit;
    @FXML
    Button deleteBtn;
    @FXML
    TextField searchBox;

    private ArticuloJpaController jpaArticulo;
    private Articulo articulo;

    private static ArticuloController instance;

    public static ArticuloController getInstance() {
        return instance;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        instance = this;
        
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colPCompra.setCellValueFactory(new PropertyValueFactory<>("precioCompra"));
        colPVenta.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colObservacion.setCellValueFactory(new PropertyValueFactory<>("observacion"));      

        btnAdd.setOnAction(e -> this.add());
        btnEdit.setOnAction(e -> this.edit());
        btnMenu.setOnAction(e -> MainApp.showMe(102));

        InitService();
        loadDatabaseData();
    }

    public void InitService() {      
        try {
            jpaArticulo = new ArticuloJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArticuloJpaController getService() {       
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
            System.out.println("Articulo: " + articulo.toString() + " pc: " + articulo.getPrecioCompra() + " pv: " + articulo.getPrecioVenta());
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

    public void add() {
        articulo = new Articulo();
        edit();
    }

    public void edit() {
        System.out.println("ar.nex.articulo.ArticuloDialog.edit() : " + articulo.toString());
        try {
            Stage dialog = new Stage();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ArticuloDialog.fxml"));
            ArticuloDialogController controller = new ArticuloDialogController(articulo);
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

}
