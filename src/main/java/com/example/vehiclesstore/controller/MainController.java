package com.example.vehiclesstore.controller;

import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.model.Veiculos;
import com.example.vehiclesstore.repository.UsersRepository;
import com.example.vehiclesstore.repository.VeiculosRepository;
import com.example.vehiclesstore.services.SessionController;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.web.PageableHandlerMethodArgumentResolverSupport;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * Os produtos devem ser agrupados em categorias, de forma a ser disponibilizado um
 * catálogo de produtos estruturado.
 *
 * Deverá permitir atualizar os produtos que vende; permitir vários tipos de consultas; fazer a gestão de vendas e dos stocks existentes.
 * Os clientes podem fazer compras online.
 *
 * A um cliente, deve ser emitida uma fatura. Suponha que a fatura será paga quando da entrega do produto.
 * Ser possível obter informação sobre o funcionamento da loja: Quais os produtos mais/menos vendidos?
 *                                                              Quais os melhores clientes? Qual o valor faturado no dia/semana/mês? …
 * Além das funcionalidades da aplicação devem ser tratados os aspetos de:
 *      - gestão de sessões na interação de cada utilizador com a aplicação;
 *      - segurança na gestão de sessões, na introdução de dados, e outros aspetos que considere importantes.
 */


@Controller // This means that this class is a Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);
    @Autowired
    private VeiculosRepository vehicleRepository;


    @Autowired
    private UsersRepository userRepository;

    @Autowired
    private PageableHandlerMethodArgumentResolverSupport pageableHandlerMethodArgumentResolverSupport;

    @GetMapping(path = "/")
    public String getAllDeps(Model model, HttpSession s) {

        /**
         * verifica se tem "sessão",
         *      se sim -> mantem
         *      se não -> cria uma nova sessão
         */
        SessionController.SessionController(s);

        System.out.println(s);
        model.addAttribute("ListDeps", vehicleRepository.findAll());
        //loginRepository.deleteAll();
        //userRepository.deleteAll();
        return "main";
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
    public String salvarVeiculo(@ModelAttribute Veiculos veiculo) {
        vehicleRepository.save(veiculo);
        return "redirect:/admColocarVeiculo?success";
    }

    @GetMapping("/eliminarVeiculo")
    public String eliminar(@RequestParam Integer id) {
        vehicleRepository.deleteById(id);
        return "redirect:/Visualizarveiculos";
    }

    @GetMapping("/Visualizarveiculos")
    public String listarVeiculos(@RequestParam (required = false) String marca, @RequestParam (required = false) String ano, Model model) {
        List<Veiculos> veiculos = (List<Veiculos>) vehicleRepository.findAll();
        model.addAttribute("veiculos", veiculos);

        if (marca != null && !marca.isEmpty() && ano != null && !ano.isEmpty()) {
            veiculos = vehicleRepository.findByMarcaAndAno(marca, ano);
        } else if (marca != null && !marca.isEmpty()) {
            veiculos = vehicleRepository.findByMarca(marca);
        } else if (ano != null && !ano.isEmpty()) {
            veiculos = vehicleRepository.findByAno(ano);
        } else {
            veiculos = vehicleRepository.findAll();
        }

        List<String> marcas = new ArrayList<>();
        marcas.add("Ferrari");
        marcas.add("Volkswagen");
        marcas.add("BMW");
        marcas.add("Toyota");

        List<String> anos = new ArrayList<>();
        anos.add("2025");
        anos.add("2015");
        anos.add("2010");
        anos.add("2005");

        model.addAttribute("marcaSelecionada", marca);
        model.addAttribute("anoSelecionado", ano);

        model.addAttribute("marcas", marcas);
        model.addAttribute("anos", anos);

        return "Visualizarveiculos";
    }

    // DIOGO VEICULOS

    @PostMapping("/addUser")
    public String registUser(@RequestParam String email, @RequestParam String password,@RequestParam String password2,@RequestParam String nome, @RequestParam Long tel, @RequestParam String morada ){
        if (userRepository.findByEmail(email) != null){
            return "redirect:/registar?error  -> email";
        }

        if (!password.equals(password2)){
            return "redirect:/registar?pass not match";
        }

        Users user = new Users();
        //user.setID(1);
        user.setEmail(email);
        //BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        //user.setPassword(bc.encode(password));
        user.setPassword(hashPassword.encode(password));
        user.setRole("user");
        user.setNome(nome);
        user.setMorada(morada);
        user.setNumTelemovel(tel);

        userRepository.save(user);
        System.out.println("added!");

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/checkUser")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model, HttpSession s){

        SessionController.SessionController(s);

        Users user = userRepository.findByEmail(email);

        if (user == null){
            model.addAttribute("error", "email errada");
            return "login";
        }
        //System.out.println("sda-<  "+ user.getPassword());


        if (hashPassword.matches(password,user.getPassword())){
            System.out.println("logged! ");
            //System.out.println("id -> "+id);

            //System.out.println("wasadbias +.> "+user1.getEmail());
            String role = user.getRole();
            System.out.println("role -> "+role);
            return "redirect:/"+role;
        }
        else {
            model.addAttribute("error", "pass errada");
            return "login";
        }
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}