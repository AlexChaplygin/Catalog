package ru.reksoft.chaplygin.catalog.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;
import ru.reksoft.chaplygin.catalog.security.TokenService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT Filter configuration
 */
@Configuration
public class JWTFilter extends GenericFilterBean {

    private TokenService tokenService;

    JWTFilter() {
        this.tokenService = new TokenService();
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String token = request.getHeader("Authorization");

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.sendError(HttpServletResponse.SC_OK, "success");
            return;
        }

        if (allowRequestWithoutToken(request)) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(req, res);
        } else {
            if (token == null || !tokenService.isTokenValid(token)) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                String userName = tokenService.getUserNameFromToken(token);
                request.setAttribute("userName", userName);
                filterChain.doFilter(req, res);

            }
        }
    }

    public boolean allowRequestWithoutToken(HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        if (request.getRequestURI().contains("/get") ||
                request.getRequestURI().equals("/") ||
                request.getRequestURI().equals("/index.html") ||
                request.getRequestURI().contains("js") ||
                request.getRequestURI().contains("css") ||
                request.getRequestURI().contains("/users") ||
                request.getRequestURI().contains("/save") ||
                request.getRequestURI().contains("/favicon.ico") ||
                request.getRequestURI().contains("/sectors")
                ) {
            return true;
        }
        return false;
    }
}