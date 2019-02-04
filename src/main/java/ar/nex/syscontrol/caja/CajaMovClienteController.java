package ar.nex.syscontrol.caja;

import ar.nex.syscontrol.cliente.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ar.nex.syscontrol.MainApp;
import ar.nex.syscontrol.config.HistorialService;
import ar.nex.syscontrol.utils.DialogController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class CajaMovClienteController implements Initializable {

    @FXML
    TableView<CajaMovCliente> tblArticulo;
    ObservableList<CajaMovCliente> dataArticulo = FXCollections.observableArrayList();
    FilteredList<CajaMovCliente> filteredArticulo = new FilteredList<>(dataArticulo);
    private CajaMovCliente selectArticulo;

    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colArticulo;
    @FXML
    private TableColumn<?, ?> colImporte;
    @FXML
    private TableColumn<?, ?> colComentario;

    @FXML
    TableView<Cliente> tblCliente;
    ObservableList<Cliente> dataCliente = FXCollections.observableArrayList();
    FilteredList<Cliente> filteredCliente = new FilteredList<>(dataCliente);
    private static Cliente selectCliente;
    @FXML
    private TableColumn<?, ?> colCliente;
    @FXML
    private TableColumn<?, ?> colSaldo;

    @FXML
    TextField searchBox;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        //Tabla Articulo
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fechaCompra"));
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));
        colImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        //Tabla Cliente
        colCliente.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        InitService();
        loadDataCliente();
    }

    private EntityManagerFactory factory;
    private CajaMovClienteJpaController jpaArticulo;
    private ClienteJpaController jpaCliente;
    private CajaMovTipoJpaController jpaMovTipo;
    private CajaMovTipo articulo;
    private CajaMovService srvCaja;
    private HistorialService srvHistorial;

    public void InitService() {
        System.out.println("ar.sys.Cliente.ClienteService.<init>()");
        factory = Persistence.createEntityManagerFactory("SysControl-PU");
        jpaArticulo = new CajaMovClienteJpaController(factory);
        jpaCliente = new ClienteJpaController(factory);
        jpaMovTipo = new CajaMovTipoJpaController(factory);
        srvCaja = new CajaMovService();
        srvHistorial = new HistorialService();
    }

    public void loadDataArticulo() {
        System.out.println("ar.nex.syscontrol.partido.ArticuloController.loadDataArticulo()");
        try {
            dataArticulo.clear();
            List<CajaMovCliente> lst = jpaArticulo.findCajaMovClienteEntities();
            for (CajaMovCliente p : lst) {
                dataArticulo.add(p);
                tblArticulo.setItems(dataArticulo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDataCliente() {
        System.out.println("ar.nex.syscontrol.partido.ArticuloController.loadDataCliente()");
        try {
            dataCliente.clear();
            List<Cliente> lst = jpaCliente.findClienteEntities();
            for (Cliente p : lst) {
                dataCliente.add(p);
                tblCliente.setItems(dataCliente);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showOnClick() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.showOnClick()");
        try {
            Cliente cliente = (Cliente) tblCliente.getSelectionModel().getSelectedItem();
            selectCliente = jpaCliente.findCliente(cliente.getId());
            System.out.println("showOnClick(): " + selectCliente.toString());

            dataArticulo.clear();
            List<CajaMovCliente> lst = jpaArticulo.findCajaMovPendiente(selectCliente.getId());
            dataArticulo.addAll(lst);
            tblArticulo.setItems(dataArticulo);

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void clickOnTableArticulo() {
        System.out.println("ar.nex.syscontrol.caja.CajaMovClienteController.clickOnTableArticulo()");
        try {
            CajaMovCliente articulo = (CajaMovCliente) tblArticulo.getSelectionModel().getSelectedItem();
            selectArticulo = jpaArticulo.findCajaMovCliente(articulo.getId());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void ClienteCobrarDialog() throws Exception {
        System.out.println("ar.nex.syscontrol.caja.CajaMovClienteController.ClienteCobrarDialog()");
        if (selectCliente == null) {
            DialogController.showError("Seleccione un Cliente primero, PAVO!!!");
        } else {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Cobro");
            alert.setHeaderText("Nuevo Pago del Cliente: " + selectCliente.getNombre());

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            grid.add(new Label("Fecha: "), 0, 0);
            TextField fecha = new TextField();
            Date d = new Date();
            DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
            fecha.setText(fd.format(d));
            fecha.setAlignment(Pos.CENTER);
            grid.add(fecha, 1, 0);

            grid.add(new Label("Importe: "), 0, 1);
            TextField importe = new TextField();
            importe.setAlignment(Pos.CENTER);
            grid.add(importe, 1, 1);

            alert.getDialogPane().setContent(grid);
            Platform.runLater(() -> importe.requestFocus());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                double pago = Double.valueOf(importe.getText());
                this.ClienteCobrar(pago, fecha.getText());
            }

        }
    }

    public void ClienteCobrar(double pago, String fecha) throws Exception {
        System.out.println("ar.nex.syscontrol.caja.CajaMovClienteController.ClienteCobrar()");
        try {
            boolean cobrar = true;
            double pagoCaja = pago;

            if (pago <= 0) {
                cobrar = false;
            }
            if (cobrar) {
                double clienteSaldo = selectCliente.getSaldo();

                for (CajaMovCliente data : dataArticulo) {

                    if (data.getImporte() <= pago) {
                        pago = pago - data.getImporte();
                        clienteSaldo = clienteSaldo - data.getImporte();
                        data.setEstado(1);
                        data.setFechaPago(fecha);
                        jpaArticulo.edit(data);
                    } else {
                        if (pago != 0) {
                            CajaMovCliente cmc = new CajaMovCliente();
                            cmc.setFechaCompra(data.getFechaCompra());
                            cmc.setArticulo(data.getArticulo());
                            cmc.setImporte(data.getImporte());
                            cmc.setEstado(1);
                            cmc.setFechaPago(fecha);
                            cmc.setComentario("Saldo pendiente $" + (data.getImporte() - pago));
                            jpaArticulo.create(cmc);

                            data.setArticulo("Saldo pendiente de " + data.toString());
                            data.setImporte(data.getImporte() - pago);
                            jpaArticulo.edit(data);

                            clienteSaldo = clienteSaldo - pago;
                            pago = 0;
                        }
                    }

                }
                selectCliente.setSaldo(clienteSaldo);
                selectCliente.setFecha(fecha);
                jpaCliente.edit(selectCliente);

                srvCaja.cobroCliente(selectCliente.getNombre(), fecha, pagoCaja);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            this.showOnClick();
            this.loadDataCliente();
        }
    }

    /**
     *
     */
    @FXML
    public void ArticuloAdd() throws Exception {
        System.out.println("ar.nex.syscontrol.caja.CajaMovClienteController.ArticuloAdd()");
        if (selectCliente == null) {
            DialogController.showError("Seleccione un Cliente primero, PAVO!!!");
        } else {

            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Nuevo Articulo");
            alert.setHeaderText("Agregar Nuevo Articulo a el Cliente: " + selectCliente.getNombre());

            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 20, 10, 10));

            grid.add(new Label("Fecha: "), 0, 0);
            TextField fecha = new TextField();
            Date d = new Date();
            DateFormat fd = new SimpleDateFormat("dd/MM/yyyy");
            fecha.setText(fd.format(d));
            fecha.setAlignment(Pos.CENTER);
            fecha.setPrefWidth(200);
            grid.add(fecha, 1, 0);

            grid.add(new Label("Articulo: "), 0, 1);
            TextField lstArticulo = new TextField();
            lstArticulo.setAlignment(Pos.CENTER);
            grid.add(lstArticulo, 1, 1);

            CajaMovTipo art = new CajaMovTipo();
            List<CajaMovTipo> lst = jpaMovTipo.findCajaMovTipoEntities();

            TextFields.bindAutoCompletion(lstArticulo, lst).setOnAutoCompleted(new EventHandler<AutoCompletionBinding.AutoCompletionEvent<CajaMovTipo>>() {
                @Override
                public void handle(AutoCompletionBinding.AutoCompletionEvent<CajaMovTipo> event) {
                    System.out.println(".handle()" + event.getCompletion().getNombre());
                    articulo = event.getCompletion();
                }
            });

            alert.getDialogPane().setContent(grid);
            Platform.runLater(() -> lstArticulo.requestFocus());
            //lstArticulo.requestFocus();

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {

                CajaMovCliente m = new CajaMovCliente();
                m.setClienteId(selectCliente.getId());
                m.setFechaCompra(fecha.getText());
                m.setArticulo(articulo.getNombre());
                m.setImporte(articulo.getImporte());
                m.setEstado(0);
                jpaArticulo.create(m);

                if (selectCliente.getSaldo() != null) {
                    selectCliente.setSaldo(selectCliente.getSaldo() + m.getImporte());
                } else {
                    selectCliente.setSaldo(m.getImporte());
                }
                jpaCliente.edit(selectCliente);

                this.showOnClick();
                this.loadDataCliente();

            } else {
                // ... user chose CANCEL or closed the dialog
            }
        }
    }

    @FXML
    public void ArticuloDelete() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.Delete()");
        try {
            if (selectArticulo != null) {
                if (DialogController.confirmDialog("Confirma que desea ELIMINAR: " + selectArticulo.toString())) {
                    srvHistorial.GuardarEvento("Se Elimino del Cliente: " + selectCliente.getNombre() + " el Articulo: " + selectArticulo.toString());
                    jpaArticulo.destroy(selectArticulo.getId());
                    if (selectCliente.getSaldo() != null) {
                        selectCliente.setSaldo(selectCliente.getSaldo() - selectArticulo.getImporte());
                    }
                    jpaCliente.edit(selectCliente);
                }

            }
        } catch (Exception e) {
            System.out.println(e);
        }
        this.showOnClick();
        this.loadDataCliente();
    }

    @FXML
    public void CajaDetalleCliente() {
        System.out.println("ar.nex.syscontrol.caja.CajaMovClienteController.CajaDetalleCliente()");
        try {
            if (selectCliente == null) {
//                Alert alert = new Alert(AlertType.ERROR);
//                alert.setTitle("Error!!!");
//                alert.setContentText("Seleccione un Cliente primero, PAVO!!!");
//                alert.showAndWait();
                DialogController.showError("Seleccione un Cliente primero, PAVO!!!");
            } else {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/caja/CajaDetalleCliente.fxml"));
                Parent root1 = (Parent) fxmlLoader.load();
                root1.getStylesheets().add("/styles/CajaDetalleCliente.css");

                Stage stage = new Stage();
                stage.setScene(new Scene(root1));
                stage.showAndWait();
            }
        } catch (IOException ex) {
            Logger.getLogger(CajaMovClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void goSignOut() throws IOException {
        MainApp.showMainMenu();
    }

//    public boolean confirmDialog(String str) {
//        Alert alert = new Alert(AlertType.CONFIRMATION);
//        alert.setTitle("Confirmation Dialog");
//        alert.setHeaderText("Confirma que desea ELIMINAR: " + str);
//
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    @FXML
    public void goCliente(ActionEvent event) {
        System.out.println("ar.nex.syscontrol.caja.CajaMovClienteController.goCliente()");
        try {
            MainApp.showClientes();
        } catch (IOException ex) {
            Logger.getLogger(CajaMovClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void goArticulo() {
        System.out.println("ar.nex.syscontrol.caja.CajaMovClienteController.goArticulo()");
        try {
            MainApp.showArticulos();
        } catch (IOException ex) {
            Logger.getLogger(CajaMovClienteController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Search() {
        searchBox.textProperty().addListener((observableValue, oldValue, newValue) -> {
            filteredCliente.setPredicate((Predicate<? super Cliente>) user -> {
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
        SortedList<Cliente> sortedData = new SortedList<>(filteredCliente);
        sortedData.comparatorProperty().bind(tblCliente.comparatorProperty());
        tblCliente.setItems(sortedData);
    }

    public static Cliente getSelectCliente() {
        return selectCliente;
    }

    public static void setSelectCliente(Cliente selectCliente) {
        CajaMovClienteController.selectCliente = selectCliente;
    }

}
