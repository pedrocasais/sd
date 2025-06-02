package com.example.vehiclesstore.services;

import com.example.vehiclesstore.model.Veiculos;
import com.example.vehiclesstore.repository.VeiculosRepository;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
public class VehiclesService {

    public static String VehiclesService(VeiculosRepository vehicleRepository, Model model){
        List<Veiculos> veiculos = vehicleRepository.findAll();

        model.addAttribute("veiculos", veiculos);
        model.addAttribute("marcaSelecionada", "");
        model.addAttribute("anoSelecionado", "");
        model.addAttribute("marcas", vehicleRepository.listarMarcas());
        model.addAttribute("anos", vehicleRepository.listarAnos());
        model.addAttribute("precoMax", 2000000);

        return "Visualizarveiculos";
    }

    public static String listVehiclesFilter(String marca, String ano, Double precoMax, Model model, VeiculosRepository vehicleRepository){

        List<Veiculos> veiculos;

        if (marca != null && !marca.isEmpty() && ano != null && !ano.isEmpty() && precoMax != null) {
            veiculos = vehicleRepository.findByMarcaAndAnoAndPrecoLessThanEqual(marca, ano, precoMax);
        } else if (marca != null && !marca.isEmpty() && ano != null && !ano.isEmpty()) {
            veiculos = vehicleRepository.findByMarcaAndAno(marca, ano);
        } else if (marca != null && !marca.isEmpty() && precoMax != null) {
            veiculos = vehicleRepository.findByMarcaAndPrecoLessThanEqual(marca, precoMax);
        } else if (ano != null && !ano.isEmpty() && precoMax != null) {
            veiculos = vehicleRepository.findByAnoAndPrecoLessThanEqual(ano, precoMax);
        } else if (marca != null && !marca.isEmpty()) {
            veiculos = vehicleRepository.findByMarca(marca);
        } else if (ano != null && !ano.isEmpty()) {
            veiculos = vehicleRepository.findByAno(ano);
        } else if (precoMax != null) {
            veiculos = vehicleRepository.findByPrecoLessThanEqual(precoMax);
        } else {
            veiculos = vehicleRepository.findAll();
        }

        model.addAttribute("veiculos", veiculos);
        model.addAttribute("marcaSelecionada", marca);
        model.addAttribute("anoSelecionado", ano);
        model.addAttribute("marcas", vehicleRepository.listarMarcas());
        model.addAttribute("anos", vehicleRepository.listarAnos());
        model.addAttribute("precoMax", precoMax != null ? precoMax : 2000000);

        return "Visualizarveiculos";
    }

    public static String listVehicles(String termo, String marca, String ano, Double precoMax, Model model, VeiculosRepository vehicleRepository) {

        List<Veiculos> veiculos;

        if (termo != null && !termo.isBlank()) {
            veiculos = vehicleRepository.findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(termo, termo);
        } else if (marca != null && !marca.isBlank() && ano != null && !ano.isBlank() && precoMax != null) {
            veiculos = vehicleRepository.findByMarcaAndAnoAndPrecoLessThanEqual(marca, ano, precoMax);
        } else if (marca != null && !marca.isBlank() && ano != null && !ano.isBlank()) {
            veiculos = vehicleRepository.findByMarcaAndAno(marca, ano);
        } else if (marca != null && !marca.isBlank() && precoMax != null) {
            veiculos = vehicleRepository.findByMarcaAndPrecoLessThanEqual(marca, precoMax);
        } else if (ano != null && !ano.isBlank() && precoMax != null) {
            veiculos = vehicleRepository.findByAnoAndPrecoLessThanEqual(ano, precoMax);
        } else if (marca != null && !marca.isBlank()) {
            veiculos = vehicleRepository.findByMarca(marca);
        } else if (ano != null && !ano.isBlank()) {
            veiculos = vehicleRepository.findByAno(ano);
        } else if (precoMax != null) {
            veiculos = vehicleRepository.findByPrecoLessThanEqual(precoMax);
        } else {
            veiculos = vehicleRepository.findByEstadoNot("vendido");
        }

        model.addAttribute("veiculos", veiculos);
        model.addAttribute("marcas", vehicleRepository.listarMarcas());
        model.addAttribute("anos", vehicleRepository.listarAnos());

        return "veiculos";
    }

    public static String vehicleDetails(Model model, VeiculosRepository vehicleRepository, Integer id){
        Optional<Veiculos> veiculoOpt = vehicleRepository.findById(id);
        if (veiculoOpt.isEmpty()) {
            return "redirect:/veiculos?erro=naoencontrado";
        }

        model.addAttribute("veiculo", veiculoOpt.get());
        return "detalheVeiculo";
    }

    public static String updateVehicles(Integer ID, String marca, String modelo, String categoria, String ano, String cor, Double preco, VeiculosRepository vehicleRepository){
        Optional<Veiculos> optionalVeiculo = vehicleRepository.findById(ID);

        if (optionalVeiculo.isPresent()) {
            Veiculos veiculo = optionalVeiculo.get();
            veiculo.setMarca(marca);
            veiculo.setModelo(modelo);
            veiculo.setCategoria(categoria);
            veiculo.setAno(ano);
            veiculo.setCor(cor);
            veiculo.setPreco(preco);

            vehicleRepository.save(veiculo);
        }

        return "redirect:/Visualizarveiculos";
    }

    public static String changeVehicle(Integer id, Model model, VeiculosRepository vehicleRepository){
        Optional<Veiculos> veiculo = vehicleRepository.findById(id);
        if (veiculo.isPresent()) {
            model.addAttribute("veiculo", veiculo.get());
            return "modificarVeiculo";
        } else {
            return "redirect:/Visualizarveiculos";
        }
    }
}
