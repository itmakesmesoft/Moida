package com.ssafy.moida.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * [403 error(Forbidden, 권한 없음) 처리]
 * */

@Component
public class JwtAccessDeniendHandeler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Map<String, Object> errorResult = new HashMap<>();

        errorResult.put("timestamp", LocalDateTime.now().toString());
        errorResult.put("status", 403);
        errorResult.put("error", "Unauthorized");
        errorResult.put("message", "권한 정보가 올바르지 않습니다.");
        String result = objectMapper.writeValueAsString(errorResult);

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(result);

    }
}
