package com.elcom.its.config.service;

public interface TokenService {
    String getAccessToken();

    boolean removeAccessToken();
}
