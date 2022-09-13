package com.elcom.its.vmsboard.service.impl;

import com.elcom.its.utils.StringUtil;
import com.elcom.its.vmsboard.config.ApplicationConfig;
import com.elcom.its.vmsboard.dto.Response;
import com.elcom.its.vmsboard.model.DisplayDetailDTO;
import com.elcom.its.vmsboard.model.DisplayScript;
import com.elcom.its.vmsboard.model.ScriptMaxPriority;
import com.elcom.its.vmsboard.service.DisplayScriptService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Service
public class DisplayScriptServiceImpl implements DisplayScriptService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayScriptServiceImpl.class);
    private static final String URI = ApplicationConfig.ITS_CORE_ROOT_URL + "/v1.0/its/management/display-script";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public Response getDisplayScript() {
        final String uri = URI;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getDisplayScriptPlan(String scriptBaseId) {
        final String uri = URI + "/display/" +  scriptBaseId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getScriptIsPlaying() {
        final String uri = URI + "/display";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getAllDisplayScript(String baseId) {
        final String uri = URI + "/baseId/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + baseId, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getDisplayScriptByParentId(String id) {
        final String uri = URI + "/parentId/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + id, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getDisplayScriptDefault(String id) {
        final String uri = URI + "/default/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + id, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getDisplayScriptByBoardId(String boardId) {
        final String uri = URI + "/board/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + boardId, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response saveDisplayScript(DisplayScript displayScript) {
        HttpEntity<DisplayScript> requestBody = new HttpEntity<>(displayScript, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI, HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateDisplayScript(DisplayScript displayScript, String pathParam) {
        HttpEntity<DisplayScript> requestBody = new HttpEntity<>(displayScript, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + pathParam, HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteDisplayScript(String route) {
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/" + route, HttpMethod.DELETE,null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getTimeByBoardId(String boardId) {
        final String uri = URI + "/time/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + boardId, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getTimeByBaseAndBoardId(String baseId, String boardId) {
        final String uri = URI + "/time?baseId="+baseId+"&boardId="+boardId;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getDetailScript(String id) {
        final String uri = URI + "/detail/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + id, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response updateDetailScript(DisplayDetailDTO displayDetailDTO, String id) {
        HttpEntity<DisplayDetailDTO> requestBody = new HttpEntity<>(displayDetailDTO, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/detail/" + id, HttpMethod.PUT, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response deleteDetailScript(String id) {
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/detail/" + id, HttpMethod.DELETE, null, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getScriptRunningInBoard(String boardId) {
        final String uri = URI + "/running/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + boardId, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getNewsLetterRunningInBoard(String boardId) {
        final String uri = URI + "/news-letter/running/";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri + boardId, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response getNewsLetterByEventType(String eventType) {
        final String uri = URI + "/news-letter/eventType?eventType="+eventType;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> requestBody = new HttpEntity<>(headers);
        ResponseEntity<Response> result = restTemplate.exchange(uri, HttpMethod.GET, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response saveScriptMaxPriority(ScriptMaxPriority scriptMaxPriority) {
        HttpEntity<ScriptMaxPriority> requestBody = new HttpEntity<>(scriptMaxPriority, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/max-priority", HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }

    @Override
    public Response saveScriptTopPriority(ScriptMaxPriority scriptMaxPriority) {
        HttpEntity<ScriptMaxPriority> requestBody = new HttpEntity<>(scriptMaxPriority, null);
        ResponseEntity<Response> result = restTemplate.exchange(URI + "/top-priority", HttpMethod.POST, requestBody, Response.class);
        Response response = result.getBody();
        return response;
    }
}
