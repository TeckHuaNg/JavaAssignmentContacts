/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;


/**
 *
 * @author Bearuang
 */
public class Profile {
    private String firstName, lastName, address, phoneNum;
    private LocalDate bday;
    private File image;
    private int contactID;


    public Profile(String firstName, String lastName, String address, String phoneNum, LocalDate bday) {
        setFirstName(firstName);
        setLastname(lastName);
        setAddress(address);
        setPhoneNum(phoneNum);
        setBday(bday);
        setImage(new File("./src/images/default.png"));
    }

    public Profile(String firstName, String lastName, String address, String phoneNum, LocalDate bday, File image) throws IOException {
        this(firstName, lastName, address, phoneNum, bday);
        setImage(image);
        copyImageFile();
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        if(contactID < 0)
            throw new IllegalArgumentException("Contact ID must be greater than or equals to 0");
        else
            this.contactID = contactID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastname(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    
    public void setPhoneNum(String phoneNum) {
        
        if(phoneNum.matches("[2-9]\\d{2}[.-]?\\d{3}[.-]\\d{4}"))
            this.phoneNum = phoneNum;  
        else  
            throw new IllegalArgumentException("Phone number must in this format NXX-XXX-XXXX");
        
    }

    public LocalDate getBday() {
        return bday;
    }

    /**
     * Validate the age is between 0-100
     * @param bday 
     */
    public void setBday(LocalDate bday) {
        int age = Period.between(bday, LocalDate.now()).getYears();
         
        if(age <= 0 || age > 100)
            throw new IllegalArgumentException("The person must be 0-100 years of age");
        else
            this.bday = bday;
    }

    public File getImage() {
        return image;
    }

    public void setImage(File image) {
        this.image = image;
    }
    
    /**
     * This method will copy the file specified to the images directory on this server and give it 
     * a unique name
     */
    public void copyImageFile() throws IOException
    {
        //create a new Path to copy the image into a local directory
        Path sourcePath = image.toPath();
        
        String uniqueFileName = getUniqueFileName(image.getName());
        
        Path targetPath = Paths.get("./src/images/"+uniqueFileName);
        
        //copy the file to the new directory
        Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        
        //update the imageFile to point to the new File
        image = new File(targetPath.toString());
    }
    
    /**
     * This method will receive a String that represents a file name and return a
     * String with a random, unique set of letters prefixed to it
     */
    private String getUniqueFileName(String oldFileName)
    {
        String newName;
        
        //create a Random Number Generator
        SecureRandom rng = new SecureRandom();
        
        //loop until we have a unique file name
        do
        {
            newName = "";
            
            //generate 32 random characters
            for (int count=1; count <=32; count++)
            {
                int nextChar;
                
                do
                {
                    nextChar = rng.nextInt(123);
                } while(!validCharacterValue(nextChar));
                
                newName = String.format("%s%c", newName, nextChar);
            }
            newName += oldFileName;
            
        } while (!uniqueFileInDirectory(newName));
        
        return newName;
    }
    
    /**
     * This method will validate if the integer given corresponds to a valid
     * ASCII character that could be used in a file name
     */
    public boolean validCharacterValue(int asciiValue)
    {
        
        //0-9 = ASCII range 48 to 57
        if (asciiValue >= 48 && asciiValue <= 57)
            return true;
        
        //A-Z = ASCII range 65 to 90
        if (asciiValue >= 65 && asciiValue <= 90)
            return true;
        
        //a-z = ASCII range 97 to 122
        if (asciiValue >= 97 && asciiValue <= 122)
            return true;
        
        return false;
    }
    
    /**
     * This method will search the images directory and ensure that the file name
     * is unique
     */
    public boolean uniqueFileInDirectory(String fileName)
    {
        File directory = new File("./src/images/");
        
        File[] dir_contents = directory.listFiles();
                
        for (File file: dir_contents)
        {
            if (file.getName().equals(fileName))
                return false;
        }
        return true;
    }
    
     /**
     * This method will write the instance of the Volunteer into the database
     */
    public void insertIntoDB() throws SQLException
    {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        
        try
        {
            //1. Connect to the database
            conn = DriverManager.getConnection("jdbc:mysql://sql.computerstudi.es/gc200348264", "gc200348264", "kS9h4EJ3");
            
            //2. Create a String that holds the query with ? as user inputs
            String sql = "INSERT INTO profiles (firstName, lastName, address, phoneNum, birthday, image)"
                    + "VALUES (?,?,?,?,?,?)";
                    
            //3. prepare the query
            preparedStatement = conn.prepareStatement(sql);
            
            //4. Convert the birthday into a SQL date
            Date db = Date.valueOf(bday);
                   
            //5. Bind the values to the parameters
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, phoneNum);
            preparedStatement.setDate(5, db);
            preparedStatement.setString(6, image.getName());
            
            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
        finally
        {
            if (preparedStatement != null)
                preparedStatement.close();
            
            if (conn != null)
                conn.close();
        }
    }
}
