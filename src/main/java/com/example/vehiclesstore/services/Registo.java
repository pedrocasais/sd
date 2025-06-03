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

    /**
     * função responsável por guardar o user na DB
     */
    public static String registo(String email, String apelido, String localidade, String codPostal, String password, String password2, String nome, Long tel, String morada, HttpSession s, UsersRepository userRepository, BCryptPasswordEncoder hashPassword){
        System.out.println("email -> " + email.toString());

        if (!(userRepository.findByEmail(email) == null)) {
            return "redirect:/registar?error=email";
        }

        // ALTERAR
        // password doesn't match error no page
        if (!password.equals(password2)) {
            return "redirect:/registar?error=pass";
        }

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
        //s.setAttribute("email", user.getEmail());

        return "redirect:/login";
    }

}
