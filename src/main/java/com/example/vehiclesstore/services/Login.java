package com.example.vehiclesstore.services;

import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Login {

     public static String login(String email, String password, UsersRepository usersRepository){
         //SessionController.SessionController(s);
            BCryptPasswordEncoder hashPassword = new BCryptPasswordEncoder();
         Optional<Users> user = usersRepository.findByEmail(email);


         if (user == null){
             /**
              * mensagem alerta erro
              */
             return "login";
         }
         //System.out.println("sda-<  "+ user.getPassword());
         System.out.println(hashPassword.encode(password) + "||" + user.orElseThrow().getPassword());

         if (hashPassword.matches(password,user.orElseThrow().getPassword())){
             System.out.println("logged! ");
             //System.out.println("id -> "+id);

             //System.out.println("wasadbias +.> "+user1.getEmail());
             String role = user.orElseThrow().getRole();
             System.out.println("role -> "+ role);
             return "redirect:/user";
         }
         else {
             /**
              * mensgaem de alerta erro password
              */
             return "login";
         }
     }
}
