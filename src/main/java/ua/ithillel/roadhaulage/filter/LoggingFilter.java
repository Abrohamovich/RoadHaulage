package ua.ithillel.roadhaulage.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoggingFilter implements Filter{

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        log.info("Request: {} {}", req.getMethod(), req.getRequestURI());

        request.getParameterMap().forEach((key, value) -> {
            if (key.equals("password")) {return;}
            log.info("Request param: {} = {}", key, String.join(",", value));
        });

        chain.doFilter(request, response);

        log.info("Request completed: {} {} - Status: {}",
                req.getMethod(),
                req.getRequestURI(),
                res.getStatus());
    }

}
