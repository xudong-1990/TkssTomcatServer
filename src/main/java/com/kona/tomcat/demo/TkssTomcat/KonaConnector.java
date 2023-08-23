package com.kona.tomcat.demo.TkssTomcat;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.connector.CoyoteAdapter;
import org.apache.coyote.http11.AbstractHttp11JsseProtocol;

/**
 * @Description：
 * @Author： zxd0308
 * @Date: Created in 2023/8/18 9:36
 * @Modified By:
 */
public class KonaConnector extends Connector {

    public KonaConnector(String protocol) {
        super(protocol);
    }

    @Override
    protected void initInternal() throws LifecycleException {
        // Initialize adapter
        adapter = new CoyoteAdapter(this);
        protocolHandler.setAdapter(adapter);

        // Make sure parseBodyMethodsSet has a default
        if (parseBodyMethodsSet == null) {
            setParseBodyMethods(getParseBodyMethods());
        }

        // Use custom JSSEImplementation
        ((AbstractHttp11JsseProtocol<?>) protocolHandler).setSslImplementationName(
                KonaSSLImpl.class.getName());

        try {
            protocolHandler.init();
        } catch (Exception e) {
            throw new LifecycleException(sm.getString(
                    "coyoteConnector.protocolHandlerInitializationFailed"), e);
        }
    }
}
