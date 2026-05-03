package com.tuyweb.adso_ml.dao;

import java.io.Serializable;

public record UserPublic(Long userId, String username) implements Serializable {

  private static final long serialVersionUID = 1L;
}
