package soiree.backend.filters;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityInterceptor implements HandlerInterceptor {

    private static final String AUTHENTIFICATION_URI = "http://localhost:8080/authentification/";
    private static final String TOKEN = "Authorization";
    public static final String LOCATION = "Location";
    public static final String TOKEN_PREFIX = "Bearer ";

    private String publicKey;

    public SecurityInterceptor(String publicKey){ this.publicKey = publicKey;}

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler){
        return httpServletRequest.getHeader(TOKEN) != null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
