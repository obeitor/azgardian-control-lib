package com.softobt.asgardian.control.config;

import com.softobt.core.api.TokenDetail;
import com.softobt.core.exceptions.models.CredentialException;
import com.softobt.asgardian.control.models.AsgardianUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * @author aobeitor
 * @since 6/3/20
 */
@Component
@Primary
public class JWTokenUtil {

    @Value("${asgardian.token.validity-period:1200}")
    private Long tokenValidity;

    @Value("${asgardian.token.secret-key:softobt}")
    private String secretKey;

    @Value("${asgardian.token.issuer:softobt}")
    private String issuerId;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     *
     * @param user
     * @return
     */
    public TokenDetail getToken(AsgardianUser user){
        return doGenerateToken(user,null);
    }

    /**
     *
     * @param user
     * @param additionalInfo
     * @return
     */
    public TokenDetail getToken(AsgardianUser user, Map<String,Object> additionalInfo){
        return doGenerateToken(user,additionalInfo);
    }

    private TokenDetail doGenerateToken(AsgardianUser user, Map<String,Object> claims){
        if(claims==null)
            claims = new HashMap<>();
        Date now = new Date(System.currentTimeMillis());
        Date exp = new Date(System.currentTimeMillis()+(tokenValidity*1000));
        claims.put("username",user.getUsername());
        claims.put("domain",user.getDomain().getName().toUpperCase());
        String issuer = passwordEncoder.encode(generateIssuer(user.getUsername(),user.getDomain().getName(),now.getTime()));
        if(user.getLastLogin()!=null)
            claims.put("lastLoginDate",user.getLastLogin().format(dateTimeFormatter));
        claims.put("userkey",passwordEncoder.encode(user.getPassword()+user.getUsername()+user.getDomain().getName()));
        String token = Jwts.builder().setClaims(claims).setId(user.getUsername()).setSubject(user.getDomain().getName())
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setExpiration(exp)
                .signWith(SignatureAlgorithm.HS512,secretKey).compact();
        TokenDetail detail = new TokenDetail();
        detail.setDomain(user.getDomain().getName());
        detail.setExpiry(exp.getTime());
        detail.setOwner(user.getUsername());
        detail.setToken(token);
        return detail;
    }

    private boolean isTokenExpired(String token)throws CredentialException{
        final Date exp = retrieveClaimFromToken(token,Claims::getExpiration);
        return exp.before(new Date());
    }

    /**
     * Pair
     * <First>Domain</First>
     * <Second>Username</Second>
     * @param token
     * @return
     * @throws Exception
     */
    public Pair<String,String> getLoggedonUser(String token)throws CredentialException{
        validateToken(token);
        final String username = retrieveClaimFromToken(token,Claims::getId);
        final String domain = retrieveClaimFromToken(token,Claims::getSubject);
        return new Pair<>(domain,username);
    }

    private void validateToken(String token)throws CredentialException{
        if(isTokenExpired(token))
            throw new CredentialException(CredentialException.CredentialExceptionType.EXPIRED_TOKEN);
        final String username = retrieveClaimFromToken(token,Claims::getId);
        final String domain = retrieveClaimFromToken(token,Claims::getSubject);
        long issuedAt = retrieveClaimFromToken(token,Claims::getExpiration).getTime();
        String issuer = retrieveClaimFromToken(token,Claims::getIssuer);
        if(passwordEncoder.matches(generateIssuer(username,domain,issuedAt),issuer)) {
            return ;
        }
        throw new CredentialException(CredentialException.CredentialExceptionType.INVALID_TOKEN);
    }

    /**
     *
     * @param token
     * @param field
     * @return
     * @throws Exception
     */
    public Object getTokenProperties(String token, String field)throws CredentialException{
        validateToken(token);
        Map<String, Object> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getOrDefault(field,null);
    }

    private <T> T retrieveClaimFromToken(String token, Function<Claims, T> claimsTFunction)throws CredentialException{
       try {
           final Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
           return claimsTFunction.apply(claims);
       }
       catch (Exception e){
           throw new CredentialException(CredentialException.CredentialExceptionType.INVALID_TOKEN);
       }
    }

    private String generateIssuer(String username, String domain, long time){
        return issuerId+username+domain;
    }
}
