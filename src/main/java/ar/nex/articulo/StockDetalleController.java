package ar.nex.articulo;

import ar.nex.jpa.StockDetalleJpaController;
import ar.nex.util.GetPK;
import java.net.URL;
import java.time.LocalDate;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class StockDetalleController implements Initializable {

    ObservableList<StockDetalle> dataStockDetalle = FXCollections.observableArrayList();
    FilteredList<StockDetalle> filteredStockDetalle = new FilteredList<>(dataStockDetalle);
    @FXML
    private TableView<StockDetalle> tableStockDetalle;
    @FXML
    private TableColumn<?, ?> colHFecha;
    @FXML
    private TableColumn<?, ?> colHEvento;
    @FXML
    private TableColumn<?, ?> colHCantidad;

    private StockDetalleJpaController jpaStockDetalle;
    private StockDetalle historia;

    private static StockDetalleController instance;

    public static StockDetalleController getInstance() {
        return instance;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colHFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHEvento.setCellValueFactory(new PropertyValueFactory<>("evento"));
        colHCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

        InitService();
        loadDatabaseData();
    }

    public void InitService() {
        System.out.println("ar.nex.articulo.ArticuloController.InitService()");
        try {
            jpaStockDetalle = new StockDetalleJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public StockDetalleJpaController getService() {
        System.out.println("ar.nex.articulo.ArticuloController.getService()");
        return this.jpaStockDetalle;
    }

    public void loadDatabaseData() {
        System.out.println("ar.nex.articulo.ArticuloController.loadDatabaseData()");
        try {
            this.dataStockDetalle.clear();
            List<StockDetalle> lst = jpaStockDetalle.findStockDetalleEntities();
            for (StockDetalle item : lst) {
                this.dataStockDetalle.add(item);
                this.tableStockDetalle.setItems(dataStockDetalle);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void crearStockDetalle(Articulo articulo, String fecha) {
        System.out.println("ar.nex.articulo.StockDetalleController.crearStockDetalle()");
        GetPK pk = new GetPK();
        try {

            historia = new StockDetalle();
            historia.setId(pk.Nuevo(StockDetalle.class));
            historia.setFecha(fecha);
            historia.setArticulo(articulo);
            historia.setCantidad(articulo.getStock());
            historia.setEvento("Nuevo Articulo creado");

            this.InitService();
            jpaStockDetalle.create(historia);
        } catch (Exception ex) {
            Logger.getLogger(StockDetalleController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void editarStockDetalle(Articulo articulo, String fecha, String detalle, int cantidad) {

        System.out.println("ar.nex.stock.StockDetalleController.editarStockDetalle()");
        GetPK pk = new GetPK();
        try {

            historia = new StockDetalle();
            historia.setId(pk.Nuevo(StockDetalle.class));
            
            historia.setFecha(fecha);
            historia.setArticulo(articulo);
            historia.setEvento("Detalle : " + detalle);
            historia.setCantidad(cantidad);

            this.InitService();
            jpaStockDetalle.create(historia);
        } catch (Exception ex) {
            Logger.getLogger(StockDetalleController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void Update(ActionEvent event) {
    }

}
