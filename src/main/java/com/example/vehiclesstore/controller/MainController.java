package com.example.vehiclesstore.controller;

import com.example.vehiclesstore.model.*;
import com.example.vehiclesstore.services.*;
import com.example.vehiclesstore.repository.UsersRepository;
import com.example.vehiclesstore.repository.VeiculosRepository;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.data.web.PageableHandlerMethodArgumentResolverSupport;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.vehiclesstore.model.Vendas;
import com.example.vehiclesstore.repository.VendasRepository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.util.Random;
import java.util.stream.Collectors;

/**
 * Os produtos devem ser agrupados em categorias, de forma a ser disponibilizado um
 * catálogo de produtos estruturado.
 * <p>
 * Deverá permitir atualizar os produtos que vende; permitir vários tipos de consultas; fazer a gestão de vendas e dos stocks existentes.
 * Os clientes podem fazer compras online.
 * <p>
 * A um cliente, deve ser emitida uma fatura. Suponha que a fatura será paga quando da entrega do produto.
 * Ser possível obter informação sobre o funcionamento da loja: Quais os produtos mais/menos vendidos?
 * Quais os melhores clientes? Qual o valor faturado no dia/semana/mês? …
 * Além das funcionalidades da aplicação devem ser tratados os aspetos de:
 * - gestão de sessões na interação de cada utilizador com a aplicação;
 * - segurança na gestão de sessões, na introdução de dados, e outros aspetos que considere importantes.
 */


@Controller // This means that this class is a Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private VeiculosRepository vehicleRepository;

    @Autowired
    private VendasRepository vendasRepository;

    @Autowired
    private UsersRepository userRepository;


    @GetMapping(path = "/")
    public String getAllDeps(Model model, HttpSession s) {
        return HomeService.home(model, vehicleRepository);
    }

    @Autowired
    private BCryptPasswordEncoder hashPassword;

    @GetMapping("/registar")
    public String registar() {
        return "registar";
    }


    // DIOGO VEICULOS
    // Criar novo veiculo vindo do forms
    @GetMapping("/admColocarVeiculo")
    public String mostrarFormularioVeiculo(Model model) {
        model.addAttribute("veiculo", new Veiculos());
        return "admColocarVeiculo";
    }

    // POST para enviar do forms para a Base de Dados
    @PostMapping("/admColocarVeiculo")
    public String salvarVeiculo(@RequestParam String marca, @RequestParam String modelo, @RequestParam String categoria, @RequestParam String ano, @RequestParam String cor, @RequestParam double preco
            , @RequestParam("image") MultipartFile imageFile) {
        return AdminService.AdminService(marca, modelo, categoria, ano, cor, preco, imageFile, vehicleRepository);
    }

    @GetMapping("/eliminarVeiculo")
    public String eliminar(@RequestParam Integer id) {
        return AdminService.delVehicle(id, vehicleRepository);
    }

    @GetMapping("/Visualizarveiculos")
    public String mostrarVeiculos(Model model) {
        return VehiclesService.VehiclesService(vehicleRepository, model);
    }

    @PostMapping("/Visualizarveiculos")
    public String listarVeiculos(@RequestParam(required = false) String marca, @RequestParam(required = false) String ano, @RequestParam(required = false) double precoMax, Model model) {
        return VehiclesService.listVehiclesFilter(marca, ano, precoMax, model, vehicleRepository);
    }

    @GetMapping("/modificar")
    public String modificar() {
        return "modificar";
    }

    @GetMapping("/eliminar")
    public String eliminar() {
        return "eliminar";
    }

    @GetMapping("/modificarVeiculo")
    public String editarVeiculo(@RequestParam("id") Integer id, Model model) {
        return VehiclesService.changeVehicle(id, model, vehicleRepository);
    }

    // Does not change
    @GetMapping("/estatisticas")
    public String mostrarEstatisticas(Model model, HttpSession s) {
        return EstatisticasService.showStats(model, s, userRepository, vendasRepository);
    }


    @PostMapping("/atualizarVeiculo")
    public String atualizarVeiculo(@RequestParam int ID, @RequestParam String marca, @RequestParam String modelo, @RequestParam String categoria, @RequestParam String ano, @RequestParam String cor, @RequestParam Double preco) {
        return VehiclesService.updateVehicles(ID, marca, modelo, categoria, ano, cor, preco, vehicleRepository);
    }

    @PostMapping("/registo")
    public String registUser(@RequestParam String email, @RequestParam String password, @RequestParam String password2, @RequestParam String nome,
                             @RequestParam String apelido, @RequestParam String codPostal, @RequestParam Long tel, @RequestParam String morada,
                             @RequestParam String localidade, HttpSession s) {

        return Registo.registo(email, apelido, codPostal, localidade, password, password2, nome, tel, morada, s, userRepository, hashPassword);

    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @GetMapping("/USER")
    public String user() {
        return "user";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {

        return ProfileServices.Perfil(model, session, userRepository, vendasRepository);

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // encerra a sessão
        return "redirect:/"; // volta à página de login
    }

    // Does not change
    @GetMapping("/logo")
    public String logo(HttpSession s) {
        return HomeService.logoAction(s, userRepository);
    }


    // Does not change
    @PostMapping("/alterar-password")
    public String alterarPassword(@RequestParam String novaPassword,
                                  @RequestParam String confirmarPassword,
                                  HttpSession session) {
        return ProfileServices.changePassword(session,userRepository ,novaPassword, confirmarPassword, hashPassword);
    }

    @GetMapping("/vendas")
    public String mostrarFormularioVenda(Model model) {
        return VendasService.showForms(model, vehicleRepository, userRepository);
    }

    @PostMapping("/confirmar-compra/{id}")
    public String confirmarCompra(@PathVariable int id, @RequestParam int nif, HttpSession session) {

        return VendasService.confirmSell(id, nif, session, userRepository, vehicleRepository, vendasRepository);
    }

    @GetMapping("/veiculos")
    public String veiculos(@RequestParam(required = false) String marca, @RequestParam(required = false) String termo, @RequestParam(required = false) String ano,
                           @RequestParam(required = false) Double precoMax, Model model) {

        return VehiclesService.listVehicles(termo, marca, ano, precoMax, model, vehicleRepository);
    }


    @GetMapping("/veiculo/{id}")
    public String mostrarDetalhesVeiculo(@PathVariable int id, Model model) {
        return VehiclesService.vehicleDetails(model, vehicleRepository, id);
    }

    @GetMapping("/comprar/{id}")
    public String comprar(@PathVariable int id, HttpSession session, Model model) {
       return VendasService.confirmCompra(session, vehicleRepository, id, model);
    }

    @GetMapping("/faq")
    public String faq(HttpSession s, Model model) {
        return HomeService.faqDetails(s, userRepository, model);
    }

    @GetMapping("/sobre")
    public String sobre(HttpSession s, Model model) {
        return HomeService.aboutUsDetails(s, userRepository, model);
    }

    @GetMapping("/ADMIN")
    public String admin() {
        return "admin";
    }

    @GetMapping("/image")
    public String image() {
        return "image";
    }

    @GetMapping("/addImg")
    public String mostrarFormularioImagem() {
        return "image";
    }


    @GetMapping(value = "/veiculo/imagem/{id}", produces = "image/jpeg")
    @ResponseBody
    public byte[] mostrarImagem(@PathVariable int id) throws Exception {
        Veiculos veiculo = vehicleRepository.findById(id).orElse(null);
        if (veiculo != null && veiculo.getImage() != null) {
            return veiculo.getImage().getBytes(1, (int) veiculo.getImage().length());
        }
        return new byte[0];
    }


    @GetMapping("/recibo-txt/{idVenda}")
    public ResponseEntity<byte[]> gerarReciboTxt(@PathVariable int idVenda) {
       return VendasService.faturaGenerator(vendasRepository, idVenda);
    }


    @GetMapping("/veiculos/pesquisa")
    public String getCar(@RequestParam String termo, Model model) {
        List<Veiculos> veiculos = vehicleRepository.findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(termo, termo);
        model.addAttribute("veiculos", veiculos);
        return "main";
    }
}