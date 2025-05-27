package com.example.vehiclesstore.controller;

import com.example.vehiclesstore.model.Login;
import com.example.vehiclesstore.repository.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.data.web.PageableHandlerMethodArgumentResolverSupport;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    @Autowired
    private VeiculosRepository vehicleRepository;

    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private PageableHandlerMethodArgumentResolverSupport pageableHandlerMethodArgumentResolverSupport;

    @GetMapping(path = "/")
    public String getAllDeps(Model model) {
        model.addAttribute("ListDeps", vehicleRepository.findAll());
        //loginRepository.deleteAll();
        return "index";
    }

    @GetMapping("/registar")
    public String registar() {
        return "registar";
    }

    @Autowired
    private BCryptPasswordEncoder hashPassword;

    @PostMapping("/registar")
    public String registUser(@RequestParam String email, @RequestParam String password,@RequestParam String password2 ){
        if (loginRepository.findByEmail(email) != null){
            return "redirect:/registar?error  -> email";

        }

        if (!password.equals(password2)){
            return "redirect:/registar?pass not match";
        }

        Login user = new Login();
        //user.setID(1);
        user.setEmail(email);
        //BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        //user.setPassword(bc.encode(password));
        user.setPassword(hashPassword.encode(password));
        loginRepository.save(user);
        return "redirect:/";
    }



}