package com.kona.tomcat.demo.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @Description：
 * @Author： zxd0308
 * @Date: Created in 2023/8/18 8:52
 * @Modified By:
 */
public class KeyStoreFactory {
    public static KeyStore createTrustStore(String trustStorePath, String trustStorePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        // 加载包含CA证书的TrustStore
        KeyStore trustStore = KeyStore.getInstance("PKCS12");
        FileInputStream trustStoreInputStream = new FileInputStream(trustStorePath);
        trustStore.load(trustStoreInputStream, trustStorePassword.toCharArray());
        trustStoreInputStream.close();
        return trustStore;
    }
    public static KeyStore createKeyStore(String keyStorePath, String keyStorePassword) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        // 加载包含终端实体证书的KeyStore
        KeyStore keyStore =  KeyStore.getInstance("PKCS12");
        FileInputStream keyStoreInputStream = new FileInputStream(keyStorePath);
        keyStore.load(keyStoreInputStream, keyStorePassword.toCharArray());
        keyStoreInputStream.close();
        return keyStore;
    }
}
