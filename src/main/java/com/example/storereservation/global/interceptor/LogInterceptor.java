package com.example.storereservation.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    public static String LOG_ID = "logId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = UUID.randomUUID().toString(); // uuid 생성
        request.setAttribute(LOG_ID, uuid); //afterCompletion에서 사용

        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;

        }
        log.info("[REQUEST URI : {}] [Handler : {}]", requestURI, handler);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String requestURI = request.getRequestURI();
        String uuid = (String) request.getAttribute(LOG_ID); //preHandle에서 생성한 uuid
        //오류발생시 오류 로그도 찍어주자.
        if (ex != null) {
            log.error("afterCompletion error!!", ex);
        }
    }
}
