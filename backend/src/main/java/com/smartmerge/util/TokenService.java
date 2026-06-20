package com.smartmerge.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenService {
    
    @Value("${github.appId}")
    String appId;

    @Value("${github.key}")
    String keyPath;
    
    public String getInstallationToken(String url) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String jwtToken = generateJwt();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/vnd.github+json");
        headers.set("Authorization", "Bearer " + jwtToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            entity,
            String.class
        );
        JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
        return jsonObject.get("token").getAsString();

    }

    public String generateJwt() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        // file contains the key
        // convert lines from file into list and remove the parts not in the key and convert each line to one string
        List<String> keyFileLines = Files.readAllLines(Paths.get(keyPath));
        StringBuilder keyBuilder = new StringBuilder();
        for (int i = 0; i < keyFileLines.size(); i++) {
            if (!keyFileLines.get(i).contains("BEGIN") && !keyFileLines.get(i).contains("END")) {
                keyBuilder.append(keyFileLines.get(i));
            }
        }
        String key = keyBuilder.toString().replace(" ", "");

        // decode to raw bytes
        byte[] pkcs1Bytes = Base64.getDecoder().decode(key);

        // convert to pkcs8 so java can understand
        byte[] pkcs8Bytes = convertPkcs1ToPkcs8(pkcs1Bytes);

        // wrapper required for generatePrivate()
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(pkcs8Bytes);

        // setting the algorithm
        KeyFactory kf = KeyFactory.getInstance("RSA");

        // generating a key with specs and algorithm
        PrivateKey privateKey = kf.generatePrivate(spec);

        Date currentTime = new Date(System.currentTimeMillis());
        return Jwts.builder()
                .setIssuedAt(currentTime)
                .setExpiration(Date.from(currentTime.toInstant().plusSeconds(600)))
                .setIssuer(appId)
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    private byte[] convertPkcs1ToPkcs8(byte[] decodedKey) {
        try(ASN1InputStream inputStream = new ASN1InputStream(decodedKey)) {
            ASN1Sequence sequence = (ASN1Sequence) inputStream.readObject();
            AlgorithmIdentifier algId = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
            PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algId, sequence);
            return privateKeyInfo.getEncoded();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
