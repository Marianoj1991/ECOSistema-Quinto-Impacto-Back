package com.quintoimpacto.ecosistema.controller;

import com.quintoimpacto.ecosistema.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

        @GetMapping("/login")
        public ResponseEntity<Void> getUser(HttpServletRequest request, HttpServletResponse response) {
                //Todo:Eliminar los sout
                Authentication auth =SecurityContextHolder.getContext().getAuthentication();

                System.out.println("---------getName---------");
                System.out.println(auth.getName());
                System.out.println("---------getPrincipal---------");
                System.out.println(auth.getPrincipal());
                System.out.println("---------getAuthorities---------");
                System.out.println(auth.getAuthorities());
                System.out.println("---------getCredentials---------");
                System.out.println(auth.getCredentials());
                System.out.println("---------getDetails---------");
                System.out.println(auth.getDetails());
                System.out.println("-----------------------");
                System.out.println("request");
                request.getHeaderNames().asIterator().forEachRemaining(headerName -> {
                        String headerValue = request.getHeader(headerName);
                        System.out.println(headerName + ": " + headerValue);
                });
                System.out.println("-----------------------");
                System.out.println("Response");
                response.getHeaderNames().iterator().forEachRemaining(headerName -> {
                        String headerValue = response.getHeader(headerName);
                        System.out.println(headerName + ": " + headerValue);
                });
                System.out.println("-----------------------");
                return ResponseEntity.status(HttpStatus.OK).build();

        }

}
