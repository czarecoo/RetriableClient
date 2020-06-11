package com.czareg;

//actual client
public class Client implements IClient {
    @Override
    public String getHostname() {
        return "hostname";
    }
}
