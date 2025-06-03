package com.example.vehiclesstore.services;

import com.example.vehiclesstore.model.Estatisticas;
import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.model.VendasMensal;
import com.example.vehiclesstore.repository.UsersRepository;
import com.example.vehiclesstore.repository.VendasRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EstatisticasService {

    public static String showStats(Model model, HttpSession s, UsersRepository userRepository, VendasRepository vendasRepository){
        Object user = s.getAttribute("email");
        Users users = userRepository.findByEmail(user.toString());

        if (users != null) {
            if (users.getRole().equals("ADMIN")) {
                model.addAttribute("isAdmin", true);
            }
        }

        ArrayList<Estatisticas> todosClientes = vendasRepository.findTopClientes();

        if (todosClientes.isEmpty()) {
            model.addAttribute("melhoresClientes", new ArrayList<Estatisticas>());
            model.addAttribute("totalVendidos", 0L);
            model.addAttribute("mostrarTabelaClientes", false);
            model.addAttribute("mostrarTabelaVendas", false);
            return "estatisticas";
        }

        List<Estatisticas> top3Clientes = todosClientes.stream()
                .limit(3)
                .collect(Collectors.toList());

        Long totalVendidos = vendasRepository.count();

        List<VendasMensal> totais = vendasRepository.findTotalVendasPorMes();

        model.addAttribute("vendasMensais", totais);
        model.addAttribute("melhoresClientes", top3Clientes);
        model.addAttribute("totalVendidos", totalVendidos);
        model.addAttribute("mostrarTabelaClientes", !top3Clientes.isEmpty());
        model.addAttribute("mostrarTabelaVendas", !totais.isEmpty());

        return "estatisticas";
    }
}
