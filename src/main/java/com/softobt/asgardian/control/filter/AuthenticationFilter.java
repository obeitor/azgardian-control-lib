package com.softobt.asgardian.control.filter;

import com.google.common.base.Strings;
import com.softobt.asgardian.control.apimodels.LoggedInUser;
import com.softobt.asgardian.control.config.JWTokenUtil;
import com.softobt.core.exceptions.models.CredentialException;
import javafx.util.Pair;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author aobeitor
 * @since 5/31/20
 */
@Component
@Primary
public class AuthenticationFilter extends HandlerInterceptorAdapter {

    private String clientKey;
    private String allowedUrls;

    private static final String clientKeyHeaderName = "Client-Key";
    private static final String authKeyHeaderName = "Authorization";

    private JWTokenUtil tokenUtil;

    public void setTokenUtil(JWTokenUtil tokenUtil){
        this.tokenUtil = tokenUtil;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public void setAllowedUrls(String allowedUrls) {
        this.allowedUrls = allowedUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String reqClientKey = request.getHeader(clientKeyHeaderName);
        String reqAuthKey = request.getHeader(authKeyHeaderName);
        if(Strings.isNullOrEmpty(reqClientKey) || !clientKey.equals(reqClientKey)){
            throw new CredentialException("Client Key not valid");
        }
        try {
            Pair<String, String> userDomain = validateAuthToken(reqAuthKey);
            request.setAttribute("user",new LoggedInUser(userDomain.getValue(),userDomain.getKey()));
        }
        catch(CredentialException e){
            if(!isPathAllowed(request))
                throw e;
        }
        return super.preHandle(request,response,handler);

    }

    private boolean isPathAllowed(HttpServletRequest request){
        final String incoming = request.getRequestURI();
        for(String url : allowedUrls.split(",")){
            if(url.equalsIgnoreCase(incoming))
                return true;
            if(url.contains("**")){
                String[] s = url.split("\\*\\*");
                if(incoming.startsWith(s[0]) && (s.length==1||(s.length==2&&incoming.endsWith(s[1])) ))
                    return true;
            }
        }
        return false;
    }

    private Pair<String,String> validateAuthToken(String authKey)throws CredentialException{
        if(Strings.isNullOrEmpty(authKey))
            throw new CredentialException("Authorization not found");
        if(!authKey.startsWith("Bearer ")){
            throw new CredentialException("Authorization not bearer type");
        }
        String token = authKey.substring(7);
        return tokenUtil.getLoggedonUser(token);
    }

}
