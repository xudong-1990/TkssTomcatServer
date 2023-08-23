package com.kona.tomcat.demo.TkssTomcat;

import com.tencent.kona.ssl.SSLInsts;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @Description：
 * @Author： zxd0308
 * @Date: Created in 2023/8/18 9:40
 * @Modified By:
 */
public class KonaSSLContext implements org.apache.tomcat.util.net.SSLContext {
    private final SSLContext context;
    private KeyManager[] kms;
    private TrustManager[] tms;

    public KonaSSLContext(String protocol) throws NoSuchAlgorithmException {
        context = SSLInsts.getSSLContext(protocol);
    }

    @Override
    public void init(KeyManager[] kms, TrustManager[] tms, SecureRandom random)
            throws KeyManagementException {
        this.kms = kms;
        this.tms = tms;
        context.init(kms, tms, random);
    }

    @Override
    public void destroy() {
    }

    @Override
    public SSLSessionContext getServerSessionContext() {
        return context.getServerSessionContext();
    }

    @Override
    public SSLEngine createSSLEngine() {
        return context.createSSLEngine();
    }

    @Override
    public SSLServerSocketFactory getServerSocketFactory() {
        return context.getServerSocketFactory();
    }

    @Override
    public SSLParameters getSupportedSSLParameters() {
        return context.getSupportedSSLParameters();
    }

    @Override
    public X509Certificate[] getCertificateChain(String alias) {
        X509Certificate[] result = null;
        if (kms != null) {
            for (int i = 0; i < kms.length && result == null; i++) {
                if (kms[i] instanceof X509KeyManager) {
                    result = ((X509KeyManager) kms[i]).getCertificateChain(alias);
                }
            }
        }
        return result;
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        Set<X509Certificate> certs = new HashSet<>();
        if (tms != null) {
            for (TrustManager tm : tms) {
                if (tm instanceof X509TrustManager) {
                    X509Certificate[] accepted = ((X509TrustManager) tm).getAcceptedIssuers();
                    if (accepted != null) {
                        certs.addAll(Arrays.asList(accepted));
                    }
                }
            }
        }
        return certs.toArray(new X509Certificate[0]);
    }
}
