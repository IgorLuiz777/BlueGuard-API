package br.com.blue.guard.api.model.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
    
}
