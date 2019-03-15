package ar.nex.compra;

import ar.nex.app.MainApp;
import ar.nex.articulo.Articulo;
import ar.nex.jpa.ArticuloJpaController;
import ar.nex.jpa.CompraJpaController;
import ar.nex.jpa.PedidoJpaController;
import ar.nex.jpa.ProveedorJpaController;
import ar.nex.util.GetPK;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javax.persistence.Persistence;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class PedidoController implements Initializable {

    @FXML
    private Proveedor proveedorSelect;
    private ObservableList<Proveedor> dataPoveedor = FXCollections.observableArrayList();

    private Articulo articuloSelect;
    private ObservableList<Articulo> dataArticulo = FXCollections.observableArrayList();

    @FXML
    private DatePicker boxFecha;
    @FXML
    private TextField boxCantidad;
    @FXML
    private TextField boxPrecio;
    @FXML
    private TextField boxArticulo;
    @FXML
    private TextField boxProveedor;

    @FXML
    private Button btnAdd;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnGuardar;

    @FXML
    private Label lblTotalArticulo;
    private Integer totalArticulo;
    @FXML
    private Label lblTotal;
    private Double total;

    ObservableList<Pedido> dataPedido = FXCollections.observableArrayList();
    @FXML
    private TableView<Pedido> tablePedido;
    @FXML
    private TableColumn<?, ?> colArticulo;
    @FXML
    private TableColumn<?, ?> colCantidad;
    @FXML
    private TableColumn<?, ?> colPrecio;
    @FXML
    private TableColumn<?, ?> colTotal;
    @FXML
    private TableColumn<Pedido, Void> colAction;

    private ProveedorJpaController jpaProveedor;
    private ArticuloJpaController jpaArticulo;
    private PedidoJpaController jpaPedido;
    private CompraJpaController jpaCompra;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        colArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        //colAction.setCellValueFactory(new PropertyValueFactory<>("observacion"));

        boxFecha.setValue(LocalDate.now());

        totalArticulo = 0;
        total = 0.0;

        InitService();
        loadDataProveedor();
        loadDataArticulo();

        InitControls();

    }

    private void InitControls() {

        btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
        btnGuardar.setOnAction(e -> guardar(e));
        //btnAdd.setOnAction(e -> addArticulo(e));
        btnAdd.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    addArticulo(ke);
                }
            }
        });

        AutoCompletionBinding<Proveedor> autoProveedor = TextFields.bindAutoCompletion(boxProveedor, dataPoveedor);
        autoProveedor.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<Proveedor> event) -> {
            proveedorSelect = event.getCompletion();
        });

        AutoCompletionBinding<Articulo> autoArticulo = TextFields.bindAutoCompletion(boxArticulo, dataArticulo);
        autoArticulo.setOnAutoCompleted((AutoCompletionBinding.AutoCompletionEvent<Articulo> event) -> {
            articuloSelect = event.getCompletion();
        });

        addButtonToTable();
    }

    public void InitService() {
        System.out.println("ar.nex.compra.PedidoController.InitService()");
        try {
            this.jpaProveedor = new ProveedorJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
            this.jpaArticulo = new ArticuloJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
            this.jpaPedido = new PedidoJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
            this.jpaCompra = new CompraJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataProveedor() {
        System.out.println("ar.nex.compra.PedidoController.loadDataProveedor()");
        try {
            this.dataPoveedor.clear();
            List<Proveedor> lst = jpaProveedor.findProveedorEntities();
            for (Proveedor item : lst) {
                this.dataPoveedor.add(item);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void loadDataArticulo() {
        System.out.println("ar.nex.compra.PedidoController.loadDataArticulo()");
        try {
            this.dataArticulo.clear();
            List<Articulo> lst = jpaArticulo.findArticuloEntities();
            for (Articulo item : lst) {
                this.dataArticulo.add(item);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void loadDataPedido(Pedido p) {
        System.out.println("ar.nex.compra.PedidoController.loadDataPedido()");
        try {
            this.dataPedido.add(p);
            this.tablePedido.setItems(dataPedido);

            totalArticulo += p.getCantidad();
            lblTotalArticulo.setText("Cantidad de Articulos : " + totalArticulo);
            total += p.getTotal();
            lblTotal.setText("Toal : " + total);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addArticulo(KeyEvent event) {
        System.out.println("ar.nex.compra.PedidoController.addArticulo()");
        try {
            Pedido p = new Pedido();
            p.setArticulo(articuloSelect);
            p.setCantidad(Integer.parseInt(boxCantidad.getText()));
            p.setPrecio(Double.parseDouble(boxPrecio.getText()));
            loadDataPedido(p);

            setControls();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addButtonToTable() {
        //TableColumn<Pedido, Void> colBtn = new TableColumn("Button Column");

        Callback<TableColumn<Pedido, Void>, TableCell<Pedido, Void>> cellFactory = new Callback<TableColumn<Pedido, Void>, TableCell<Pedido, Void>>() {
            public TableCell<Pedido, Void> call(final TableColumn<Pedido, Void> param) {
                final TableCell<Pedido, Void> cell = new TableCell<Pedido, Void>() {

                    private final Button btn = new Button("-");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Pedido data = getTableView().getItems().get(getIndex());
                            tablePedido.getItems().remove(getIndex());
                            System.out.println("selectedPedido: " + data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        colAction.setCellFactory(cellFactory);

        //tablePedido.getColumns().add(colAction);
    }

    private void setControls() {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                boxArticulo.requestFocus();
            }
        });
        boxArticulo.clear();
        boxCantidad.setText("");
        boxPrecio.setText("");
    }

    private void guardar(ActionEvent event) {
        System.out.println("ar.nex.compra.PedidoController.guardar()");
        try {
            GetPK pk = new GetPK();
            Compra compra = new Compra(pk.Nuevo(Compra.class));
            compra.setFecha(boxFecha.getValue().toString());
            compra.setProveedor(proveedorSelect);
            compra.setTotal(total);
            jpaCompra.create(compra);
            for (Pedido p : dataPedido) {
                p.setId(pk.Nuevo(Pedido.class));
                jpaPedido.create(p);
                compra.addPedido(p);
            }
            jpaCompra.edit(compra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ((Node) (event.getSource())).getScene().getWindow().hide();
        CompraController.getInstance().loadCompra();
        MainApp.showInformationAlertBox("Nuevo Aticulo: " + "VAR ACA" + " Added Successfully!");
    }
}
