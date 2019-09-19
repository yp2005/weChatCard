package com.weChatCard.wechatpay.sdk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyWxPayConfig extends WXPayConfig{

    private byte[] certData;

    public MyWxPayConfig() throws Exception {
        String certPath = "";
        File file = new File(certPath);
        InputStream certStream = new FileInputStream(file);
        this.certData = new byte[(int) file.length()];
        certStream.read(this.certData);
        certStream.close();
    }

    public String getAppID() {
        return "wx247dfe5fbcd1fb3a";
    }

    public String getMchID() {
        return "";
    }

    public String getKey() {
        return "d2a74f6df5b54d8adfdcf2f5a8aae493";
    }


    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    IWXPayDomain getWXPayDomain() {
        IWXPayDomain iwxPayDomain = new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new DomainInfo("www.xxxxx.com",true);
            }
        };
        return iwxPayDomain;
    }

}