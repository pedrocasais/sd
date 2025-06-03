package com.example.vehiclesstore.services;

import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.model.Veiculos;
import com.example.vehiclesstore.model.Vendas;
import com.example.vehiclesstore.repository.UsersRepository;
import com.example.vehiclesstore.repository.VeiculosRepository;
import com.example.vehiclesstore.repository.VendasRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class VendasService {

    public static String confirmSell(Integer id, Integer nif, HttpSession session, UsersRepository userRepository, VeiculosRepository vehicleRepository, VendasRepository vendasRepository){
        Object email = session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        Users userOpt = userRepository.findByEmail(email.toString());
        Optional<Veiculos> veiculoOpt = vehicleRepository.findById(id);

        if (userOpt.equals("") || veiculoOpt.isEmpty()) {
            return "redirect:/veiculos?erro";
        }

        // Criar venda
        Vendas venda = new Vendas();
        venda.setUser(userOpt); // Extrai o valor do Optional
        venda.setVeiculo(veiculoOpt.get());
        venda.setDataVenda(LocalDateTime.now());
        venda.setPrecoVenda(veiculoOpt.get().getPreco());
        venda.setNif(nif);
        long ref = (long) (Math.random() * 99999999 + 100000000);
        venda.setRefPagamento(ref);

        // Atualiza estado do veículo e salva
        Veiculos veiculo = veiculoOpt.get();
        veiculo.setEstado("vendido");
        vehicleRepository.save(veiculo);

        // Guarda a venda
        vendasRepository.save(venda);

        return "redirect:/veiculos?sucesso";
    }
    public static String showForms(Model model, VeiculosRepository vehicleRepository, UsersRepository userRepository){
        model.addAttribute("veiculos", vehicleRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "vendas";
    }
}
