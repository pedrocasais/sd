package com.example.vehiclesstore.services;

import com.example.vehiclesstore.VehiclesStoreApplication;
import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.model.Veiculos;
import com.example.vehiclesstore.repository.UsersRepository;
import com.example.vehiclesstore.repository.VeiculosRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class HomeService {

    public static String home(Model model, VeiculosRepository vehicleRepository) {

        List<Veiculos> todosVeiculos = vehicleRepository.findByEstadoNot("vendido");

        if (todosVeiculos.isEmpty()) {
            return "main";
        }

        List<Veiculos> listaVeiculos = new ArrayList<>();
        Random r = new Random();

        // Limita ao número de veículos disponíveis ou 8, o que for menor
        int totalParaMostrar = Math.min(todosVeiculos.size(), 8);

        for (int i = 0; i < totalParaMostrar; i++) {
            int indiceAleatorio = r.nextInt(todosVeiculos.size());
            listaVeiculos.add(todosVeiculos.remove(indiceAleatorio));
        }

        model.addAttribute("veiculos", listaVeiculos);
        return "main";

    }

    public static String logoAction(HttpSession s, UsersRepository userRepository){
        Object user = s.getAttribute("email");
        if (user.toString().isEmpty()) {
            return "redirect:/";
        }
        Users users = userRepository.findByEmail(user.toString());
        System.out.println("role .> ," + users.getRole().toString());
        if (users.getRole().equals("ADMIN")) {
            return "redirect:/ADMIN";

        } else {
            return "redirect:/USER";
        }
    }

    public static String faqDetails(HttpSession s, UsersRepository userRepository, Model model){
        Object user = s.getAttribute("email");
        Users users = userRepository.findByEmail(user.toString());

        if (users != null) {
            if (users.getRole().equals("ADMIN")) {
                model.addAttribute("isAdmin", true);
            }
        }
        return "faq";
    }

    public static String aboutUsDetails(HttpSession s, UsersRepository userRepository, Model model){
        Object user = s.getAttribute("email");
        Users users = userRepository.findByEmail(user.toString());

        if (users != null) {
            if (users.getRole().equals("ADMIN")) {
                model.addAttribute("isAdmin", true);
            }
        }
        return "sobre";
    }
}
