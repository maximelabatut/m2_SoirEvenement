package utilisateur.backend.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import utilisateur.backend.controllers.UtilisateurController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    private static final String CURRENT_URI = "http://localhost:8080/utilisateur";
    private static final String TOKEN = "Authorization";
    public static final String LOCATION = "Location";
    public static final String TOKEN_PREFIX = "Bearer ";

    private String publicKey;

    public SecurityInterceptor(String publicKey){ this.publicKey = publicKey;}

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler){
        logger.info("Token : "+httpServletRequest.getHeader(TOKEN));
        if(httpServletRequest.getHeader(TOKEN) == null){
            logger.error("Connexion refusee");
        }else {
            logger.info("Connexion acceptee");
        }
        return true;
        //return httpServletRequest.getHeader(TOKEN) != null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
