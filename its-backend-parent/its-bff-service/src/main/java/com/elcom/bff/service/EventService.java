package com.elcom.bff.service;

import com.elcom.bff.dto.Response;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface EventService {

    public Response getHistory(String urlParam, Map<String,String> headerParam, String pathParam);
}
