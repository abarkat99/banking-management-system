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
        SSBank.main(null);

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
            String queries[]=new String[]{"CREATE TABLE Branch(id INT NOT NULL AUTO_INCREMENT, name VARCHAR(35) NOT NULL, city VARCHAR(35) NOT NULL, balance INT NOT NULL, PRIMARY KEY (id))","CREATE TABLE Employee(id INT NOT NULL AUTO_INCREMENT, name VARCHAR(35) NOT NULL, address VARCHAR(150) NOT NULL, phone VARCHAR(10), email VARCHAR(30), hired_date DATE NOT NULL, username VARCHAR(30) NOT NULL, passwd VARCHAR(30) NOT NULL, b_id INT NOT NULL, pl INT NOT NULL, PRIMARY KEY (id), FOREIGN KEY (b_id) REFERENCES Branch (id), UNIQUE(username))","CREATE TABLE Customer(id BIGINT NOT NULL AUTO_INCREMENT, name VARCHAR(35) NOT NULL, address VARCHAR(150) NOT NULL, phone VARCHAR(10) NOT NULL, email VARCHAR(30), dob DATE NOT NULL, b_id INT NOT NULL, e_enable BOOLEAN NOT NULL, PRIMARY KEY (id), FOREIGN KEY (b_id) REFERENCES Branch (id))","ALTER TABLE Customer AUTO_INCREMENT=100000000000","CREATE TABLE Loan(id INT NOT NULL AUTO_INCREMENT, amount INT NOT NULL, sanction_date DATE NOT NULL, interest INT NOT NULL, c_id BIGINT NOT NULL, acc_id BIGINT NOT NULL, PRIMARY KEY (id), FOREIGN KEY (c_id) REFERENCES Customer (id),FOREIGN KEY (acc_id) REFERENCES account (id))","CREATE TABLE E_Account(username VARCHAR(30) NOT NULL, passwd VARCHAR(30) NOT NULL, c_id BIGINT NOT NULL, PRIMARY KEY (username), FOREIGN KEY (c_id) REFERENCES Customer (id))","CREATE TABLE Account(id BIGINT NOT NULL AUTO_INCREMENT, balance INT NOT NULL, e_enable BOOLEAN NOT NULL, PRIMARY KEY (id))","ALTER TABLE Account AUTO_INCREMENT=100000000000","CREATE TABLE Cust_Acc(c_id BIGINT NOT NULL, acc_id BIGINT NOT NULL, FOREIGN KEY (c_id) REFERENCES Customer (id), FOREIGN KEY (acc_id) REFERENCES Account (id))","CREATE TABLE Transact_Hist(t_id INT NOT NULL AUTO_INCREMENT, t_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, amount INT NOT NULL, debit_acc BIGINT NOT NULL, credit_acc BIGINT NOT NULL, PRIMARY KEY (t_id), FOREIGN KEY (debit_acc) REFERENCES Account (id), FOREIGN KEY (credit_acc) REFERENCES Account (id))","CREATE TABLE Card(card_no BIGINT NOT NULL AUTO_INCREMENT, cvv VARCHAR(3) NOT NULL, valid_from DATE NOT NULL, valid_to DATE NOT NULL, card_type ENUM('credit','debit'), acc_id BIGINT NOT NULL, c_id BIGINT NOT NULL, PRIMARY KEY (card_no), FOREIGN KEY (acc_id) REFERENCES Account (id), FOREIGN KEY (c_id) REFERENCES Customer (id))","CREATE TABLE Credit_Card(card_no BIGINT NOT NULL, pending_amt INT NOT NULL, credit_limit INT NOT NULL, FOREIGN KEY (card_no) REFERENCES Card (card_no))","ALTER TABLE Card AUTO_INCREMENT=100000000000","INSERT INTO branch(name, city, balance) VALUES('Main','Mumbai','50000000')","INSERT INTO `employee` (`id`, `name`, `address`, `phone`, `email`, `hired_date`, `username`, `passwd`, `b_id`, `pl`) VALUES (NULL, 'admin', 'admin address', '1234567890', 'admin@admin.com', '2019-09-01', 'admin123', 'admin123', '1', '3')","INSERT INTO `account` (`id`, `balance`, `e_enable`) VALUES ('1', '0', '0')"};
            stmt.close();
            conn.close();
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/banking?zeroDateTimeBehavior=convertToNull","root","");
            stmt=conn.createStatement();
            for(String q:queries)
            {
                stmt.executeUpdate(q);
                System.out.println("Executed query: "+q);
            }
            queries=new String[]{"SET GLOBAL event_scheduler = ON","CREATE TRIGGER `employee_dttrigger` BEFORE INSERT ON `employee` FOR EACH ROW BEGIN SET NEW.hired_date = CURRENT_DATE(); END;","CREATE TRIGGER `card_dttrigger` BEFORE INSERT ON `card` FOR EACH ROW BEGIN SET NEW.valid_from = CURRENT_DATE(), NEW.valid_to =DATE_ADD(CURRENT_DATE(), INTERVAL 20 YEAR); END;","CREATE PROCEDURE SEARCHBRANCH(bname VARCHAR(35)) BEGIN SELECT * FROM branch WHERE name LIKE bname;END;","CREATE PROCEDURE ADDBRANCH(bname VARCHAR(35),bcity VARCHAR(35),balance INT) BEGIN INSERT INTO branch(name,city,balance) VALUES(bname,bcity,balance);SELECT LAST_INSERT_ID();END;","CREATE PROCEDURE MODIFYBRANCH(bid INT,bname VARCHAR(35),bcity VARCHAR(35),bbal INT) BEGIN UPDATE branch SET name=bname, city=bcity, balance=bbal WHERE id=bid;END;","CREATE PROCEDURE ADDEMPLOYEE(IN ename VARCHAR(35), IN eaddr VARCHAR(150), IN ephone VARCHAR(10), IN eemail VARCHAR(30), IN eusername VARCHAR(30), IN epasswd VARCHAR(30), IN eb_id INT, IN epl INT) BEGIN IF (SELECT COUNT(*) FROM employee WHERE username=eusername) THEN SELECT -1; ELSEIF (SELECT COUNT(*) FROM branch WHERE id=eb_id)<1 THEN SELECT -2; ELSE INSERT INTO employee(name,address,phone,email,username,passwd,b_id,pl) VALUES(ename,eaddr,ephone,eemail,eusername,epasswd,eb_id,epl); SELECT LAST_INSERT_ID(); END IF; END;","CREATE PROCEDURE MODIFYEMPLOYEE(IN eid INT, IN ename VARCHAR(35), IN eaddr VARCHAR(150), IN ephone VARCHAR(10), IN eemail VARCHAR(30), IN eusername VARCHAR(30), IN epasswd VARCHAR(30), IN eb_id INT, IN epl INT) BEGIN IF (SELECT COUNT(*) FROM employee WHERE username=eusername AND id!=eid) THEN SELECT -1; ELSEIF (SELECT COUNT(*) FROM branch WHERE id=eb_id)<1 THEN SELECT -2; ELSE UPDATE employee SET name=ename, address=eaddr, phone=ephone, email=eemail, username=eusername,passwd=epasswd, b_id=eb_id, pl=epl WHERE id=eid; SELECT LAST_INSERT_ID(); END IF; END;","CREATE PROCEDURE ADDCUSTOMER(cname VARCHAR(35),caddr VARCHAR(150),cphone VARCHAR(10), cemail VARCHAR(30), cdob DATE, cb_id INT, ce_enable BOOLEAN) BEGIN INSERT INTO customer(name, address, phone, email, dob, b_id, e_enable) VALUES(cname, caddr, cphone, cemail, cdob, cb_id, ce_enable);SELECT LAST_INSERT_ID();END;","CREATE PROCEDURE MODIFYCUSTOMER(cid BIGINT, cname VARCHAR(35),caddr VARCHAR(150),cphone VARCHAR(10), cemail VARCHAR(30), cdob DATE, cb_id INT, ce_enable BOOLEAN) BEGIN UPDATE customer SET name=cname, address=caddr, phone=cphone, email=cemail, dob=cdob, b_id=cb_id, e_enable=ce_enable;END;","CREATE PROCEDURE GETTRANSACTHIST(acc_id BIGINT) BEGIN SELECT t_id,DATE(t_time) AS 'Date',TIME(t_time) AS 'Time', CASE WHEN debit_acc=1 THEN 'DEPOSIT CASH' WHEN credit_acc=1 THEN 'WITHDRAW CASH' WHEN debit_acc=acc_id THEN CONCAT('TRANSFER TO ', credit_acc) ELSE CONCAT('TRANSFER FROM ', debit_acc) END AS 'Description', CONCAT('Rs. ',amount) AS 'Amount' FROM transact_hist WHERE debit_acc=acc_id OR credit_acc=acc_id;END;","CREATE EVENT IF NOT EXISTS loanupdate ON SCHEDULE EVERY 1 MONTH STARTS '2019-05-10' DO UPDATE loan SET amount=ROUND(amount*interest/1200.0)"};
            for(String q:queries)
            {
                stmt.execute(q);
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
