/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Aya Ali
 */
@Path("testservice")
public class Service {
    public Connection con = null;
    
    public Connection dbConnection(){
        if(con == null){
         try{
            String id = "root";
            String pass = "12345678";
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/apiplatfrom?useSSL=false", id, pass);
        }catch(Exception e){
            System.out.println("Failed Connection");
        }   
        }
        return this.con;
    }
    
    public boolean authentication(String email) throws SQLException{
        
        dbConnection();
        String dbLogged = "";
        String query = "select logged from users where email = '"+email+"'";
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(query);
        while(res.next()){
        dbLogged = res.getString("logged");
        }
          if (dbLogged != null && !dbLogged.isEmpty() && dbLogged.equals("1")){   
            return true;
        }
          else{
            return false;
        }
    }; 
    
    public boolean authorization(String email) throws SQLException, ClassNotFoundException{
        
        dbConnection();
        if (authentication(email)){
        String dbusertype = "";
        String query = "select userType from Users where email = '"+email+"'";
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(query);
        while(res.next()){
        dbusertype = res.getString("userType");     
        }
          if (dbusertype.equals("admin")){   
            return true;
        }
        else{
            return false;
        }
        }
        else{
        return false;
        }
    };
    
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<UserModel> listRegisteredUsers(String email) throws ClassNotFoundException, SQLException {
        
        dbConnection();
        ArrayList<UserModel> registered = new ArrayList();
        String query = "select * from Users";
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(query);
        if (authorization(email)){
        while(res.next()){
            UserModel user = new UserModel();
            user.setEmail(res.getString("email"));
            user.setUserName(res.getString("userName"));
            user.setUserType(res.getString("userType"));
            registered.add(user);
        }        
        return registered;
        }
        return registered;
}
   
    @Produces(MediaType.APPLICATION_JSON)
    public String register (String email, String username, String password, String usertype) throws ClassNotFoundException, SQLException{
      
        dbConnection();
         if (usertype.equals("buyer") || usertype.equals("owner") || usertype.equals("admin"))
         {
             String query = "Insert into Users(email, userName, password, userType) Values ('"+email+"','"+username+"','"+password+"','"+usertype+"')";
             con.prepareStatement(query).execute();
        }
        else{
            return "Invalid Data";
        }
        
        return "Your Registeration was Successful";  
    }

    @Produces(MediaType.APPLICATION_JSON)
    public String login(String email, String password) throws ClassNotFoundException, SQLException{
       
        dbConnection();
        String dbEmail = "";
        String dbPassword = "";
        Statement st1 = con.createStatement();
        String query = "select * from users where email = '" + email + "'and  password = '" + password+ "'";
        ResultSet res1 = st1.executeQuery(query);
        while(res1.next()){
            dbEmail = (res1.getString("email"));
            dbPassword = (res1.getString("password"));
        }
        if (email.equals(dbEmail) && password.equals(dbPassword)){
            String loggedIn = "1";
            String query2 = "update users set logged = '"+loggedIn+"' where email = '"+dbEmail+"'";
            Statement st2 = con.createStatement();
            st2.executeUpdate(query2);
            return "Your Login was Successful, Welcome!";
        }
        else{
            return "Invalid Data, Please Try Again";
        }
    }


}
