package com.tuyweb.adso_ml.helpers;

import java.util.Objects;

import jakarta.servlet.http.HttpSession;

public class Common {

  public static Boolean isAuthenticated(HttpSession session) {
    return Objects.nonNull(session.getAttribute("user"));
  }
}
