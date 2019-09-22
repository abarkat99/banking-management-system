/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;

import java.sql.*;

/**
 *
 * @author Abdul Aziz
 */
public class Banking {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Banking b=new Banking();
        boolean exists=b.checkdbexists();
        if(!exists)
            b.createdb();
        else
            System.out.println("Database exists");
        new SSBank().setVisible(true);

    }
    boolean checkdbexists()
    {
        boolean exists=false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull","root","");
            Statement stmt=conn.createStatement();
            String query = "SHOW databases LIKE 'banking'";
            ResultSet rs = stmt.executeQuery(query);                  
            if(rs.next())
                exists = true;
            rs.close();
            stmt.close();
            conn.close();
        }catch(ClassNotFoundException | SQLException e)  {
            System.out.println(e.getMessage());
        }
        return exists;
    }
    void createdb()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/mysql?zeroDateTimeBehavior=convertToNull","root","");
            Statement stmt=conn.createStatement();
            String query = "CREATE DATABASE banking";
            stmt.executeUpdate(query);        
            String queries[]=new String[]{"CREATE TABLE Branch(id INT NOT NULL AUTO_INCREMENT, name VARCHAR(35) NOT NULL, city VARCHAR(35) NOT NULL, balance INT NOT NULL, PRIMARY KEY (id))","CREATE TABLE Employee(id INT NOT NULL AUTO_INCREMENT, name VARCHAR(35) NOT NULL, address VARCHAR(150) NOT NULL, phone VARCHAR(10), email VARCHAR(30), hired_date DATE NOT NULL, username VARCHAR(30) NOT NULL, passwd VARCHAR(30) NOT NULL, b_id INT NOT NULL, pl INT NOT NULL, PRIMARY KEY (id), FOREIGN KEY (b_id) REFERENCES Branch (id), UNIQUE(username))","CREATE TABLE Customer(id INT NOT NULL AUTO_INCREMENT, name VARCHAR(35) NOT NULL, address VARCHAR(150) NOT NULL, phone VARCHAR(10) NOT NULL, email VARCHAR(30), dob DATE NOT NULL, b_id INT NOT NULL, e_enable BOOLEAN NOT NULL, PRIMARY KEY (id), FOREIGN KEY (b_id) REFERENCES Branch (id))","ALTER TABLE Customer AUTO_INCREMENT=100000000000","CREATE TABLE Loan(id INT NOT NULL AUTO_INCREMENT, amount INT NOT NULL, sanction_date DATE NOT NULL, interest INT NOT NULL, c_id INT NOT NULL, PRIMARY KEY (id), FOREIGN KEY (c_id) REFERENCES Customer (id))","CREATE TABLE E_Account(username VARCHAR(30) NOT NULL, passwd VARCHAR(30) NOT NULL, c_id INT NOT NULL, PRIMARY KEY (username), FOREIGN KEY (c_id) REFERENCES Customer (id))","CREATE TABLE Account(id INT NOT NULL AUTO_INCREMENT, balance INT NOT NULL, e_enable BOOLEAN NOT NULL, PRIMARY KEY (id))","ALTER TABLE Account AUTO_INCREMENT=100000000000","CREATE TABLE Cust_Acc(c_id INT NOT NULL, acc_id INT NOT NULL, FOREIGN KEY (c_id) REFERENCES Customer (id), FOREIGN KEY (acc_id) REFERENCES Account (id))","CREATE TABLE Fixed_Deposit(fd_no INT NOT NULL AUTO_INCREMENT, amount INT NOT NULL, start_date DATE NOT NULL, maturity DATE NOT NULL, interest INT NOT NULL, acc_id INT NOT NULL,PRIMARY KEY (fd_no), FOREIGN KEY (acc_id) REFERENCES Account (id))","CREATE TABLE Cust_FD(c_id INT NOT NULL, fd_no INT NOT NULL, FOREIGN KEY (c_id) REFERENCES Customer (id), FOREIGN KEY (fd_no) REFERENCES Fixed_Deposit (fd_no))","CREATE TABLE Transact_Hist(t_id INT NOT NULL AUTO_INCREMENT, t_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, amount INT NOT NULL, debit_acc INT NOT NULL, credit_acc INT NOT NULL, PRIMARY KEY (t_id), FOREIGN KEY (debit_acc) REFERENCES Account (id), FOREIGN KEY (credit_acc) REFERENCES Account (id))","CREATE TABLE Card(card_no INT NOT NULL AUTO_INCREMENT, cvv VARCHAR(3) NOT NULL, valid_from DATE NOT NULL, valid_to DATE NOT NULL, card_type ENUM('credit','debit'), acc_id INT NOT NULL, PRIMARY KEY (card_no), FOREIGN KEY (acc_id) REFERENCES Account (id))","CREATE TABLE Credit_Card(card_no INT NOT NULL, pending_amt INT NOT NULL, credit_limit INT NOT NULL, FOREIGN KEY (card_no) REFERENCES Card (card_no))","INSERT INTO branch(name, city, balance) VALUES('Main','Mumbai','50000000')","INSERT INTO `employee` (`id`, `name`, `address`, `phone`, `email`, `hired_date`, `username`, `passwd`, `b_id`, `pl`) VALUES (NULL, 'admin', 'admin address', '1234567890', 'admin@admin.com', '2019-09-01', 'admin123', 'admin123', '1', '3')"};
            stmt.close();
            conn.close();
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/banking?zeroDateTimeBehavior=convertToNull","root","");
            stmt=conn.createStatement();
            for(String q:queries)
            {
                stmt.executeUpdate(q);
                System.out.println("Executed query: "+q);
            }
            stmt.close();
            conn.close();
            System.out.println("Created Database");
        }catch(ClassNotFoundException | SQLException e)  {
            System.out.println(e.getMessage());
            System.out.println("Database creation failed");
        }
    }
}
