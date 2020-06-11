package com.czareg;

import java.lang.reflect.Proxy;

//decorator
public class RetriableClient implements IClient {
    private IClient clientProxy;

    public RetriableClient(Client originalClient) {
        clientProxy = (IClient) Proxy.newProxyInstance(IClient.class.getClassLoader(),
                new Class[]{IClient.class},
                new ClientProxy(originalClient));
    }

    @Override
    public String getHostname() throws ClientException {
        return clientProxy.getHostname();
    }
}
