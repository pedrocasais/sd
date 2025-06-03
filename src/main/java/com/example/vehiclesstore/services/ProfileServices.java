package com.example.vehiclesstore.services;

import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.model.Vendas;
import com.example.vehiclesstore.repository.UsersRepository;
import com.example.vehiclesstore.repository.VendasRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class ProfileServices {

    public static String Perfil(Model model, HttpSession session, UsersRepository userRepository, VendasRepository vendasRepository) {
        Object userEmail = session.getAttribute("email"); // ou use SessionController


        if (userEmail == null) {
            return "redirect:/login"; // Proteção extra
        }
        Users users = userRepository.findByEmail(userEmail.toString());


        if (users.getRole().equals("ADMIN")) {
            model.addAttribute("isAdmin", true);
        }

        System.out.println("user -> " + userEmail.toString());
        Users user = userRepository.findByEmail(userEmail.toString());
        model.addAttribute("user", user);

        List<Vendas> compras = vendasRepository.findByUser(user);
        model.addAttribute("compras", compras);

        Users user2 = userRepository.findByEmail(userEmail.toString());
        model.addAttribute("user", user2);
        return "perfil";
    }

    public static String changePassword(HttpSession session, UsersRepository userRepository, String novaPassword, String confirmarPassword, BCryptPasswordEncoder hashPassword){
        Object email = session.getAttribute("email");

        if (email == null) {
            System.out.println("Erro: Email ausente");
            return "redirect:/perfil?error";
        }

        Users user = userRepository.findByEmail(email.toString());

        if (user == null) {
            System.out.println("Erro: Utilizador não encontrado");
            return "redirect:/perfil?error";
        }
        if (!novaPassword.equals(confirmarPassword)) {
            return "redirect:/perfil?error";
        }
        String hashed = hashPassword.encode(novaPassword);
        user.setPassword(hashed);
        userRepository.save(user);

        System.out.println("Nova password (hashed): " + hashed);
        return "redirect:/perfil?success";
    }
}
