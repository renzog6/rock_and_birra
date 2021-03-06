package ar.nex.compra;

import ar.nex.articulo.Pedido;
import ar.nex.app.MainApp;
import ar.nex.jpa.CompraJpaController;
import ar.nex.jpa.PedidoJpaController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class CompraController implements Initializable {

    @FXML
    private Button btnMenu;
    @FXML
    private TextField boxBuscar;
    @FXML
    private Button btnMas;
    @FXML
    private Button btnMenos;
    @FXML
    private Label lblStockSelect;
    @FXML
    private Button btnEditar;
    @FXML
    private Label lblHistoriaSelect;

    ObservableList<Compra> dataCompra = FXCollections.observableArrayList();
    @FXML
    private TableView<Compra> tableCompra;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colProveedor;
    @FXML
    private TableColumn<?, ?> colTotal;
    @FXML
    private TableColumn<?, ?> colEstado;

    ObservableList<Pedido> dataPedido = FXCollections.observableArrayList();
    @FXML
    private TableView<Pedido> tablePedido;
    @FXML
    private TableColumn<?, ?> colPArticulo;
    @FXML
    private TableColumn<?, ?> colPCantidad;
    @FXML
    private TableColumn<?, ?> colPPrecio;
    @FXML
    private TableColumn<?, ?> colPEstado;

    private static CompraController instance;

    public static CompraController getInstance() {
        return instance;
    }

    private CompraJpaController jpaCompra;
    private PedidoJpaController jpaPedido;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        CompraController.instance = this;

        btnMenu.setOnAction(e -> MainApp.showMe(102));
        btnMas.setOnAction(e -> crearPedido());

        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        colPArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        colPCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colPEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        InitService();
        loadCompra();
    }

    private void InitService() {
        System.out.println("ar.nex.articulo.ArticuloController.InitService()");
        try {
            jpaCompra = new CompraJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
            jpaPedido = new PedidoJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Search(InputMethodEvent event
    ) {
    }

    @FXML
    private void Search(KeyEvent event
    ) {
    }

    @FXML
    private void onTableCompra(MouseEvent event
    ) {
        try {
            Compra item = (Compra) tableCompra.getSelectionModel().getSelectedItem();
            loadPedido(item);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    private void onTablePedido(MouseEvent event
    ) {
    }

    public void crearPedido() {
        System.out.println("ar.nex.compra.CompraController.crearPedido()");
        try {
            Stage dialog = new Stage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/compra/PedidoCompra.fxml")));
            dialog.setTitle("Nuevo Pedido");
            dialog.setScene(scene);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.resizableProperty().setValue(Boolean.FALSE);
            dialog.showAndWait();

        } catch (Exception e) {
            System.err.print(e);
            e.printStackTrace();
        }
    }

    public void loadCompra() {
        System.out.println("ar.nex.compra.CompraController.loadCompra()");
        try {
            this.dataCompra.clear();
            List<Compra> lst = jpaCompra.findCompraEntities();
            for (Compra item : lst) {
                this.dataCompra.add(item);
            }
            this.tableCompra.setItems(dataCompra);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

    private void loadPedido(Compra compra) {
        System.out.println("ar.nex.compra.CompraController.loadPedido()");
        try {
            this.dataPedido.clear();
            //List<Pedido> lst = jpaCompra.findCompraEntities();
            for (Pedido item : compra.getPedidoList()) {
                this.dataPedido.add(item);
            }
            this.tablePedido.setItems(dataPedido);
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
        }
    }

}
