package com.example.vehiclesstore.services;

import com.example.vehiclesstore.VehiclesStoreApplication;
import com.example.vehiclesstore.model.Veiculos;
import com.example.vehiclesstore.repository.VeiculosRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class Home {

    public static String home(Model model, VeiculosRepository vehicleRepository) {
        /* int n = (int) vehicleRepository.count();

        List<Veiculos> listaVeiculos = new ArrayList<>();
        Random r = new Random();
        System.out.println("n -> " + n);
        if (n == 0) {
            System.out.println("aqui ????");
            return "main";
        }
        for (int i = 0; i < 8; i++) {
            Veiculos veiculo = vehicleRepository.findByID(r.nextInt(n));
            if (veiculo != null) {
                listaVeiculos.add(veiculo);
            }
        }

        model.addAttribute("veiculos", listaVeiculos);

        return "main";

       */
            List<Veiculos> todosVeiculos = vehicleRepository.findAll();

    if(todosVeiculos.isEmpty()){
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
}
