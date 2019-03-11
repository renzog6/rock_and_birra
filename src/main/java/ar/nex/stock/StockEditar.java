package ar.nex.stock;

import ar.nex.jpa.StockJpaController;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.persistence.Persistence;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class StockEditar implements Initializable {

    public StockEditar(Stock stock, boolean suma) {
        this.stock = stock;
        this.suma = suma;
    }

    Stock stock;
    boolean suma;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;
    @FXML
    private TextField boxDetalle;
    @FXML
    private TextField boxCantidad;
    @FXML
    private DatePicker boxFecha;
    @FXML
    private Label lblArticulo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        lblArticulo.setText((suma ? "SUMAR a : " : "RESTAR a : ") + stock.getArticulo().getNombre());

        btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
        
        boxFecha.setValue(LocalDate.now());
    }

    @FXML
    private void guardar(ActionEvent event) {
        System.out.println("ar.nex.stock.StockEditar.guardar()");
        try {
            Integer c = suma ? Integer.parseInt(boxCantidad.getText()) : ((-1) * Integer.parseInt(boxCantidad.getText()));

            stock.setCantidad(stock.getCantidad() + c);
            stock.setFecha(boxFecha.getValue().toString());

            StockJpaController jpaStock = new StockJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));            
            jpaStock.edit(stock);            
            
            new HistoriaController().editarHistoria(stock, boxFecha.getValue().toString(), boxDetalle.getText(), c);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }

    }

}
