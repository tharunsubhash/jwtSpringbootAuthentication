package com.security.jwtExample.controller;

import com.security.jwtExample.jwtutil.JwtUtil;
import com.security.jwtExample.models.AuthenticationRequest;
import com.security.jwtExample.models.AuthenticationResponse;
import com.security.jwtExample.userdetails.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/hello")
    public String getHello() {
        String hello = "Hello";
        return hello;
    }

    @RequestMapping(path = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> getAuthenticated(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUserName(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException exception) {
            throw new Exception("Incorrect Username or Password",exception);
        }

        final UserDetails userDetails = userDetailsService
                                    .loadUserByUsername(authenticationRequest.getUserName());

        return ResponseEntity.ok(new AuthenticationResponse(jwtUtil.generateToken(userDetails)));
    }
}
