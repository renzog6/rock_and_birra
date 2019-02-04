package ar.nex.syscontrol.caja;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ArticuloAddClienteApp extends Application {

    public static Stage stage;
    private static BorderPane mainLayout;

    @Override
    public void start(Stage stage) throws Exception {
        ArticuloAddClienteApp.stage = stage;
        ArticuloAddClienteApp.stage.setTitle("SysControl");        
        ArticuloAddClienteApp.showMain();
    }

    public static void showMain() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(ArticuloAddClienteApp.class.getResource("/fxml/caja/ArticuloAddCliente.fxml"));
        mainLayout = loader.load();

        Scene scene = new Scene(mainLayout);     
        stage.setScene(scene);
        stage.show();
    }

   
    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
