/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Aya Ali
 */
@Path("testcontroller")
public class UserHandler {
    @GET
    @Path("/getregistered")
    @Produces(MediaType.APPLICATION_JSON)
    public ArrayList<UserModel> listRegisteredUsers() throws ClassNotFoundException, SQLException {
        
        Service db = new Service();
        ArrayList<UserModel> registeredUsers = new ArrayList();
        registeredUsers = db.listRegisteredUsers();
        return registeredUsers;

}
   @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String register (UserModel user) throws ClassNotFoundException, SQLException{
      
        Service db = new Service();
        return db.register(user.getEmail(), user.getUserName(), user.getPassword(), user.getUserType());
    }
    
    @GET
    @Path("/login/{parameter1}/{parameter2}")
    
    public String login(@PathParam("parameter1") String email, @PathParam("parameter2") String password) throws ClassNotFoundException, SQLException{
          
        Service db = new Service();
        String message = db.login(email, password);
        return message;  
    };
}
