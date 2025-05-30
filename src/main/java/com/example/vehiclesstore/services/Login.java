package com.example.vehiclesstore.services;

import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Login {

    private static BCryptPasswordEncoder hashPassword;

     public static String login(String email, String password, UsersRepository usersRepository){
         //SessionController.SessionController(s);

         Users user = usersRepository.findByEmail(email);

         if (user == null){
             /**
              * mensagem alerta erro
              */
             return "login";
         }
         //System.out.println("sda-<  "+ user.getPassword());


         if (hashPassword.matches(password,user.getPassword())){
             System.out.println("logged! ");
             //System.out.println("id -> "+id);

             //System.out.println("wasadbias +.> "+user1.getEmail());
             String role = user.getRole();
             System.out.println("role -> "+ role);
             return "redirect:/"+role;
         }
         else {
             /**
              * mensgaem de alerta erro password
              */
             return "login";
         }
     }
}
