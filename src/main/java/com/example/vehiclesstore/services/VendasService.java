package com.example.vehiclesstore.services;

import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.model.Veiculos;
import com.example.vehiclesstore.model.Vendas;
import com.example.vehiclesstore.repository.UsersRepository;
import com.example.vehiclesstore.repository.VeiculosRepository;
import com.example.vehiclesstore.repository.VendasRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    public static String confirmCompra(HttpSession session, VeiculosRepository vehicleRepository, Integer id, Model model){
        Object email = session.getAttribute("email");

        if (email == null) {
            session.setAttribute("redirectAfterLogin", "/comprar/" + id);
            return "redirect:/login";
        }

        Optional<Veiculos> veiculoOpt = vehicleRepository.findById(id);
        if (veiculoOpt.isEmpty()) {
            return "redirect:/veiculos";
        }

        model.addAttribute("veiculo", veiculoOpt.get());
        return "compra";
    }

    public static ResponseEntity<byte[]>  faturaGenerator(VendasRepository vendasRepository, Integer idVenda){
        Optional<Vendas> vendaOpt = vendasRepository.findById(idVenda);
        if (vendaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Vendas venda = vendaOpt.get();

        StringBuilder sb = new StringBuilder();
        sb.append("========== Fatura da Compra ==========\n");
        sb.append("AutoUBI - Sistema de Gestão de Veículos\n\n");

        sb.append(">> Número da Venda:\n");
        sb.append("ID Venda: ").append(venda.getIdVenda()).append("\n\n");

        sb.append(">> Dados do Veículo:\n");
        sb.append("Marca: ").append(venda.getVeiculo().getMarca()).append("\n");
        sb.append("Modelo: ").append(venda.getVeiculo().getModelo()).append("\n");
        sb.append("Ano: ").append(venda.getVeiculo().getAno()).append("\n");
        sb.append("Categoria: ").append(venda.getVeiculo().getCategoria()).append("\n");
        sb.append("Cor: ").append(venda.getVeiculo().getCor()).append("\n");
        sb.append("Preço: ").append(venda.getPrecoVenda()).append(" €\n\n");

        sb.append(">> Comprador:\n");
        sb.append("Nome: ").append(venda.getUser().getNome()).append("\n");
        sb.append("Email: ").append(venda.getUser().getEmail()).append("\n");
        sb.append("NIF: ").append(venda.getNif()).append("\n");
        sb.append("Referência: ").append(venda.getRefPagamento()).append("\n");
        sb.append("Data: ").append(venda.getDataVenda()).append("\n");

        byte[] conteudo = sb.toString().getBytes();

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=fatura_" + idVenda + ".txt")
                .contentType(MediaType.TEXT_PLAIN)
                .body(conteudo);
    }
}
