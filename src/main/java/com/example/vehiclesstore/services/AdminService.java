package com.example.vehiclesstore.services;

import com.example.vehiclesstore.model.Veiculos;
import com.example.vehiclesstore.repository.VeiculosRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AdminService {

    public static String AdminService(String marca, String modelo, String categoria, String ano, String cor, double preco, MultipartFile imageFile, VeiculosRepository vehicleRepository){
        Veiculos veiculo = new Veiculos();
        veiculo.setMarca(marca);
        veiculo.setModelo(modelo);
        veiculo.setCategoria(categoria);
        veiculo.setAno(ano);
        veiculo.setCor(cor);
        veiculo.setPreco(preco);
        veiculo.setEstado("venda");
        try {
            if (!imageFile.isEmpty()) {
                assert veiculo != null;
                veiculo.setImage(new javax.sql.rowset.serial.SerialBlob(imageFile.getBytes()));
            }
            vehicleRepository.save(veiculo);
            return "redirect:/admColocarVeiculo?success";
        } catch (Exception e) {
            return "redirect:/admColocarVeiculo?error";
        }
    }

    public static String delVehicle(Integer id, VeiculosRepository vehicleRepository){
        vehicleRepository.deleteById(id);
        return "redirect:/Visualizarveiculos";
    }
}
