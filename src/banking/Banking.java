/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;

import java.sql.*;

/**
 *
 * @author Yusi
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
            stmt.close();
            conn.close();
            System.out.println("Created Database");
        }catch(ClassNotFoundException | SQLException e)  {
            System.out.println(e.getMessage());
            System.out.println("Database creation failed");
        }
    }
}
