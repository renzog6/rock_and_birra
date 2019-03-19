package ar.nex.app;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static Stage stage;
    private static BorderPane mainLayout;

    @Override
    public void start(Stage stage) throws Exception {

        MainApp.stage = stage;
        MainApp.stage.setTitle("Rock & Birra");
        MainApp.stage.setMaximized(true);

        MainApp.showMain();
        showMe(102);
    }

    public static void showMe(int i) {
        try {
            FXMLLoader loader = new FXMLLoader();

            switch (i) {
                case 1:
                    loader.setLocation(MainApp.class.getResource("/fxml/Login.fxml"));
                    break;
                case 2:
                    loader.setLocation(MainApp.class.getResource("/fxml/articulo/Articulo.fxml"));
                    break;
                case 3:
                    loader.setLocation(MainApp.class.getResource("/fxml/articulo/Stock.fxml"));
                    break;
                case 4:
                    loader.setLocation(MainApp.class.getResource("/fxml/compra/Proveedor.fxml"));
                    break;
                case 5:
                    loader.setLocation(MainApp.class.getResource("/fxml/compra/Compra.fxml"));
                    break;
                case 6:
                    loader.setLocation(MainApp.class.getResource("/fxml/venta/Cliente.fxml"));
                    break;
                case 7:
                    loader.setLocation(MainApp.class.getResource("/fxml/venta/Venta.fxml"));
                    break;
                case 11:
                    loader.setLocation(MainApp.class.getResource("/fxml/config/HistorialDetalle.fxml"));
                    break;
//                case 100:
//                    loader.setLocation(MainApp.class.getResource("/fxml/MainView.fxml"));
//                    break;
                case 101:
                    loader.setLocation(MainApp.class.getResource("/fxml/Login.fxml"));
                    break;
                case 102:
                    loader.setLocation(MainApp.class.getResource("/fxml/MainMenu.fxml"));
                    mainLayout.getStylesheets().add("/styles/MainMenu.css");
                default:
                    break;
            }
            //BorderPane mainItems = loader.load();
            mainLayout.setCenter(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showMain() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainApp.class.getResource("/fxml/MainView.fxml"));
        mainLayout = loader.load();

        Scene scene = new Scene(mainLayout);
        // scene.getStylesheets().add("/styles/Styles.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void showInformationAlertBox(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialoge");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
