package ar.nex.stock;

import ar.nex.app.MainApp;
import ar.nex.articulo.Articulo;
import ar.nex.jpa.HistoriaJpaController;
import ar.nex.jpa.StockJpaController;
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
    private Label lblStockSelect;
    @FXML
    private Label lblHistoriaSelect;

    ObservableList<Stock> data = FXCollections.observableArrayList();
    FilteredList<Stock> filteredData = new FilteredList<>(data);
    @FXML
    private TableView<Stock> tableStock;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colArticulo;
    @FXML
    private TableColumn<?, ?> colCantidad;

    ObservableList<Historia> dataHistoria = FXCollections.observableArrayList();
    FilteredList<Historia> filteredHistoria = new FilteredList<>(dataHistoria);
    @FXML
    private TableView<Historia> tableHistoria;
    @FXML
    private TableColumn<?, ?> colHFecha;
    @FXML
    private TableColumn<?, ?> colHEvento;
    @FXML
    private TableColumn<?, ?> colHCantidad;

    private StockJpaController jpaStock;
    private Stock selectStock;

    private HistoriaJpaController jpaHistoria;
    private Historia selectHistoria;

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
        
        selectStock = null;
        selectHistoria = null;

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colHFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHEvento.setCellValueFactory(new PropertyValueFactory<>("evento"));
        colHCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        InitService();
        loadDataStock();

        btnMas.setOnAction(e -> editarStock(true));
        btnMenos.setOnAction(e -> editarStock(false));
        btnMenu.setOnAction(e -> MainApp.showMe(102));

    }

    public void InitService() {
        System.out.println("ar.nex.articulo.ArticuloController.InitService()");
        try {
            jpaStock = new StockJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
            jpaHistoria = new HistoriaJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StockJpaController getService() {
        System.out.println("ar.nex.articulo.ArticuloController.getService()");
        return this.jpaStock;
    }

    public void loadDataStock() {
        System.out.println("ar.nex.articulo.ArticuloController.loadDataStock()");
        try {
            this.data.clear();
            List<Stock> lst = jpaStock.findStockEntities();
            for (Stock item : lst) {
                this.data.add(item);
                this.tableStock.setItems(data);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void loadDataHistoria(Stock s) {
        System.out.println("ar.nex.stock.StockController.loadDataHistoria() : " + s.getArticulo());
        try {
            this.dataHistoria.clear();
            List<Historia> lst = s.getHistoriaList();
            Collections.reverse(lst); 
            for (Historia item : lst) {
                this.dataHistoria.add(item);
                this.tableHistoria.setItems(dataHistoria);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void crearStock(Articulo articulo, String fecha, Integer cantidad) {
        System.out.println("ar.nex.stock.StockController.crearStock() ---- " + articulo.toString());
        GetPK pk = new GetPK();
        try {

            Stock stock = new Stock();
            stock.setId(pk.Nuevo(Stock.class));

            stock.setFecha(fecha);
            stock.setArticulo(articulo);
            stock.setCantidad(cantidad);

            this.InitService();
            jpaStock.create(stock);

            System.out.println("ar.nex.stock.StockController.crearStock() : " + stock.toString());

            new HistoriaController().crearHistoria(stock);

            //stock.addHistoria(new Historia(pk.Nuevo(Historia.class),"hoy", "Nuevo Articulo creado", cantidad));                        
            //jpaStock.edit(stock);
        } catch (Exception ex) {
            Logger.getLogger(StockController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void editarStock(boolean suma) {
        System.out.println("ar.nex.stock.StockController.editarStock()");
        try {
            if (selectStock != null) {

                Stage dialog = new Stage();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/stock/StockEditar.fxml"));
                StockEditar controller = new StockEditar(selectStock, suma);
                loader.setController(controller);

                Scene scene = new Scene(loader.load());

                dialog.setScene(scene);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.resizableProperty().setValue(Boolean.FALSE);

                dialog.showAndWait();

                loadDataStock();
                loadDataHistoria(jpaStock.findStock(selectStock.getId()));
            }
        } catch (Exception e) {
            System.err.print(e);
        }
    }

    @FXML
    public void showOnClick() {
        System.out.println("ar.nex.stock.StockController.showOnClick()");
        try {
            Stock item = (Stock) tableStock.getSelectionModel().getSelectedItem();
            selectStock = jpaStock.findStock(item.getId());
            loadDataHistoria(selectStock);
            lblStockSelect.setText("Articulo: " + selectStock.getArticulo().getNombre() + " - Cantidad: " + selectStock.getCantidad());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void showHsitoriaSelect() {
        try {
            Historia item = (Historia) tableHistoria.getSelectionModel().getSelectedItem();
            selectHistoria = jpaHistoria.findHistoria(item.getId());
            lblHistoriaSelect.setText(selectHistoria.getEventoFull());
        } catch (Exception e) {
        }
    }

    @FXML
    public void Search() {
        boxBuscar.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super Stock>) item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (item.getArticulo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (item.getArticulo().getNombre().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
        SortedList<Stock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tableStock.comparatorProperty());
        tableStock.setItems(sortedData);
    }

}
