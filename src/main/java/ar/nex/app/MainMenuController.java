package ar.nex.app;

import ar.nex.syscontrol.login.LoginController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
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
    Button btnClientes;
    @FXML
    Button btnCompra;
    @FXML
    Button btnConfig;
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
        
        btnHistorial.setOnAction(e -> MainApp.showMe(11));
        
//        lblUser.setText("Usuario [ " + LoginController.getUserLogin().getName() + " ]");
    }

}
