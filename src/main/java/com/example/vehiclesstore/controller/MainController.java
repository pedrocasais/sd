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

    @Autowired
    private PageableHandlerMethodArgumentResolverSupport pageableHandlerMethodArgumentResolverSupport;
    //@Autowired
    //private FilterRegistrationBean<ResourceUrlEncodingFilter> resourceUrlEncodingFilter;

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

/*
    @PostMapping("/addImg")
    public String addImage(@RequestParam("image") MultipartFile imageFile) {
        try {
            Veiculos veiculo = vehicleRepository.findById(3).orElse(null);

            if (!imageFile.isEmpty()) {
                assert veiculo != null;
                veiculo.setImage(new javax.sql.rowset.serial.SerialBlob(imageFile.getBytes()));
            }
            vehicleRepository.save(veiculo);
            return "redirect:/addImg?success";
        } catch (Exception e) {
            return "redirect:/addImg?error";
        }
    }
*/

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


    @PostMapping("/atualizarVeiculo")
    public String atualizarVeiculo(@RequestParam int ID, @RequestParam String marca, @RequestParam String modelo, @RequestParam String categoria, @RequestParam String ano, @RequestParam String cor, @RequestParam Double preco) {
        return VehiclesService.updateVehicles(ID, marca, modelo, categoria, ano, cor, preco, vehicleRepository);
    }

    // DIOGO VEICULOS

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


    // Does not change
    @PostMapping("/alterar-password")
    public String alterarPassword(@RequestParam String novaPassword,
                                  @RequestParam String confirmarPassword,
                                  HttpSession session) {

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

    /*
        @GetMapping("/veiculos/imagem/{id}")
        @ResponseBody
        public ResponseEntity<byte[]> obterImagem(@PathVariable int id) {
           return ;
        }
    */
    @GetMapping("/veiculo/{id}")
    public String mostrarDetalhesVeiculo(@PathVariable int id, Model model) {
        return VehiclesService.vehicleDetails(model, vehicleRepository, id);
    }

    @GetMapping("/comprar/{id}")
    public String comprar(@PathVariable int id, HttpSession session, Model model) {
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

    @GetMapping("/faq")
    public String faq(HttpSession s, Model model) {
        Object user = s.getAttribute("email");
        Users users = userRepository.findByEmail(user.toString());

        if (users != null) {
            if (users.getRole().equals("ADMIN")) {
                model.addAttribute("isAdmin", true);
            }
        }
        return "faq";
    }

    @GetMapping("/sobre")
    public String sobre(HttpSession s, Model model) {
        Object user = s.getAttribute("email");
        Users users = userRepository.findByEmail(user.toString());

        if (users != null) {
            if (users.getRole().equals("ADMIN")) {
                model.addAttribute("isAdmin", true);
            }
        }
        return "sobre";
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


    @GetMapping("/recibo")
    public String genPDF() {
        return "";
    }


    @GetMapping("/veiculos/pesquisa")
    public String getCar(@RequestParam String termo, Model model) {
        List<Veiculos> veiculos = vehicleRepository.findByMarcaContainingIgnoreCaseOrModeloContainingIgnoreCase(termo, termo);
        model.addAttribute("veiculos", veiculos);
        return "main";
    }
}