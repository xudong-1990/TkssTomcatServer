package com.kona.tomcat.demo.TkssTomcat;

import org.apache.tomcat.util.net.SSLHostConfig;

import java.util.*;

/**
 * @Description：
 * @Author： zxd0308
 * @Date: Created in 2023/8/18 9:36
 * @Modified By:
 */
public class KonaSSLHostConfig  extends SSLHostConfig {
    private static final long serialVersionUID = 3931709572625017292L;

    private Set<String> protocols;
    private List<String> ciphersuites;

    @Override
    public Set<String> getProtocols() {
        if (protocols == null) {
            protocols = new HashSet<>();
            protocols.add("TLCPv1.1");
            protocols.add("TLSv1.3");
        }

        return protocols;
    }

    @Override
    public List<String> getJsseCipherNames() {
        if (ciphersuites == null) {
            ciphersuites = Collections.unmodifiableList(Arrays.asList(
                    "TLCP_ECC_SM4_GCM_SM3",
                    "TLCP_ECC_SM4_CBC_SM3",
                    "TLCP_ECDHE_SM4_GCM_SM3",
                    "TLCP_ECDHE_SM4_CBC_SM3",
                    "TLS_SM4_GCM_SM3",
                    "TLS_AES_128_GCM_SHA256"));
        }

        return ciphersuites;
    }
}
