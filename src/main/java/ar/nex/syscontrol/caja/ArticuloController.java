package ar.nex.syscontrol.caja;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ar.nex.syscontrol.MainApp;
import ar.nex.syscontrol.config.HistorialService;
import ar.nex.syscontrol.caja.CajaMovTipo;
import ar.nex.syscontrol.caja.CajaMovTipoJpaController;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ArticuloController implements Initializable {

    HistorialService historial = new HistorialService();
    
    ObservableList<CajaMovTipo> data = FXCollections.observableArrayList();
    FilteredList<CajaMovTipo> filteredData = new FilteredList<>(data);

    @FXML
    private Button signOut;

    @FXML
    TableView<CajaMovTipo> table;

    @FXML
    private TableColumn<?, ?> colArticulo;

    @FXML
    private TableColumn<?, ?> colImporte;

    @FXML
    private TableColumn<?, ?> colComentario;

    @FXML
    Button addnewBtn;

    @FXML
    Button updateBtn;

    @FXML
    Button deleteBtn;

    @FXML
    TextField searchBox;

    @FXML
    TextField boxNombre;

    @FXML
    TextField boxImporte;

    @FXML
    TextField boxComentario;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        colArticulo.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colImporte.setCellValueFactory(new PropertyValueFactory<>("importe"));
        colComentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        loadDatabaseData();
    }

    private EntityManagerFactory factory;// = Persistence.createEntityManagerFactory("SysServices-PU");
    private CajaMovTipoJpaController dao;// = new CajaMovTipoJpaController(factory);

    public void CajaMovTipoService() {
        System.out.println("ar.sys.usuario.CajaMovTipoService.<init>()");
        factory = Persistence.createEntityManagerFactory("SysControl-PU");
        dao = new CajaMovTipoJpaController(factory);
    }

    public void loadDatabaseData() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.loadDatabaseData()");
        try {
            boxNombre.clear();
            boxImporte.clear();
            boxComentario.clear();
            data.clear();

            CajaMovTipoService();
            List<CajaMovTipo> lst = dao.findCajaMovTipoEntities();
            for (CajaMovTipo usuario : lst) {
                data.add(usuario);
                table.setItems(data);
            }

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @FXML
    public void Add() throws Exception {
        System.out.println("ar.nex.syscontrol.config.ConfigController.Add()");

        String nombre = boxNombre.getText();
        String importe = boxImporte.getText();
        String comentario = boxComentario.getText();

        try {
            CajaMovTipo u = new CajaMovTipo();
            u.setNombre(nombre);
            u.setImporte(Double.parseDouble( importe.replace(",",".") ));
            u.setComentario(comentario);
            dao.create(u);
        } catch (Exception e) {
            System.out.println(e);
        }
        loadDatabaseData();
        MainApp.showInformationAlertBox("Nuevo Aticulo: " + nombre + " Added Successfully!");        
        historial.GuardarEvento("Nuevo > Articulo: " + nombre+" a $"+importe);
    }

    static CajaMovTipo usuarioSelect;

    @FXML
    public void showOnClick() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.showOnClick()");
        try {
            CajaMovTipo user = (CajaMovTipo) table.getSelectionModel().getSelectedItem();
            usuarioSelect = dao.findCajaMovTipo(user.getId());

            boxNombre.setText(user.getNombre());
            boxImporte.setText(user.getImporte().toString());
            boxComentario.setText(user.getComentario());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void Delete() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.Delete()");
        try {
            if (confirmDialog()) {
                dao.destroy(usuarioSelect.getId());
                MainApp.showInformationAlertBox("CajaMovTipo '" + usuarioSelect.getNombre()+ "' Deleted Successfully!");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        loadDatabaseData();
    }

    @FXML
    public void Update() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.Update()");
        try {
            
            UpdatePendiente(usuarioSelect);
            
            usuarioSelect.setNombre(boxNombre.getText());
            usuarioSelect.setImporte(Double.parseDouble( boxImporte.getText().replace(",",".") ));
            usuarioSelect.setComentario(boxComentario.getText());
            dao.edit(usuarioSelect);
            MainApp.showInformationAlertBox("CajaMovTipo '" + boxNombre.getText() + "' Updated Successfully!");
            
            UpdatePendiente(usuarioSelect);
            historial.GuardarEvento("El Articulo "+ usuarioSelect.toString()+" fue Actualizado.");
            loadDatabaseData();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public void UpdatePendiente(CajaMovTipo mv){
        System.out.println("ar.nex.syscontrol.caja.ArticuloController.UpdatePendiente()");
        CajaMovClienteService movService = new CajaMovClienteService();
        movService.UpdateMovCLiente(mv);
    }

    @FXML
    public void Search() {
		searchBox.textProperty().addListener((observableValue,oldValue,newValue)->{
			filteredData.setPredicate((Predicate<? super CajaMovTipo>)user->{
				if(newValue==null||newValue.isEmpty()){
					return true;
				}
				String lowerCaseFilter=newValue.toLowerCase();
				if(user.getNombre().toLowerCase().contains(lowerCaseFilter)){
					return true;
				}
				else if(user.getNombre().toLowerCase().contains(lowerCaseFilter)){
					return true;
				}
				return false;
			});
		});
		SortedList<CajaMovTipo> sortedData=new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
    }

    public boolean confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirma que desea ELIMINAR el articulo: " + usuarioSelect.getNombre());
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        } else {
            return false;
        }
    }

    @FXML
    void goSignOut() throws IOException {
        MainApp.showMainMenu();
    }

}
