package com.project.befree.security;

import com.google.gson.Gson;
import com.project.befree.dto.MemberDTO;
import com.project.befree.util.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Slf4j
public class JWTCheckFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    public JWTCheckFilter(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String requestURI = request.getRequestURI();
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        if (requestURI.startsWith("/")) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("********************JWTCheckFilter");
        String authValue = request.getHeader("Authorization");
        log.info("authValue:{}",authValue);
        try {
            String accessToken = authValue.substring(7);
            Map<String, Object> claims = jwtUtil.validateToken(accessToken);
            String email = (String) claims.get("email");
            String password = (String) claims.get("password");
            String name = (String) claims.get("name");
            boolean social = (boolean) claims.get("social");
            MemberDTO memberDTO = new MemberDTO(email, password, name, social);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberDTO, password);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("*********************** JWTCheckFilter error");
            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN - JWTCheckFilter"));
            response.setContentType("application/json");
            PrintWriter writer = response.getWriter();
            writer.println(msg);
            writer.close();
        }
    }
}
