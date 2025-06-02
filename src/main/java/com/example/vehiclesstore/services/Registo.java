package com.example.vehiclesstore.services;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Registo {


    public static String registo(String email, String apelido, String localidade, String codPostal, String password, String password2, String nome, Long tel, String morada, HttpSession s, UsersRepository userRepository, BCryptPasswordEncoder hashPassword){
        System.out.println("email -> " + email.toString());


        Users user = new Users();
        user.setEmail(email);

        user.setNome(nome);
        user.setApelido(apelido);

        user.setMorada(morada);
        user.setLocalidade(localidade);
        user.setCodPostal(codPostal);
        user.setNumTelemovel(tel);

        user.setPassword(hashPassword.encode(password));
        user.setRole("USER");
        user.setNumTelemovel(tel);
        userRepository.save(user);

        // Inicia sessão automaticamente
        s.setAttribute("email", user.getEmail());

        return "redirect:/USER";
    }

}
