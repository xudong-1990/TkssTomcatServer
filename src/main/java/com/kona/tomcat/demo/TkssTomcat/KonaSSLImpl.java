package com.kona.tomcat.demo.TkssTomcat;

import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.apache.tomcat.util.net.SSLUtil;
import org.apache.tomcat.util.net.jsse.JSSEImplementation;

/**
 * @Description：
 * @Author： zxd0308
 * @Date: Created in 2023/8/18 9:38
 * @Modified By:
 */
public class KonaSSLImpl extends JSSEImplementation{
    @Override
    public SSLUtil getSSLUtil(SSLHostConfigCertificate certificate) {
        return new KonaSSLUtil(certificate);
    }
}
