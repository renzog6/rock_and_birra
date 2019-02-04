package ar.nex.syscontrol.login;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import ar.nex.syscontrol.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class HomeController implements Initializable {

    Connection connection = null;//MyAppDatabaseConnection.LoginConnector();
    ObservableList<Usuario> data = FXCollections.observableArrayList();
    FilteredList<Usuario> filteredData = null;//new FilteredList<>(data,e->true);

    PreparedStatement preparedStatement = null;
    ResultSet rs = null;

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
//		nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
//		usernameCol.setCellValueFactory(new PropertyValueFactory<>("Usuarioname"));
//		passwordCol.setCellValueFactory(new PropertyValueFactory<>("Password"));
        loadDatabaseData();
    }

    public void loadDatabaseData() {
        String query = "select * from UsuarioInfo";

        try {
            nameBox.clear();
            usernameBox.clear();
            passwordBox.clear();
            data.clear();

//			preparedStatement=connection.prepareStatement(query);
//			rs=preparedStatement.executeQuery();
//			
//			while(rs.next())
//			{
//				data.add(new Usuario(
//						rs.getString("Name"),
//						rs.getString("Usuarioname"),
//						rs.getString("Password")
//						));
//				table.setItems(data);
//			}
//			preparedStatement.close();
//			rs.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    @FXML
    public void addUsuario() throws SQLException {
        String name = nameBox.getText();
        String user = usernameBox.getText();
        String pass = passwordBox.getText();

        String query = "INSERT INTO UsuarioInfo (Name, Usuarioname, Password) VALUES(?,?,?)";

        preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, user);
            preparedStatement.setString(3, pass);

        } catch (SQLException e) {
            System.out.println(e);
        } finally {
            preparedStatement.execute();
            preparedStatement.close();
        }

        loadDatabaseData();
        MainApp.showInformationAlertBox("New Usuario '" + name + "' Added Successfully!");
    }

    static String tempUsuarioname;

    @FXML
    public void showOnClick() {
        try {
            Usuario user = (Usuario) table.getSelectionModel().getSelectedItem();
            String query = "select * from UsuarioInfo";
//			preparedStatement=connection.prepareStatement(query);
//			
//			tempUsuarioname=user.getUsuarioname();
//			nameBox.setText(user.getName());
//			usernameBox.setText(user.getUsuarioname());
//			passwordBox.setText(user.getPassword());
//			
//			preparedStatement.close();
//			rs.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @FXML
    public void deleteUsuario() {
        String name = null;
        try {
            Usuario user = (Usuario) table.getSelectionModel().getSelectedItem();
            String query = "delete from UsuarioInfo where Usuarioname=?";
            preparedStatement = connection.prepareStatement(query);
            //preparedStatement.setString(1, user.getUsuarioname());
            name = user.getName();
            preparedStatement.executeUpdate();

            preparedStatement.close();
            rs.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
        loadDatabaseData();
        MainApp.showInformationAlertBox("Usuario '" + name + "' Deleted Successfully!");

    }

    @FXML
    public void updateUsuario() {
        String query = "update UsuarioInfo set Name=?, Usuarioname=?,Password=? where Usuarioname='" + tempUsuarioname + "'";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nameBox.getText());
            preparedStatement.setString(2, usernameBox.getText());
            preparedStatement.setString(3, passwordBox.getText());
            preparedStatement.execute();
            preparedStatement.close();
            MainApp.showInformationAlertBox("Usuario '" + nameBox.getText() + "' Updated Successfully!");
            loadDatabaseData();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @FXML
    public void searchUsuario() {
//		searchBox.textProperty().addListener((observableValue,oldValue,newValue)->{
//			filteredData.setPredicate((Predicate<? super Usuario>)user->{
//				if(newValue==null||newValue.isEmpty()){
//					return true;
//				}
//				String lowerCaseFilter=newValue.toLowerCase();
//				if(user.getName().toLowerCase().contains(lowerCaseFilter)){
//					return true;
//				}
//				else if(user.getUsuarioname().toLowerCase().contains(lowerCaseFilter)){
//					return true;
//				}
//				return false;
//			});
//		});
//		SortedList<Usuario> sortedData=new SortedList<>(filteredData);
//		sortedData.comparatorProperty().bind(table.comparatorProperty());
//		table.setItems(sortedData);
    }

    @FXML
    public void goSignOut() throws IOException {
        MainApp.showLogin();
    }

    @FXML
    public void handleOnMouseClicked(MouseEvent event) {
        
    }

}
