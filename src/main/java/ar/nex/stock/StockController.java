package ar.nex.stock;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author Renzo
 */
public class StockController implements Initializable {

    @FXML
    private Button signOut;
    
    
    @FXML
    private TableView<?> tableStock;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colArticulo;
    @FXML
    private TableColumn<?, ?> colCantidad;
    @FXML
    private TableView<?> tableHistoria;
    @FXML
    private MenuItem update;
    @FXML
    private TableColumn<?, ?> colHFecha;
    @FXML
    private TableColumn<?, ?> colHEvento;
    @FXML
    private TableColumn<?, ?> colHCantidad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void goSignOut(ActionEvent event) {
    }

    @FXML
    private void Update(ActionEvent event) {
    }
    
}
