package com.kona.tomcat.demo.config;


import com.kona.tomcat.demo.TkssTomcat.KonaConnector;
import com.kona.tomcat.demo.TkssTomcat.KonaSSLHostConfig;
import com.tencent.kona.pkix.PKIXInsts;
import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

/**
 * @Description：
 * @Author： zxd0308
 * @Date: Created in 2023/7/14 16:42
 * @Modified By:
 */
@Configuration
public class TomcatConfig {

    @Autowired
    private AppConfig appConfig;

    /**
     * 编程方式实现springboot的http
     *
     * @return
     */
    @Bean
    public TomcatServletWebServerFactory webServerFactory() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                //安全约束
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };

        // KONA设置
        tomcat.addAdditionalTomcatConnectors(httpsConnector(appConfig));
        return tomcat;
    }

    private Connector httpsConnector(AppConfig appConfig)
            throws CertificateException, KeyStoreException, IOException,
            NoSuchAlgorithmException {
        KonaConnector connector = new KonaConnector(
                TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("https");
        connector.setProperty("SSLEnabled", Boolean.toString(appConfig.isSslEnabled()));
        connector.setPort(9010);
        connector.setSecure(false);
        connector.setRedirectPort(appConfig.getPort());

        SSLHostConfig sslConfig = new KonaSSLHostConfig();
        SSLHostConfigCertificate certConfig = new SSLHostConfigCertificate(
                sslConfig, SSLHostConfigCertificate.Type.EC);
        certConfig.setCertificateKeystore(createKeyStore(
                appConfig.getKeyStoreType(), appConfig.getKeyStorePath(),
                appConfig.getKeyStorePassword().toCharArray()));
        certConfig.setCertificateKeystorePassword(appConfig.getKeyStorePassword());
        sslConfig.addCertificate(certConfig);
        sslConfig.setTrustStore(createKeyStore(
                appConfig.getTrustStoreType(), appConfig.getTrustStorePath(),
                appConfig.getTrustStorePassword().toCharArray()));
        connector.addSslHostConfig(sslConfig);

        return connector;
    }
    private static KeyStore createKeyStore(
            String storeType, String storePath, char[] password)
            throws KeyStoreException, IOException, CertificateException,
            NoSuchAlgorithmException {
        KeyStore keyStore = PKIXInsts.getKeyStore(storeType);
        try (InputStream in = new FileInputStream(
                ResourceUtils.getFile(storePath))) {
            keyStore.load(in, password);
        }

        return keyStore;
    }


}
