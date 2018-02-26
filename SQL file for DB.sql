CREATE DATABASE profile;
USE profile;

CREATE TABLE profiles(
	profileID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(30),
    lastName VARCHAR(30),
    address VARCHAR(100),
    phoneNum VARCHAR(12),
    birthday DATE,
    image VARCHAR(100)
);

SELECT * FROM profiles;


INSERT INTO profiles(firstName,lastName, address, phoneNum,birthday, image)
 VALUES ('Teck', 'Hua', '111 One Georgian Drive', '705-111-2222', '1997-01-01', 'some file');

