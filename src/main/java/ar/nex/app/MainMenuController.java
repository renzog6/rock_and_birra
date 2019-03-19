package ar.nex.app;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class MainMenuController implements Initializable {

    @FXML
    Button btnCaja;
    @FXML
    Button btnArticulo;
    @FXML
    Button btnStock;
    @FXML
    Button btnProveedor;
    @FXML
    Button btnCliente;
    @FXML
    Button btnCompra;
    @FXML
    Button btnVenta;
    @FXML
    Button btnHistorial;
    @FXML
    Label lblUser;

       /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnArticulo.setOnAction(e -> MainApp.showMe(2));
        btnStock.setOnAction(e -> MainApp.showMe(3));
        
        btnProveedor.setOnAction(e -> MainApp.showMe(4));
        btnCompra.setOnAction(e -> MainApp.showMe(5));
        
        btnCliente.setOnAction(e-> MainApp.showMe(6));
        btnVenta.setOnAction(e-> MainApp.showMe(7));
        
        btnHistorial.setOnAction(e -> MainApp.showMe(11));
        
//        lblUser.setText("Usuario [ " + LoginController.getUserLogin().getName() + " ]");
    }

}
