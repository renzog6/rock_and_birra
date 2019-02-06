package ar.nex.syscontrol.config;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ar.nex.app.MainApp;
import ar.nex.syscontrol.login.Usuario;
import ar.nex.syscontrol.login.UsuarioJpaController;
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

public class ConfigController implements Initializable {

    HistorialService historial = new HistorialService();
    
    ObservableList<Usuario> data = FXCollections.observableArrayList();
    FilteredList<Usuario> filteredData = new FilteredList<>(data,e->true);

    @FXML
    private Button signOut;

    @FXML
    TableView<Usuario> table;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> usernameCol;

    @FXML
    private TableColumn<?, ?> passwordCol;

    @FXML
    Button addnewBtn;

    @FXML
    Button updateBtn;

    @FXML
    Button deleteBtn;

    @FXML
    TextField searchBox;

    @FXML
    TextField usernameBox;

    @FXML
    TextField nameBox;

    @FXML
    TextField passwordBox;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("user"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("pass"));
        loadDatabaseData();
    }

    private EntityManagerFactory factory;// = Persistence.createEntityManagerFactory("SysServices-PU");
    private UsuarioJpaController dao;// = new UsuarioJpaController(factory);

    public void UsuarioService() {
        System.out.println("ar.sys.usuario.UsuarioService.<init>()");
        factory = Persistence.createEntityManagerFactory("SysControl-PU");
        dao = new UsuarioJpaController(factory);
    }

    public void loadDatabaseData() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.loadDatabaseData()");
        try {
            nameBox.clear();
            usernameBox.clear();
            passwordBox.clear();
            data.clear();

            UsuarioService();
            List<Usuario> lst = dao.findUsuarioEntities();
            for (Usuario usuario : lst) {
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

        String name = nameBox.getText();
        String user = usernameBox.getText();
        String pass = passwordBox.getText();

        try {
            Usuario u = new Usuario();
            u.setName(name);
            u.setUser(user);
            u.setPass(pass);
            dao.create(u);
        } catch (Exception e) {
            System.out.println(e);
        }
        loadDatabaseData();
        MainApp.showInformationAlertBox("New Usuario '" + name + "' Added Successfully!");        
        historial.GuardarEvento("New Usuario " + name);
    }

    static Usuario usuarioSelect;

    @FXML
    public void showOnClick() {
        System.out.println("ar.nex.syscontrol.config.ConfigController.showOnClick()");
        try {
            Usuario user = (Usuario) table.getSelectionModel().getSelectedItem();
            usuarioSelect = dao.findUsuario(user.getId());

            nameBox.setText(user.getName());
            usernameBox.setText(user.getUser());
            passwordBox.setText(user.getPass());
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
                MainApp.showInformationAlertBox("Usuario '" + usuarioSelect.getName() + "' Deleted Successfully!");
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
            usuarioSelect.setName(nameBox.getText());
            usuarioSelect.setUser(usernameBox.getText());
            usuarioSelect.setPass(passwordBox.getText());
            dao.edit(usuarioSelect);
            MainApp.showInformationAlertBox("Usuario '" + nameBox.getText() + "' Updated Successfully!");
            loadDatabaseData();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void Search() {
		searchBox.textProperty().addListener((observableValue,oldValue,newValue)->{
			filteredData.setPredicate((Predicate<? super Usuario>)user->{
				if(newValue==null||newValue.isEmpty()){
					return true;
				}
				String lowerCaseFilter=newValue.toLowerCase();
				if(user.getName().toLowerCase().contains(lowerCaseFilter)){
					return true;
				}
				else if(user.getName().toLowerCase().contains(lowerCaseFilter)){
					return true;
				}
				return false;
			});
		});
		SortedList<Usuario> sortedData=new SortedList<>(filteredData);
		sortedData.comparatorProperty().bind(table.comparatorProperty());
		table.setItems(sortedData);
    }

    public boolean confirmDialog() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Confirma que desea ELIMINAR al usuario: " + usuarioSelect.getName());
        
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
