package com.example.vehiclesstore.controller;

import com.example.vehiclesstore.model.Login;
import com.example.vehiclesstore.repository.LoginRepository;
import com.example.vehiclesstore.repository.VeiculosRepository;
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