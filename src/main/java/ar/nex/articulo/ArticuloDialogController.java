package ar.nex.articulo;

import ar.nex.app.MainApp;
import ar.nex.syscontrol.config.Historial;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javax.persistence.Persistence;

/**
 *
 * @author Renzo
 */
public class ArticuloDialogController implements Initializable {

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnCancelar;

    @FXML
    private TextField boxCodigo;
    @FXML
    private TextField boxNombre;
    @FXML
    private TextField boxImporte;
    @FXML
    private TextField boxStock;
    @FXML
    private TextField boxObservacion;

   // private ArticuloController articuloController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      //  articuloController = new ArticuloController();
        //articuloController.InitService();
    }

    @FXML
    public void guardar(ActionEvent event) {
        System.out.println("ar.nex.articulo.ArticuloDialogController.guardar()");
        try {
            Articulo u = new Articulo();

            u.setCodigo(boxCodigo.getText());
            u.setNombre(boxNombre.getText());
            u.setImporte(Double.parseDouble(boxImporte.getText().replace(",", ".")));
            u.setStock(Integer.valueOf(boxStock.getText()));
            u.setObservacion(boxObservacion.getText());

            ArticuloController.getInstance().getService().create(u);
        } catch (Exception e) {
            System.out.println(e);
        }

        MainApp.showInformationAlertBox("Nuevo Aticulo: " + "VAR ACA" + " Added Successfully!");
        ArticuloController.getInstance().loadDatabaseData();
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

    @FXML
    private void goBack(ActionEvent event) {
        System.out.println("ar.nex.articulo.ArticuloDialogController.goBack()");
        ((Node) (event.getSource())).getScene().getWindow().hide();
    }

}
