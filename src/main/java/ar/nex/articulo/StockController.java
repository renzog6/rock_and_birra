package ar.nex.articulo;

import ar.nex.app.MainApp;
import ar.nex.jpa.ArticuloJpaController;
import ar.nex.jpa.StockDetalleJpaController;
import ar.nex.util.GetPK;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class StockController implements Initializable {

    @FXML
    private Button btnMenu;
    @FXML
    private Button btnMas;
    @FXML
    private Button btnMenos;
    @FXML
    private Button btnEditar;
    @FXML
    private TextField boxBuscar;
    @FXML
    private Label lblArticuloSelect;
    @FXML
    private Label lblHistoriaSelect;

    ObservableList<Articulo> data = FXCollections.observableArrayList();
    FilteredList<Articulo> filteredData = new FilteredList<>(data);
    @FXML
    private TableView<Articulo> tableArticulo;
    @FXML
    private TableColumn<?, ?> colCodigo;
    @FXML
    private TableColumn<?, ?> colArticulo;
    @FXML
    private TableColumn<?, ?> colCantidad;

    ObservableList<StockDetalle> dataStockDetalle = FXCollections.observableArrayList();
    FilteredList<StockDetalle> filteredStockDetalle = new FilteredList<>(dataStockDetalle);
    @FXML
    private TableView<StockDetalle> tableHistoria;
    @FXML
    private TableColumn<?, ?> colHFecha;
    @FXML
    private TableColumn<?, ?> colHEvento;
    @FXML
    private TableColumn<?, ?> colHCantidad;

    private ArticuloJpaController jpaArticulo;
    private Articulo selectArticulo;

    private StockDetalleJpaController jpaStockDetalle;
    private StockDetalle selectStockDetalle;

    private static StockController instance;

    public static StockController getInstance() {
        return instance;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        StockController.instance = this;

        selectArticulo = null;
        selectStockDetalle = null;

        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colHFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHEvento.setCellValueFactory(new PropertyValueFactory<>("evento"));
        colHCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        InitService();
        loadDataArticulo();

        btnMas.setOnAction(e -> editarArticulo(true));
        btnMenos.setOnAction(e -> editarArticulo(false));
        btnMenu.setOnAction(e -> MainApp.showMe(102));

    }

    public void InitService() {
        System.out.println("ar.nex.articulo.ArticuloController.InitService()");
        try {
            jpaArticulo = new ArticuloJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
            jpaStockDetalle = new StockDetalleJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArticuloJpaController getService() {
        System.out.println("ar.nex.articulo.ArticuloController.getService()");
        return this.jpaArticulo;
    }

    public void loadDataArticulo() {
        System.out.println("ar.nex.articulo.ArticuloController.loadDataArticulo()");
        try {
            this.data.clear();
            List<Articulo> lst = jpaArticulo.findArticuloEntities();
            for (Articulo item : lst) {
                this.data.add(item);
                this.tableArticulo.setItems(data);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void loadDataStockDetalle(Articulo s) {
        try {
            this.dataStockDetalle.clear();
            List<StockDetalle> lst = s.getStockDetalleList();
            Collections.reverse(lst);
            for (StockDetalle item : lst) {
                this.dataStockDetalle.add(item);                
            }
            this.tableHistoria.setItems(dataStockDetalle);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void crearArticulo(Articulo articulo, String fecha, Integer cantidad) {
        System.out.println("ar.nex.stock.ArticuloController.crearArticulo() ---- " + articulo.toString());
        GetPK pk = new GetPK();
        try {

            Articulo stock = new Articulo();
            stock.setId(pk.Nuevo(Articulo.class));

//            stock.setFecha(fecha);
//            stock.setArticulo(articulo);
//            stock.setCantidad(cantidad);
            this.InitService();
            jpaArticulo.create(stock);

            System.out.println("ar.nex.stock.ArticuloController.crearArticulo() : " + stock.toString());
//
//            new ArticuloDetalleController().crearStockDetalle(stock);

            //stock.addStockDetalle(new StockDetalle(pk.Nuevo(StockDetalle.class),"hoy", "Nuevo Articulo creado", cantidad));                        
            //jpaArticulo.edit(stock);
        } catch (Exception ex) {
            Logger.getLogger(ArticuloController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void editarArticulo(boolean suma) {
        System.out.println("ar.nex.articulo.StockController.editarArticulo()");
        try {
            if (selectArticulo != null) {

                Stage dialog = new Stage();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/articulo/StockEditar.fxml"));
                StockEditar controller = new StockEditar(selectArticulo, suma);
                loader.setController(controller);

                Scene scene = new Scene(loader.load());

                dialog.setScene(scene);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.resizableProperty().setValue(Boolean.FALSE);

                dialog.showAndWait();

                loadDataArticulo();
                loadDataStockDetalle(jpaArticulo.findArticulo(selectArticulo.getId()));
            }
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    @FXML
    public void showOnClick() {
        System.out.println("ar.nex.stock.ArticuloController.showOnClick()");
        try {
            //Articulo item = (Articulo) tableArticulo.getSelectionModel().getSelectedItem();
            //selectArticulo = jpaArticulo.findArticulo(item.getId());
            selectArticulo = (Articulo) tableArticulo.getSelectionModel().getSelectedItem();
            loadDataStockDetalle(selectArticulo);
            lblArticuloSelect.setText("Articulo: " + selectArticulo.toString() + " - Cantidad: " + selectArticulo.getStock());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void showHsitoriaSelect() {
        try {
            //StockDetalle item = (StockDetalle) tableStockDetalle.getSelectionModel().getSelectedItem();
           // selectStockDetalle = jpaStockDetalle.findStockDetalle(item.getId());
           selectStockDetalle =(StockDetalle) tableHistoria.getSelectionModel().getSelectedItem();
            lblHistoriaSelect.setText(selectStockDetalle.getEventoFull());
        } catch (Exception e) {
        }
    }

    @FXML
    public void Search() {
        boxBuscar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Articulo>) item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
//                if (item.getArticulo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
//                    return true;
//                } else if (item.getArticulo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
//                    return true;
//                }
                return false;
            });
        });
        SortedList<Articulo> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableArticulo.comparatorProperty());
        tableArticulo.setItems(sortedData);
    }

}
