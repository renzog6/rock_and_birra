package ar.nex.articulo;

import ar.nex.jpa.ArticuloJpaController;
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

    public StockEditar(Articulo articulo, boolean suma) {
        this.articulo = articulo;
        this.suma = suma;
    }

    Articulo articulo;
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

        lblArticulo.setText((suma ? "SUMAR a : " : "RESTAR a : ") + articulo.getNombre());

        btnCancelar.setOnAction(e -> ((Node) (e.getSource())).getScene().getWindow().hide());
        btnGuardar.setOnAction(e -> guardar(e));

        boxFecha.setValue(LocalDate.now());
    }

    @FXML
    private void guardar(ActionEvent event) {
        System.out.println("ar.nex.articulo.StockEditar.guardar()");
        try {
            Integer c = suma ? Integer.parseInt(boxCantidad.getText()) : ((-1) * Integer.parseInt(boxCantidad.getText()));

            articulo.setStock(articulo.getStock() + c);

            ArticuloJpaController jpaArticulo = new ArticuloJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
            jpaArticulo.edit(articulo);

            new StockDetalleController().editarStockDetalle(articulo, boxFecha.getValue().toString(), boxDetalle.getText(), c);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ((Node) (event.getSource())).getScene().getWindow().hide();
        }

    }
    
        public void updateStock(String fecha, String msg, int cantidad) {        
        try {
            Integer c = suma ? cantidad : ((-1) * cantidad);

            articulo.setStock(articulo.getStock() + c);

            ArticuloJpaController jpaArticulo = new ArticuloJpaController(Persistence.createEntityManagerFactory("SysControl-PU"));
            jpaArticulo.edit(articulo);

            new StockDetalleController().editarStockDetalle(articulo, fecha, msg, c);

        } catch (Exception e) {
            e.printStackTrace();
        } 

    }

}
