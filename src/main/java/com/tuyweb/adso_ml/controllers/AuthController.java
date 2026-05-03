package com.tuyweb.adso_ml.controllers;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.tuyweb.adso_ml.model.User;
import com.tuyweb.adso_ml.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @GetMapping("/")
  String index() {
    return "index";
  }

  @GetMapping("/register")
  String register() {
    return "register";
  }

  @PostMapping("/register")
  String register(String username, String password) {
    if (userRepository.findByUsername(username) != null) {
      return "redirect:/?error=Username%20already%20exists";
    }
    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
    return "redirect:/?success=Registration%20successful";
  }

  @PostMapping("/login")
  String login(String username, String password, HttpSession session) {
    User user = userRepository.findByUsername(username);
    if (user != null && passwordEncoder.matches(password, user.getPassword())) {
      session.setAttribute("user", user);
      return "redirect:/products";
    }
    return "redirect:/";
  }

  @PostMapping("/logout")
  String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/?success=Session%20closed";
  }
}