/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package banking;

import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author Abdul Aziz
 */
public class Session {
    String username,password,name;
    int id,b_id,pl;
    Session(String username,String password)
    {
        this.username=username;
        this.password=password;
        this.getSessionDetails();
    }
    private void getSessionDetails()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/banking?zeroDateTimeBehavior=convertToNull","root","");
            Statement stmt=conn.createStatement();
            ResultSet rs=stmt.executeQuery("SELECT id, name, b_id, pl FROM employee WHERE username='"+username+"' AND passwd='"+password+"'");
            if(rs.next())
            {
                id=rs.getInt(1);
                name=rs.getString(2);
                b_id=rs.getInt(3);
                pl=rs.getInt(4);
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
}
