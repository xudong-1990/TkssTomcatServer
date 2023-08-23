package com.kona.tomcat.demo.TkssTomcat;

import com.tencent.kona.ssl.SSLInsts;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.apache.tomcat.util.net.SSLUtilBase;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Description：
 * @Author： zxd0308
 * @Date: Created in 2023/8/18 9:39
 * @Modified By:
 */
public class KonaSSLUtil extends SSLUtilBase{
    private static final Log LOG = LogFactory.getLog(KonaSSLUtil.class);

    private Set<String> protocols;
    private Set<String> ciphersuites;

    public KonaSSLUtil(SSLHostConfigCertificate certificate) {
        super(certificate);
    }

    public KonaSSLUtil(SSLHostConfigCertificate certificate,
                       boolean warnTls13) {
        super(certificate, warnTls13);
    }

    @Override
    public KeyManager[] getKeyManagers() throws Exception {
        KeyManagerFactory kmf = SSLInsts.getKeyManagerFactory("NewSunX509");
        kmf.init(certificate.getCertificateKeystore(),
                certificate.getCertificateKeystorePassword().toCharArray());
        return kmf.getKeyManagers();
    }

    @Override
    protected Log getLog() {
        return LOG;
    }

    @Override
    protected Set<String> getImplementedProtocols() {
        if (protocols == null) {
            protocols = new HashSet<>();
            protocols.add("TLCPv1.1");
            protocols.add("TLSv1.3");
        }

        return protocols;
    }

    @Override
    protected Set<String> getImplementedCiphers() {
        if (ciphersuites == null) {
            Set<String> temp = new HashSet<>();
            temp.add("TLCP_ECC_SM4_GCM_SM3");
            temp.add("TLCP_ECC_SM4_CBC_SM3");
            temp.add("TLCP_ECDHE_SM4_GCM_SM3");
            temp.add("TLCP_ECDHE_SM4_CBC_SM3");
            temp.add("TLS_SM4_GCM_SM3");
            temp.add("TLS_AES_128_GCM_SHA256");

            ciphersuites = Collections.unmodifiableSet(temp);
        }

        return ciphersuites;
    }

    @Override
    protected boolean isTls13RenegAuthAvailable() {
        // TLS 1.3 does not support authentication after the initial handshake
        return false;
    }

    @Override
    public org.apache.tomcat.util.net.SSLContext createSSLContextInternal(
            List<String> negotiableProtocols) throws NoSuchAlgorithmException {
        return new KonaSSLContext(sslHostConfig.getSslProtocol());
    }
}
