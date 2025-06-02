package com.example.vehiclesstore.services;

import jakarta.servlet.http.HttpSession;

import java.util.UUID;

public class SessionController {

    public static void SessionController(HttpSession session) {
        String userID = (String) session.getAttribute("userID");

        if(userID== null){
            userID = "guest-" + UUID.randomUUID();
            session.setAttribute("userID", userID);
            System.out.println(session);
        }else{
            System.out.println(session);
        }
    }
}
