package com.quintoimpacto.ecosistema.service.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.quintoimpacto.ecosistema.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();


    @Value("${secret.key}")
    private String secretKey;

    public String generateToken(Usuario usuario){

        return Jwts.builder()
                .issuer("Ecosistema")
                .subject(usuario.getEmail())
                .issuedAt(Date.from(Instant.now().atZone(ZoneId.of("America/Argentina/Buenos_Aires")).toInstant()))
                .claim("nombre", usuario.getNombre())
                .claim("apellido", usuario.getApellido())
                .claim("telefono", usuario.getTelefono())
                .claim("rol", usuario.getRol().name())
                .expiration(Date.from(Instant.now().atZone(ZoneId.of("America/Argentina/Buenos_Aires")).plus(Duration.ofHours(24)).toInstant()))
                .signWith(getSignInKey())
                .compact();
    }

    private SecretKey getSignInKey(){return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public Claims verifyAndGetJwtPayload(String jwt){
        return Jwts.parser()
                .requireIssuer("Ecosistema")
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
    public Payload googleVerifierAndPayload(String googleToken)  {

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(HTTP_TRANSPORT, JSON_FACTORY).build();

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(googleToken);
        } catch (GeneralSecurityException | IOException e) {
            System.out.println("error googletoken");
            System.err.println("Error verifying Google ID token: " + e.getMessage());
        }

        if (idToken == null){
            return null;
        }
        return idToken.getPayload();
    }



}
