package com.OnedayOwner.server.global.security;

import com.OnedayOwner.server.global.utils.FileUtils;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import com.auth0.jwt.interfaces.RSAKeyProvider;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Objects;

@Component
public class FileRsaKeyProvider implements RSAKeyProvider{
    private RSAPrivateKey privateKey;
    private RSAPublicKey publicKey;
    private final String ALGORITHM_NAME = "RSA";

    @SneakyThrows({NoSuchAlgorithmException.class})
    @PostConstruct
    public void makeKeySet() throws InvalidKeySpecException {//임시 추가
        this.privateKey = createPrivateKey();
        this.publicKey = createPublicKey();
    }

    private RSAPrivateKey createPrivateKey() throws NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM_NAME);
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("private_key.pem")) {

            String privateKey = FileUtils.streamToString(
                    new InputStreamReader(
                            Objects.requireNonNull(inputStream, "private_key.pem file is not found"),
                            StandardCharsets.UTF_8
                    )
            );

            privateKey = privateKey.replace("-----BEGIN PRIVATE KEY-----", "");
            privateKey = privateKey.replace("-----END PRIVATE KEY-----", "");
            privateKey = privateKey.replaceAll("\\n", "");

            byte[] decodePrivateKey = Base64.getDecoder().decode(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodePrivateKey);
            return (RSAPrivateKey) kf.generatePrivate(keySpec);

        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("private_key.pem is invalid to make RSAPublicKey object",
                    e.getCause());
        } catch (IOException e) {
            throw new RuntimeException("cannot close InputStream for private_key.pem",
                    e.getCause());
        }
    }

    private RSAPublicKey createPublicKey() throws NoSuchAlgorithmException {
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM_NAME);
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("public_key.pem")) {

            String publicKey = FileUtils.streamToString(new InputStreamReader(
                    Objects.requireNonNull(inputStream, "public_key.pem file is not found"),
                    StandardCharsets.UTF_8
            ));

            publicKey = publicKey.replace("-----BEGIN PUBLIC KEY-----", "");
            publicKey = publicKey.replace("-----END PUBLIC KEY-----", "");
            publicKey = publicKey.replaceAll("\\n", "");

            byte[] decodedPublicKey = Base64.getDecoder().decode(publicKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedPublicKey);

            return (RSAPublicKey) kf.generatePublic(keySpec);

        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("public_key.pem is invalid to make RSAPublicKey object",
                    e.getCause());
        } catch (IOException e) {
            throw new RuntimeException("cannot close InputStream for public_key.pem", e.getCause());
        }
    }

    @Override
    public RSAPublicKey getPublicKeyById(String keyId) {
        return this.publicKey;
    }

    @Override
    public RSAPrivateKey getPrivateKey() {
        return this.privateKey;
    }

    @Override
    public String getPrivateKeyId() {
        return null;
    }
}
