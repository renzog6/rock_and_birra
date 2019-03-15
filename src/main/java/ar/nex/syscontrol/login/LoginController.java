package ar.nex.syscontrol.login;

import java.io.IOException;

import ar.nex.app.MainApp;
import ar.nex.syscontrol.config.HistorialService;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController implements Initializable {

    private static Usuario userLogin;

    private HistorialService historial;

    @FXML
    Button signInButton;

    @FXML
    TextField usernameBox;

    @FXML
    PasswordField passwordBox;

    @FXML
    Text errorReport;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clearAll();
        historial = new HistorialService();
        usernameBox.setText("admin");
        passwordBox.setText("admin");
    }

    public void clearAll() {
        userLogin = null;
        usernameBox.clear();
        passwordBox.clear();
    }

    @FXML
    void goSignIn(ActionEvent event) throws IOException {
        System.out.println("ar.nex.syscontrol.login.LoginController.goSignIn()");
        try {
            Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            System.out.println("boxU: " + usernameBox.getText() + " boxP: " + passwordBox.getText());
            boolean isLogin = false;

            UsuarioService service = new UsuarioService();
            List<Usuario> lst = service.Get().findUsuarioEntities();
            int u = 0;
            while (u < lst.size()) {
                Usuario l = lst.get(u);
                if ((l.getUser().equals(usernameBox.getText())) && (l.getPass().equals(passwordBox.getText()))) {
                    isLogin = true;
                    setUserLogin(l);
                }
                u++;
            }

            if (isLogin) {
                historial.GuardarEvento("Ingreso al sistema el usuario :" + userLogin.getName());
                MainApp.showMe(102);
            } else {
                historial.GuardarEvento("Error al ingreso!!! user:" + usernameBox.getText() + " pass:" + passwordBox.getText());
                errorReport.setText("Error! Invalid Username or Password.");
                clearAll();
            }
        } catch (Exception e) {
            errorReport.setText("Error! Invalid Username or Password.");
            e.printStackTrace();
        }
    }

    public static Usuario getUserLogin() {
        if (userLogin == null) {
            userLogin = new Usuario("Desconocido");
        }
        return userLogin;
    }

    public static void setUserLogin(Usuario userLogin) {
        LoginController.userLogin = userLogin;
    }

    public static boolean isAdmin() {
        if (userLogin.getUser().contains("admin")) {
            return true;
        }else{
            return false;
        }        
    }
}
