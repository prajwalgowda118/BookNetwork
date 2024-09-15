package com.scaler.booknetwork.booknetwork.SecurityConfig;
import com.scaler.booknetwork.booknetwork.Repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
//import lombok.var;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@RequiredArgsConstructor

public class JwtService {

    private final TokenRepository tokenRepository;
    private long jwtExpiration=1234455;

    private String secretKey="8xVkr8tq9uKl6vq1ESuMAL//vdQ16iOZIaN8LbA4eM1JxPLHtX5NP6+XhN1pZm8z";

    public String generateToken( UserDetails userDetails) {

        return generateToken(new HashMap<>(),userDetails);
    }

    public String generateToken(Map<String,Object> claims, UserDetails userDetails) {
        return buildToken(claims,userDetails,jwtExpiration);
    }

    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long jwtExpiration) {

        var authorities = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .claim("authorities", authorities)
                .signWith(getSignInKey())
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username=extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());

    }

    private Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims,T> claimsResolver) {

        final Claims claims=extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token,Claims::getSubject);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);

    }

}
