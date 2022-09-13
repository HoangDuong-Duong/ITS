package com.elcom.its.config.model;


import java.io.Serializable;
import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.*;
import javax.persistence.Id;

/**
 * @author hanh
 */
@Entity
@Table(name="event_notifier")
@SuperBuilder
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class EventNotifier implements Serializable {
    @NotNull(message = "Name required and unique")
    @Size(max = 255)
    @Column(unique = true)
    @Id
    private String name;
    
    @NotNull(message = "Ip address required")
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    @Size(max = 20)
    private String ipaddess;
    
    @Min(80)
    private String port;
    
    @NotNull(message = "Address required")
    @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$")
    @Size(max = 20)
    private String addess;
    
    @NotNull(message = "Route required")
    @Size(max = 255)
    private String route;
    
    @Size(max = 255)
    private String apiKey;
    
    @Size(max = 1024)
    private String _token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpaddess() {
        return ipaddess;
    }

    public void setIpaddess(String ipaddess) {
        this.ipaddess = ipaddess;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAddess() {
        return addess;
    }

    public void setAddess(String addess) {
        this.addess = addess;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getToken() {
        return _token;
    }

    public void setToken(String _token) {
        this._token = _token;
    }
}
