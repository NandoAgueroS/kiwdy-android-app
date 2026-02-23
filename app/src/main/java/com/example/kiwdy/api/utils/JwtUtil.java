package com.example.kiwdy.api.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

public class JwtUtil {

    public static String obtenerRol(String token){
        try{
            DecodedJWT jwt = JWT.decode(token);
            Claim rolClaim = jwt.getClaim("http://schemas.microsoft.com/ws/2008/06/identity/claims/role");
            return rolClaim.asString();
        } catch (Exception e) {
           e.printStackTrace();
           return "";
        }
    }

    public static String obtenerId(String token){
        try{
            DecodedJWT jwt = JWT.decode(token.replace("Bearer ", ""));
            Claim idClaim = jwt.getClaim("http://schemas.xmlsoap.org/ws/2005/05/identity/claims/name");
            return idClaim.asString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

}
