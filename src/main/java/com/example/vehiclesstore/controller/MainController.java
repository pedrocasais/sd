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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

import java.util.Optional;


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
        //model.addAttribute("ListDeps", vehicleRepository.findAll());
        Random r = new Random();
        int n = (int)vehicleRepository.count();
        System.out.println("sdas _> "+n);
        List<Veiculos> listaVeiculos = new ArrayList<>();

        for(int i = 0; i < 8; i++) {
            Veiculos veiculo = vehicleRepository.findByID(r.nextInt(n));
            if (veiculo != null) {
                listaVeiculos.add(veiculo);
            }
        }

        model.addAttribute("veiculos", listaVeiculos);
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
    public String salvarVeiculo(@RequestParam String marca, @RequestParam String modelo, @RequestParam String categoria, @RequestParam String ano, @RequestParam String cor, @RequestParam int preco
    ,@RequestParam("image") MultipartFile imageFile) {
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


    @PostMapping("/addImg")
    public String addImage( @RequestParam("image") MultipartFile imageFile) {
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


    @GetMapping("/eliminarVeiculo")
    public String eliminar(@RequestParam Integer id) {
        vehicleRepository.deleteById(id);
        return "redirect:/Visualizarveiculos";
    }

    @GetMapping("/Visualizarveiculos")
    public String mostrarVeiculos(Model model) {
        List<Veiculos> veiculos = vehicleRepository.findAll();

        model.addAttribute("veiculos", veiculos);
        model.addAttribute("marcaSelecionada", "");
        model.addAttribute("anoSelecionado", "");
        model.addAttribute("marcas", vehicleRepository.listarMarcas());
        model.addAttribute("anos", vehicleRepository.listarAnos());
        model.addAttribute("precoMax", 2000000);

        return "Visualizarveiculos";
    }

    @PostMapping("/Visualizarveiculos")
    public String listarVeiculos(@RequestParam(required = false) String marca, @RequestParam(required = false) String ano, @RequestParam(required = false) Integer precoMax, Model model) {

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

    @GetMapping("/modificar")
    public String modificar(){
        return "modificar";
    }

    @GetMapping("/eliminar")
    public String eliminar(){
        return "eliminar";
    }

    @GetMapping("/modificarVeiculo")
    public String editarVeiculo(@RequestParam("id") Integer id, Model model) {
        Optional<Veiculos> veiculo = vehicleRepository.findById(id);
        if (veiculo.isPresent()) {
            model.addAttribute("veiculo", veiculo.get());
            return "modificarVeiculo";
        } else {
            return "redirect:/Visualizarveiculos";
        }
    }

    @PostMapping("/atualizarVeiculo")
    public String atualizarVeiculo(@RequestParam int ID, @RequestParam String marca, @RequestParam String modelo, @RequestParam String categoria, @RequestParam String ano, @RequestParam String cor, @RequestParam int preco
    ) {
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

    // DIOGO VEICULOS

    @PostMapping("/addUser")
    public String registUser(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String password2,
            @RequestParam String nome,
            @RequestParam Long tel,
            @RequestParam String morada,
            HttpSession session
    ) {
        if (userRepository.findByEmail(email) != null) {
            return "redirect:/registar?error=email";
        }

        if (!password.equals(password2)) {
            return "redirect:/registar?error=pass";
        }

        Users user = new Users();
        user.setEmail(email);
        user.setPassword(hashPassword.encode(password));
        user.setRole("use");
        user.setNome(nome);
        user.setMorada(morada);
        user.setNumTelemovel(tel);

        userRepository.save(user);

        // ✅ Inicia sessão automaticamente
        session.setAttribute("email", user.getEmail());

        return "redirect:/USER"; // ou "/perfil" se preferir
    }


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/checkUser")
    public String loginUser(@RequestParam String email, @RequestParam String password, Model model, HttpSession s) {

        try {
            SessionController.SessionController(s);

            Users user = userRepository.findByEmail(email);

            if (user == null) {
                model.addAttribute("error", "Email não encontrado.");
                return "login";
            }

            if (hashPassword.matches(password, user.getPassword())) {
                s.setAttribute("email", user.getEmail());
                return "redirect:/" + user.getRole();
            } else {
                model.addAttribute("error", "Password incorreta.");
                return "login";
            }

        } catch (Exception e) {
            model.addAttribute("error", "Erro interno no login.");
            return "login";
        }
    }

    @GetMapping("/USER")
    public String user() {
        return "USER";
    }

    @GetMapping("/perfil")
    public String perfil(HttpSession session, Model model) {
        Object userEmail = session.getAttribute("email"); // ou use SessionController

        if (userEmail == null) {
            return "redirect:/login"; // Proteção extra
        }

        Users user = userRepository.findByEmail(userEmail.toString());
        model.addAttribute("user", user);
        return "perfil";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // encerra a sessão
        return "redirect:"; // volta à página de login
    }

    @PostMapping("/alterar-password")
    public String alterarPassword(@RequestParam String novaPassword,
                                  @RequestParam String confirmarPassword,
                                  HttpSession session) {

        Object email = session.getAttribute("email");

        if (email == null || !novaPassword.equals(confirmarPassword)) {
            System.out.println("Erro: Email ausente ou passwords não coincidem");
            return "redirect:/perfil?error";
        }

        Users user = userRepository.findByEmail(email.toString());

        if (user == null) {
            System.out.println("Erro: Utilizador não encontrado");
            return "redirect:/perfil?error";
        }

        String hashed = hashPassword.encode(novaPassword);
        user.setPassword(hashed);
        userRepository.save(user);

        System.out.println("Nova password (hashed): " + hashed);
        return "redirect:/perfil?success";
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

    @GetMapping("/estatisticas")
    public String stats() {
        return "estatisticas";
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

}