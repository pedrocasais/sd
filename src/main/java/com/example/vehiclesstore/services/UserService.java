package com.example.vehiclesstore.services;

import com.example.vehiclesstore.repository.UsersRepository;
import com.example.vehiclesstore.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UsersRepository userRepository;

    public UserService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = userRepository.findByEmail(username);

        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();

        // For multiple roles:
        // List<String> roles = user.getRoles().stream().map(Role::getName).toArray(String[]::new);
        // return User.withUsername(user.getUsername()).password(user.getPassword()).roles(roles).build();
    }
}