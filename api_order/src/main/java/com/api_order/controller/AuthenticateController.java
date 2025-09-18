package com.api_order.controller;

import com.api_order.config.security.FixedJwtGenerator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticateController {

    private FixedJwtGenerator fixedJwtGenerator;
    public AuthenticateController(FixedJwtGenerator fixedJwtGenerator) {
        this.fixedJwtGenerator = fixedJwtGenerator;
    }

    @GetMapping("/generate")
    public ResponseEntity<?> generateToken(){
        try{
            var token = this.fixedJwtGenerator.generateToken("inventory");
            return ResponseEntity.ok().body(
                    Map.of(
                            "token", token,
                            "status", "success",
                            "statusCode", HttpStatus.OK
                    )
            );
        } catch (RuntimeException e) {
            return ResponseEntity.unprocessableEntity().body(
                    Map.of(
                            "message", e.getMessage(),
                            "status", "success",
                            "statusCode", HttpStatus.UNPROCESSABLE_ENTITY
                    )
            );
        }
    }
}
