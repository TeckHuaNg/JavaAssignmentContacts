/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Profile;

/**
 * FXML Controller class
 *
 * @author Bearuang
 */
public class ProfileListController implements Initializable {

    @FXML private TableView<Profile> contactsTable;
    @FXML private TableColumn<Profile, Integer> contactIDColumn;
    @FXML private TableColumn<Profile, String> firstNameColumn;
    @FXML private TableColumn<Profile, String> lastNameColumn;
    @FXML private TableColumn<Profile, String> addressColumn;
    @FXML private TableColumn<Profile, String> phoneColumn;
    
    @FXML private Button editButton;
    @FXML private TextField searchTextField;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // confgure the table columns
        contactIDColumn.setCellValueFactory(new PropertyValueFactory<Profile, Integer>("contactID"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<Profile, String>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<Profile, String>("lastName"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<Profile, String>("address"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<Profile, String>("phoneNum"));
        
        editButton.setDisable(true);
        
        try
        {
            loadContacts();
        }
        catch (SQLException e)
        {
            System.err.println(e.getMessage());
        }
        
    }   
    
    /**
     * This method will load the contacts from the database and load them into 
     * the TableView object
     */
    public void loadContacts() throws SQLException{
        ObservableList<Profile> contacts = FXCollections.observableArrayList();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try{
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es/gc200348264", "gc200348264", "kS9h4EJ3");
            //2.  create a statement object
            statement = conn.createStatement();
            
            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM profiles");
            
            //4.  create volunteer objects from each record
            while (resultSet.next())
            {
                Profile newProfile = new Profile(resultSet.getString("firstName"),
                                                       resultSet.getString("lastName"),
                                                       resultSet.getString("address"),
                                                       resultSet.getString("phoneNum"),
                                                       resultSet.getDate("birthday").toLocalDate());
                newProfile.setContactID(resultSet.getInt("profileID"));
                newProfile.setImage(new File(resultSet.getString("image")));
                
                contacts.add(newProfile);
            }
            
            contactsTable.getItems().addAll(contacts);
            
        } catch (Exception e)
        {
            System.err.println(e);
        }
        finally
        {
            if (conn != null)
                conn.close();
            if(statement != null)
                statement.close();
            if(resultSet != null)
                resultSet.close();
        }
    }
    
    /**
     * This method will return contacts that have the have the same value in the Search Box
     */
    public void SearchButtonPushed() throws SQLException
    {
        ObservableList<Profile> contacts = FXCollections.observableArrayList();
        
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try{
            //1. connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es/gc200348264", "gc200348264", "kS9h4EJ3");
            //2.  create a statement object
            statement = conn.createStatement();
            
            //3.  create the SQL query
            resultSet = statement.executeQuery("SELECT * FROM profiles WHERE firstName LIKE '%" + searchTextField.getText() + "%' OR lastName LIKE '%"
                                                + searchTextField.getText() + "%'");
            
            //4.  create volunteer objects from each record
            while (resultSet.next())
            {
                Profile newProfile = new Profile(resultSet.getString("firstName"),
                                                       resultSet.getString("lastName"),
                                                       resultSet.getString("address"),
                                                       resultSet.getString("phoneNum"),
                                                       resultSet.getDate("birthday").toLocalDate());
                newProfile.setContactID(resultSet.getInt("profileID"));
                newProfile.setImage(new File(resultSet.getString("image")));
                
                contacts.add(newProfile);
            }
           
            contactsTable.setItems(contacts);
            //contactsTable.getItems().addAll(contacts);
            searchTextField.setText("");
            
        } catch (Exception e)
        {
            System.err.println(e);
        }
        finally
        {
            if (conn != null)
                conn.close();
            if(statement != null)
                statement.close();
            if(resultSet != null)
                resultSet.close();
        }
    }
    
    /**
     * This method will switch to the NewUserView scene when the button is pushed
     */
    public void CreateNewProfileButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "NewContact.fxml", "Create Contact");
    }
    
}
