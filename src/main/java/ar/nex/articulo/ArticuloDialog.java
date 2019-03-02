package ar.nex.articulo;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Renzo
 */
public class ArticuloDialog {

    public int add() {
        System.out.println("ar.nex.articulo.ArticuloDialog.add()");
        try {
            Stage dialog = new Stage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/ArticuloDialog.fxml")));

            dialog.setScene(scene);
           //dialog.initOwner(MainApp.stage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            //dialog.initStyle(StageStyle.UNDECORATED);
            dialog.resizableProperty().setValue(Boolean.FALSE);
            dialog.showAndWait();

        } catch (Exception e) {
            System.err.print(e);
            e.printStackTrace();
        }

        return 1;
    }

    public void edit() {
        System.out.println("ar.nex.articulo.ArticuloDialog.edit()");
        try {
            Stage dialog = new Stage();
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/fxml/ArticuloDialog.fxml")));

            dialog.setScene(scene);
           // dialog.initOwner(MainApp.stage);
            dialog.initModality(Modality.APPLICATION_MODAL);
            //dialog.initStyle(StageStyle.UNDECORATED);
            dialog.resizableProperty().setValue(Boolean.FALSE);
            
            dialog.showAndWait();

        } catch (IOException e) {
            System.err.print(e);
        }
    }

}
