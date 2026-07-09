package com.smartmerge.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class TokenService {
    
    @Value("${github.appId}")
    String appId;

    @Value("${github.keyPath}")
    String keyPath;

    private final GithubServiceCaller githubServiceCaller;
    
    public String getInstallationToken(String url) {
        try {
            String jwtToken = generateJwt();
            Map<String, Object> installationTokenData = githubServiceCaller.post(url, jwtToken, null, 
                    new ParameterizedTypeReference<Map<String, Object>>() {});
            return installationTokenData.get("token").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve installation token", e);
        }
    }

    private String generateJwt() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

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

    // try-with-resources closes inputStream even if readObject() throws. IOException propagates to the caller
    private byte[] convertPkcs1ToPkcs8(byte[] decodedKey) throws IOException {
        try(ASN1InputStream inputStream = new ASN1InputStream(decodedKey)) {
            ASN1Sequence sequence = (ASN1Sequence) inputStream.readObject();
            AlgorithmIdentifier algId = new AlgorithmIdentifier(PKCSObjectIdentifiers.rsaEncryption, DERNull.INSTANCE);
            PrivateKeyInfo privateKeyInfo = new PrivateKeyInfo(algId, sequence);
            return privateKeyInfo.getEncoded();
        }
    }
}
