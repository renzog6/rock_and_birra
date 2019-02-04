package ar.nex.syscontrol.caja;

import ar.nex.syscontrol.cliente.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ar.nex.syscontrol.MainApp;
import ar.nex.syscontrol.config.Historial;
import ar.nex.syscontrol.config.HistorialService;
import ar.nex.syscontrol.partido.Partido;
import ar.nex.syscontrol.partido.PartidoJpaController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;

public class CajaMovController implements Initializable {

    private CajaMovService srvCajaMov;
    private HistorialService srvHistorial;

    @FXML
    private TextField searchBox;
    @FXML
    private Button goMenu;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnAdd;

    ObservableList<CajaMov> data = FXCollections.observableArrayList();
    FilteredList<CajaMov> filteredData = new FilteredList<>(data);

    @FXML
    private TableView<CajaMov> table;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colMov;
    @FXML
    private TableColumn<?, ?> colImporte;
    @FXML
    private TableColumn<?, ?> colSaldo;
    @FXML
    private TableColumn<?, ?> colComentario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        srvCajaMov = new CajaMovService();
        srvHistorial = new HistorialService();
        initTabla();
        loadTabla();
    }

    public void initTabla() {
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colMov.setCellValueFactory(new PropertyValueFactory<>("mov"));
        colImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
    }

    public void loadTabla() {
        data.clear();
        List<CajaMov> lst = srvCajaMov.Get().findCajaMovEntities();
        for (CajaMov item : lst) {
            data.add(item);
            table.setItems(data);
        }
    }

    @FXML
    private void Delete(ActionEvent event) {
    }

    @FXML
    private void Update(ActionEvent event) {
    }

    @FXML
    private void AddMov(ActionEvent event) {
        System.out.println("ar.nex.syscontrol.caja.CajaMovController.AddMov()");
        try {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Nuevo Ingreso a Caja.");
            alert.initStyle(StageStyle.UTILITY);
            //alert.setHeaderText("Agregar Nuevo Articulo a el Cliente: " + selectCliente.getNombre());

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
            grid.add(fecha, 1, 0);

            grid.add(new Label("Tipo: "), 0, 1);
            ObservableList<String> options
                    = FXCollections.observableArrayList(
                            "Pago",
                            "Cobro"
                    );
            ComboBox comboBox = new ComboBox(options);
            comboBox.setPrefWidth(250);
            comboBox.setValue("Pago");
            grid.add(comboBox, 1, 1);

            grid.add(new Label("Detalle: "), 0, 2);
            TextField detalle = new TextField();
            detalle.setAlignment(Pos.CENTER);
            detalle.setPrefWidth(250);
            grid.add(detalle, 1, 2);

            grid.add(new Label("Importe: "), 0, 3);
            TextField importe = new TextField();
            importe.setAlignment(Pos.CENTER);
            grid.add(importe, 1, 3);

            alert.getDialogPane().setContent(grid);
            Platform.runLater(() -> comboBox.requestFocus());

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                srvCajaMov.addMov(fecha.getText(),
                        comboBox.getSelectionModel().getSelectedItem().toString(),
                        detalle.getText(),
                        Double.valueOf(importe.getText()));
            } else {
                // ... user chose CANCEL or closed the dialog
            }
            loadTabla();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void Search(InputMethodEvent event) {
    }

    @FXML
    private void Search(KeyEvent event) {
    }

    @FXML
    private void showOnClick(MouseEvent event) {
    }

    @FXML
    private void goMenu(ActionEvent event) throws IOException {
        MainApp.showMainMenu();
    }
}
