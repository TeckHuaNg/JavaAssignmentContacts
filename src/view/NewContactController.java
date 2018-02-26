/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import model.Profile;

/**
 * FXML Controller class
 *
 * @author Bearuang
 */
public class NewContactController implements Initializable {

    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private TextField addressTextField;
    @FXML private TextField phoneTextField;
    @FXML private DatePicker birthday;
    @FXML private Label errorMsg;
    @FXML private ImageView imageView;
    
    private File imageFile;
    private boolean imageFileChanged;
    
    /**
     * This method will change back to the TableView of contacts without adding
     * a user.  All data in the form will be lost
     */
    public void cancelButtonPushed(ActionEvent event) throws IOException
    {
        SceneChanger sc = new SceneChanger();
        sc.changeScenes(event, "ProfileList.fxml", "All Contacts");
    }
        
    /**
     * This method will read from the scene and try to create a new instance of a Profile.
     * If a profile was successfully created, it is updated in the database.
     */
    public void saveProfile(ActionEvent event){
        
        try
        {
            Profile newProfile;
            if(imageFileChanged)
            {
                validateInputs();
                newProfile = new Profile(firstNameTextField.getText(), lastNameTextField.getText(),addressTextField.getText(),phoneTextField.getText()
                                            ,birthday.getValue(), imageFile);
            }
            else
            {
                validateInputs();
                newProfile = new Profile(firstNameTextField.getText(), lastNameTextField.getText(),addressTextField.getText(),phoneTextField.getText()
                                            ,birthday.getValue());
            }
            
            errorMsg.setText("");
            newProfile.insertIntoDB();
            SceneChanger sc = new SceneChanger();
            sc.changeScenes(event, "ProfileList.fxml", "All Contacts");
        }
        catch(Exception e)
        {
            errorMsg.setText(e.getMessage());
        }
        
    }
    
    /**
     * This method will validate phoneTextField and birthday Picker
     * The border will turns red if input is invalid
     */
    public void validateInputs(){
        int age = Period.between(birthday.getValue(), LocalDate.now()).getYears();;
        
        if(!phoneTextField.getText().matches("[2-9]\\d{2}[.-]?\\d{3}[.-]\\d{4}"))
                phoneTextField.setStyle("-fx-text-box-border: red");
        else
            phoneTextField.setStyle("-fx-text-box-border: transparent");
        
        if(age < 0 || age > 100)
            birthday.setStyle("-fx-border-color: red");
        else
            birthday.setStyle("-fx-border-color: transparent");
            
    }
    
    /**
     * When this button is pushed, a FileChooser object is launched to allow the user
     * to browse for a new image file.  When that is complete, it will update the 
     * view with a new image
     */
    public void chooseImageButtonPushed(ActionEvent event)
    {
        //get the Stage to open a new window (or Stage in JavaFX)
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        //Instantiate a FileChooser object
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        
        //filter for .jpg and .png
        FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("Image File (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("Image File (*.png)", "*.png");
        fileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter);
        
        //Set to the user's picture directory or user directory if not available
        String userDirectoryString = System.getProperty("user.home")+"\\Pictures";
        File userDirectory = new File(userDirectoryString);
        
        //if you cannot navigate to the pictures directory, go to the user home
        if (!userDirectory.canRead())
            userDirectory = new File(System.getProperty("user.home"));
        
        fileChooser.setInitialDirectory(userDirectory);
        
        //open the file dialog window
        File tmpImageFile = fileChooser.showOpenDialog(stage);
        
        if (tmpImageFile != null)
        {
            imageFile = tmpImageFile;
        
            //update the ImageView with the new image
            if (imageFile.isFile())
            {
                try
                {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    Image img = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageView.setImage(img);
                    imageFileChanged = true;
                }
                catch (IOException e)
                {
                    System.err.println(e.getMessage());
                }
            }
        }
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        birthday.setValue(LocalDate.now().minusYears(10));
        
        errorMsg.setText("");
        
        imageFileChanged = false; //initially the image has not changed, use the default
        
        //load the defautl image for the avatar
        try{
            imageFile = new File("./src/images/default.png");
            BufferedImage bufferedImage = ImageIO.read(imageFile);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);
            imageView.setImage(image);
            
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }    
    
}
