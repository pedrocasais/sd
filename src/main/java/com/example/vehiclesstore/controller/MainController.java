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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.web.PageableHandlerMethodArgumentResolverSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.vehiclesstore.model.Users;
import com.example.vehiclesstore.model.Veiculos;
import com.example.vehiclesstore.model.Vendas;
import com.example.vehiclesstore.repository.UsersRepository;
import com.example.vehiclesstore.repository.VeiculosRepository;
import com.example.vehiclesstore.repository.VendasRepository;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.vehiclesstore.model.Veiculos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
    private VendasRepository vendasRepository;

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
    public String listarVeiculos(@RequestParam(required = false) String marca,
                                 @RequestParam(required = false) String ano,
                                 @RequestParam(required = false) Integer precoMax,
                                 Model model) {

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
        model.addAttribute("anos",  vehicleRepository.listarAnos());
        model.addAttribute("precoMax", precoMax != null ? precoMax : 2000000);

        return "Visualizarveiculos";
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
        user.setRole("user");
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
    public String loginUser(@RequestParam String email,
                            @RequestParam String password,
                            Model model,
                            HttpSession session) {

        try {
            SessionController.SessionController(session);
            Users user = userRepository.findByEmail(email);

            if (user == null || !hashPassword.matches(password, user.getPassword())) {
                model.addAttribute("error", "Email ou password incorretos.");
                return "login";
            }

            session.setAttribute("email", user.getEmail());

            // Redirecionamento após login
            String destino = (String) session.getAttribute("redirectAfterLogin");
            if (destino != null) {
                session.removeAttribute("redirectAfterLogin");
                return "redirect:" + destino;
            }

            return "redirect:/" + user.getRole();
        } catch (Exception e) {
            model.addAttribute("error", "Erro interno no login.");
            return "login";
        }
    }


    @GetMapping("/USER")
    public String user() {
        return "user";
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

    @GetMapping("/vendas")
    public String mostrarFormularioVenda(Model model) {
        model.addAttribute("veiculos", vehicleRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        return "vendas";
    }

    @PostMapping("/registar-venda")
    public String registarVenda(@RequestParam int veiculoId,
                                @RequestParam Long userId,
                                @RequestParam double precoVenda,
                                @RequestParam String refPagamento,
                                @RequestParam int nif) {

        Vendas venda = new Vendas();
        venda.setVeiculo(vehicleRepository.findById(veiculoId).orElse(null));
        venda.setUser(userRepository.findById(userId.intValue()).orElse(null));
        venda.setPrecoVenda(precoVenda);
        venda.setRefPagamento(refPagamento);
        venda.setNif(nif);
        venda.setDataVenda(LocalDateTime.now());

        vendasRepository.save(venda);

        return "redirect:/vendas?success";
    }

    @GetMapping("/veiculos")
    public String veiculos(@RequestParam(required = false) String marca,
                           @RequestParam(required = false) String termo,
                           @RequestParam(required = false) String ano,
                           @RequestParam(required = false) Integer precoMax,
                           Model model) {

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

    @GetMapping("/veiculos/imagem/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> obterImagem(@PathVariable int id) {
        Optional<Veiculos> veiculoOpt = vehicleRepository.findById(id);

        if (veiculoOpt.isPresent() && veiculoOpt.get().getImage() != null) {
            try {
                Blob imagemBlob = veiculoOpt.get().getImage();
                byte[] imagemBytes = imagemBlob.getBytes(1, (int) imagemBlob.length());

                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imagemBytes);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/veiculo/{id}")
    public String mostrarDetalhesVeiculo(@PathVariable int id, Model model) {
        Optional<Veiculos> veiculoOpt = vehicleRepository.findById(id);
        if (veiculoOpt.isEmpty()) {
            return "redirect:/veiculos?erro=naoencontrado";
        }

        model.addAttribute("veiculo", veiculoOpt.get());
        return "detalheVeiculo";
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

    @PostMapping("/confirmar-compra/{id}")
    public String confirmarCompra(@PathVariable int id,
                                  @RequestParam String refPagamento,
                                  @RequestParam int nif,
                                  HttpSession session) {

        Object email = session.getAttribute("email");
        if (email == null) {
            return "redirect:/login";
        }

        Optional<Users> userOpt = Optional.ofNullable(userRepository.findByEmail(email.toString()));
        Optional<Veiculos> veiculoOpt = vehicleRepository.findById(id);

        if (userOpt.isEmpty() || veiculoOpt.isEmpty()) {
            return "redirect:/veiculos?erro";
        }

        // Criar venda
        Vendas venda = new Vendas();
        venda.setUser(userOpt.get());
        venda.setVeiculo(veiculoOpt.get());
        venda.setDataVenda(LocalDateTime.now());
        venda.setPrecoVenda(veiculoOpt.get().getPreco());
        venda.setNif(nif);
        venda.setRefPagamento(refPagamento);

        // Atualiza estado do veículo e salva
        Veiculos veiculo = veiculoOpt.get();
        veiculo.setEstado("vendido");
        vehicleRepository.save(veiculo);

        // Guarda a venda
        vendasRepository.save(venda);

        return "redirect:/veiculos?sucesso";
    }

    @GetMapping("/estatisticas")
    public String estatisticas() {
        return "estatisticas";
    }

    @GetMapping("/faq")
    public String faq() {
        return "faq";
    }

    @GetMapping("/sobre")
    public String sobre() {
        return "sobre";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}