//package com.restaurant.app.services.auth;
//
//
//import com.auth0.jwt.JWT;
//
//import com.auth0.jwt.algorithms.Algorithm;
//
//import com.auth0.jwt.exceptions.JWTVerificationException;
//
//import com.auth0.jwt.interfaces.DecodedJWT;
//
//import com.auth0.jwt.interfaces.JWTVerifier;
//
//import com.nimbusds.jose.jwk.JWK;
//
//import com.nimbusds.jose.jwk.JWKSet;
//
//import com.nimbusds.jose.jwk.RSAKey;
//
//import javax.inject.Inject;
//
//import javax.inject.Singleton;
//
//import java.net.URL;
//
//import java.security.interfaces.RSAPublicKey;
//
//import java.util.HashMap;
//
//import java.util.Map;
//
//@Singleton
//
//public class TokenVerifier {
//
//    private final String region;
//
//    private final String userPoolId;
//
//    private final String issuer;
//
//    @Inject
//
//    public TokenVerifier() {
//
//        this.region = System.getenv("REGION");
//
//        this.userPoolId = System.getenv("COGNITO_ID");  // Set in AWS Lambda Env Vars
//
//        this.issuer = "https://cognito-idp." + region + ".amazonaws.com/" + userPoolId;
//
//    }
//
//    public Map<String, String> verifyTokenAndExtractClaims(String token) {
//        try {
//            DecodedJWT jwt = JWT.decode(token);
//            String kid = jwt.getKeyId();
//
//            // Fetch Cognito public keys
//            Map<String, RSAKey> publicKeys = getCognitoPublicKeys();
//            if (!publicKeys.containsKey(kid)) {
//                throw new JWTVerificationException("Invalid Key ID");
//            }
//
//            // Get the correct RSA Public Key
//            RSAPublicKey publicKey = publicKeys.get(kid).toRSAPublicKey();
//
//            // Create JWT verifier
//            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
//            JWTVerifier verifier = JWT.require(algorithm)
//                    .withIssuer(issuer)
//                    .build();
//
//            DecodedJWT verifiedJwt = verifier.verify(token);
//
//            // Extract claims
//            Map<String, String> claims = new HashMap<>();
//            claims.put("sub", verifiedJwt.getClaim("sub").asString()); // Cognito User ID
//            claims.put("username", verifiedJwt.getClaim("cognito:username").asString()); // Cognito username
//            claims.put("email", verifiedJwt.getClaim("email").asString()); // Email
//            claims.put("first_name", verifiedJwt.getClaim("given_name").asString()); // First Name
//            claims.put("last_name", verifiedJwt.getClaim("family_name").asString()); // Last Name
//
//
//            return claims;
//        } catch (Exception e) {
//            System.err.println("Token verification failed: " + e.getMessage());
//            return null;
//        }
//    }
//
//    private Map<String, RSAKey> getCognitoPublicKeys() throws Exception {
//        String jwksUrl = issuer + "/.well-known/jwks.json";
//        JWKSet jwkSet = JWKSet.load(new URL(jwksUrl));
//        Map<String, RSAKey> keys = new HashMap<>();
//
//        for (JWK jwk : jwkSet.getKeys()) {
//            keys.put(jwk.getKeyID(), (RSAKey) jwk);
//        }
//        return keys;
//    }
//
//
//}
//
//

package com.restaurant.app.services.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * TokenVerifier is responsible for verifying the authenticity of a JWT token
 * using Cognito's public RSA keys. It also extracts user information (claims) from the token.
 *
 * This class uses asymmetric cryptography:
 * - Cognito signs the token using its private key
 * - This class verifies the token using Cognito's public key (fetched from JWKS URL)
 */
@Singleton
public class TokenVerifier {

    private final String region;
    private final String userPoolId;
    private final String issuer;

    /**
     * Constructor that reads environment variables to construct the token issuer URL.
     * REGION and COGNITO_ID are set as environment variables in the AWS Lambda console.
     */
    @Inject
    public TokenVerifier() {
        this.region = System.getenv("REGION");
        this.userPoolId = System.getenv("COGNITO_ID");  // e.g., "us-east-1_xxxxx"
        this.issuer = "https://cognito-idp." + region + ".amazonaws.com/" + userPoolId;
    }

    /**
     * Verifies the provided JWT token using Cognito's public key,
     * and extracts user claims like email, name, sub, etc.
     *
     * @param token JWT access token received from the client (signed by Cognito)
     * @return a map of user claims if token is valid, or null if verification fails
     */
    public Map<String, String> verifyTokenAndExtractClaims(String token) {
        try {
            // Decode the token header to extract the key ID (kid)
            DecodedJWT jwt = JWT.decode(token);
            String kid = jwt.getKeyId();  // 'kid' is used to identify which public key to use

            // Get Cognito's public keys and check if the token's kid is valid
            Map<String, RSAKey> publicKeys = getCognitoPublicKeys();
            if (!publicKeys.containsKey(kid)) {
                throw new JWTVerificationException("Invalid Key ID");
            }

            // Extract the correct public RSA key based on 'kid'
            RSAPublicKey publicKey = publicKeys.get(kid).toRSAPublicKey();

            // Create a JWT verifier using the algorithm RSA256 (RSA with SHA-256)
            Algorithm algorithm = Algorithm.RSA256(publicKey, null);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer) // Ensure the issuer matches Cognito's expected format
                    .build();

            // Verify the token's signature and claims
            DecodedJWT verifiedJwt = verifier.verify(token);

            // Extract useful user claims from the payload
            Map<String, String> claims = new HashMap<>();
            claims.put("sub", verifiedJwt.getClaim("sub").asString());                      // Unique user ID (Cognito UUID)
            claims.put("username", verifiedJwt.getClaim("cognito:username").asString());    // Cognito-assigned username
            claims.put("email", verifiedJwt.getClaim("email").asString());                  // Email address
            claims.put("first_name", verifiedJwt.getClaim("given_name").asString());        // First name
            claims.put("last_name", verifiedJwt.getClaim("family_name").asString());        // Last name

            return claims;

        } catch (Exception e) {
            System.err.println("Token verification failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Fetches the JWKS (JSON Web Key Set) from the Cognito user pool endpoint.
     * These keys are used to verify JWTs that Cognito has signed using its private key.
     *
     * @return a map of key ID (kid) to RSAKey
     * @throws Exception if the JWKS cannot be fetched or parsed
     */
    private Map<String, RSAKey> getCognitoPublicKeys() throws Exception {
        // JWKS URL: standard endpoint for Cognito public keys
        String jwksUrl = issuer + "/.well-known/jwks.json";

        // Load the JWKS set from the URL (parses JSON into key objects)
        JWKSet jwkSet = JWKSet.load(new URL(jwksUrl));
        Map<String, RSAKey> keys = new HashMap<>();

        // Map each key in JWKS by its 'kid' (key ID)
        for (JWK jwk : jwkSet.getKeys()) {
            keys.put(jwk.getKeyID(), (RSAKey) jwk);
        }
        return keys;
    }
}

