package com.sparta.write.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.write.dto.SecurityExceptionDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j//이번에는 토큰이 request Header에 있냐 없냐로 분기처리를 해줘서 로직을 진행(?)
@RequiredArgsConstructor//게시글 작성같은 인증이 필요한 요청들이 들어왔을 때 토큰이 있는지 없는지 확인을 해서인증처리를 하는 부분으로 로직이 바뀌어져 있음(?)
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    //전에는 request에 있는 Username과 패스워드를 가지고 와서 여기 자체에서 인증,인가를 전부 했음
        String token = jwtUtil.resolveToken(request);

        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                jwtExceptionHandler(response, "Token Error", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(token);//토큰에서 유저의 정보를 가지고 옴
            setAuthentication(info.getSubject());//setAuthentication에 info.getSubject()즉 Username을 넣어줌(?)
        }
        filterChain.doFilter(request,response);
    }//filterChain을 이용해 다음필터로 이동(?)//인증이 필요없는 부분은 필요없게 해야함(?)

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = jwtUtil.createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }//SecurityContext를 만들어서 Authentication인증객체를 넣은 다음에 SecurityContextHolder안에 넣어줌(?)


    //jwtExceptionHandler가 실행되고 response와 메세지 statusCode값을 파라미터로 주며 그것을 사용해 Client로 반환(?)
    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            //SecurityExceptionDto로 객체를 만들고 ObjectMapper를 통해 변환을 하고 Client로 보내줌
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }//Client로 반환(?)(오류가 생기면 Client로 반환(?))


}