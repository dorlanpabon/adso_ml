package com.tuyweb.adso_ml.controllers;

import static com.tuyweb.adso_ml.helpers.Common.isAuthenticated;

import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tuyweb.adso_ml.model.Product;
import com.tuyweb.adso_ml.repositories.ProductRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ProductController {

  private final ProductRepository productRepository;
  @GetMapping("/products")
  String products(HttpSession session, Model model,
      @RequestParam(name = "editId", required = false) Long editId) {
    if (!isAuthenticated(session)) {
      return "redirect:/";
    }
    List<Product> products = productRepository.findAll();
    model.addAttribute("products", products);
    if (editId != null) {
      productRepository.findById(editId)
          .ifPresent(product -> model.addAttribute("editProduct", product));
    }
    return "products";
  }
  @PostMapping("/products/create")
  String createProduct(String name, String description, Double price, HttpSession session) {
    if (!isAuthenticated(session)) {
      return "redirect:/";
    }
    Product product = new Product();
    product.setName(name);
    product.setDescription(description);
    product.setPrice(price);
    productRepository.save(product);
    return "redirect:/products?success=Product%20created";
  }

  @PostMapping("/products/update")
  String updateProduct(Long productId, String name, String description, Double price,
      HttpSession session) {
    if (!isAuthenticated(session)) {
      return "redirect:/";
    }
    Product product = productRepository.findById(productId).orElse(null);
    if (product == null) {
      return "redirect:/products?error=Product%20not%20found";
    }
    product.setName(name);
    product.setDescription(description);
    product.setPrice(price);
    productRepository.save(product);
    return "redirect:/products?success=Product%20updated";
  }

  @PostMapping("/products/delete")
  String deleteProduct(Long productId, HttpSession session) {
    if (!isAuthenticated(session)) {
      return "redirect:/";
    }
    productRepository.deleteById(productId);
    return "redirect:/products?success=Product%20deleted";
  }
}
