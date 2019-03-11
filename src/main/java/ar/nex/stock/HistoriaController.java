package ar.nex.stock;

import ar.nex.jpa.HistoriaJpaController;
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
public class HistoriaController implements Initializable {

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

    private HistoriaJpaController jpaHistoria;
    private Historia historia;

    private static HistoriaController instance;

    public static HistoriaController getInstance() {
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
            jpaHistoria = new HistoriaJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HistoriaJpaController getService() {
        System.out.println("ar.nex.articulo.ArticuloController.getService()");
        return this.jpaHistoria;
    }

    public void loadDatabaseData() {
        System.out.println("ar.nex.articulo.ArticuloController.loadDatabaseData()");
        try {
            this.dataHistoria.clear();
            List<Historia> lst = jpaHistoria.findHistoriaEntities();
            for (Historia item : lst) {
                this.dataHistoria.add(item);
                this.tableHistoria.setItems(dataHistoria);
            }
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    public void crearHistoria(Stock stock) {
        System.out.println("ar.nex.historia.HistoriaController.crearHistoria()");
        GetPK pk = new GetPK();
        try {

            historia = new Historia();
            historia.setId(pk.Nuevo(Historia.class));

            historia.setStock(stock);
            historia.setFecha(LocalDate.now().toString());
            historia.setEvento("Nuevo Articulo creado");
            historia.setCantidad(stock.getCantidad());

            this.InitService();
            jpaHistoria.create(historia);
        } catch (Exception ex) {
            Logger.getLogger(HistoriaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void editarHistoria(Stock stock, String fecha, String detalle, int cantidad) {
    
        System.out.println("ar.nex.stock.HistoriaController.editarHistoria()");
        GetPK pk = new GetPK();
        try {

            historia = new Historia();
            historia.setId(pk.Nuevo(Historia.class));

            historia.setStock(stock);
            historia.setFecha(fecha);
            historia.setEvento("Detalle : " + detalle);
            historia.setCantidad(cantidad);

            this.InitService();
            jpaHistoria.create(historia);
        } catch (Exception ex) {
            Logger.getLogger(HistoriaController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void Update(ActionEvent event) {
    }

}
