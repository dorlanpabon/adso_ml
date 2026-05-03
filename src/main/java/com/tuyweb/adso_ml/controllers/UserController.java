package com.tuyweb.adso_ml.controllers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tuyweb.adso_ml.dao.UserPublic;
import com.tuyweb.adso_ml.model.User;
import com.tuyweb.adso_ml.repositories.UserRepository;

import java.util.List;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/users")
  String users(Model model,
      @RequestParam(name = "editId", required = false) Long editId) {
    List<UserPublic> users = userRepository.findAll().stream()
        .map(user -> new UserPublic(user.getUserId(), user.getUsername()))
        .toList();
    model.addAttribute("users", users);
    if (editId != null) {
      userRepository.findById(editId)
          .map(user -> new UserPublic(user.getUserId(), user.getUsername()))
          .ifPresent(user -> model.addAttribute("editUser", user));
    }
    return "users";
  }

  @PostMapping("/users/create")
  String createUser(String username, String password) {
    if (username == null || username.isBlank() || password == null || password.isBlank()) {
      return "redirect:/users?error=Missing%20data";
    }
    if (userRepository.findByUsername(username) != null) {
      return "redirect:/users?error=Username%20already%20exists";
    }
    User user = new User();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
    return "redirect:/users?success=User%20created";
  }

  @PostMapping("/users/update")
  String updateUser(Long userId, String username, String password) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      return "redirect:/users?error=User%20not%20found";
    }
    User existing = userRepository.findByUsername(username);
    if (existing != null && !existing.getUserId().equals(userId)) {
      return "redirect:/users?error=Username%20already%20exists";
    }
    user.setUsername(username);
    if (password != null && !password.isBlank()) {
      user.setPassword(passwordEncoder.encode(password));
    }
    userRepository.save(user);
    return "redirect:/users?success=User%20updated";
  }

  @PostMapping("/users/delete")
  String deleteUser(Long userId) {
    userRepository.deleteById(userId);
    return "redirect:/users?success=User%20deleted";
  }

}
