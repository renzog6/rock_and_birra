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
    Button btnArticulo;
    @FXML
    Button btnStock;
    @FXML
    Button btnProveedor;
    @FXML
    Button btnClientes;
    @FXML
    Button btnCaja;
    @FXML
    Button btnConfig;
    @FXML
    Button btnHistorial;
    @FXML
    Label lblUser;

    @FXML
    public void goClientes() throws IOException {
        System.out.println("ar.nex.syscontrol.MainMenuController.goClientes()");
        try {
            boolean isLogin = true;
            if (isLogin) {
                MainApp.showClientes();
            } else {
                System.out.println("ar.nex.syscontrol.MainMenuController.goSignIn(): ERROR");
            }
        } catch (Exception e) {
            System.out.println("ar.nex.syscontrol.MainMenuController.goSignIn(): ERROR");
            e.printStackTrace();
        }
    }

    @FXML
    public void goConfig() throws IOException {
        System.out.println("ar.nex.syscontrol.MainMenuController.goConfig()");
        try {
            if (LoginController.isAdmin()) {
                MainApp.showConfig();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error!!!");
                alert.setContentText("Solo en Administrador puede ingresar!!!");
                alert.showAndWait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goCaja() throws IOException {
        try {
            System.out.println("ar.nex.syscontrol.MainMenuController.goConfig()");
            MainApp.showCaja();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        btnArticulo.setOnAction(e -> MainApp.showMe(2));
        btnStock.setOnAction(e -> MainApp.showMe(3));
        btnProveedor.setOnAction(e -> MainApp.showMe(4));
        btnHistorial.setOnAction(e -> MainApp.showMe(11));
        lblUser.setText("Usuario [ " + LoginController.getUserLogin().getName() + " ]");
    }

}
