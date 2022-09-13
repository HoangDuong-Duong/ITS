package com.elcom.its.vds.service;

public interface TokenService {
    String getAccessToken();

    boolean removeAccessToken();
}
