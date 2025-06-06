package com.kufamilylinkbackend.infrastructure.mqtt;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class AwsIotSslUtil {
  static {
    Security.addProvider(new BouncyCastleProvider());
  }

  public static SSLSocketFactory getSocketFactory(String caCertPath, String clientCertPath, String privateKeyPath) throws Exception {
    // Load CA certificate
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    X509Certificate caCert;
    try (InputStream in = new FileInputStream(caCertPath)) {
      caCert = (X509Certificate) cf.generateCertificate(in);
    }

    // Load client certificate
    X509Certificate clientCert;
    try (InputStream in = new FileInputStream(clientCertPath)) {
      clientCert = (X509Certificate) cf.generateCertificate(in);
    }

    // Load private key
    PrivateKey privateKey;
    try (PEMParser pemParser = new PEMParser(new FileReader(privateKeyPath))) {
      Object object = pemParser.readObject();
      PEMKeyPair keyPair = (PEMKeyPair) object;
      privateKey = new JcaPEMKeyConverter().getPrivateKey(keyPair.getPrivateKeyInfo());
    }

    // Create a KeyStore with client certificate and private key
    KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(null, null);
    Certificate[] certChain = new Certificate[]{clientCert};  // include chain
    keyStore.setKeyEntry("client", privateKey, "".toCharArray(), certChain);

    // KeyManager
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(keyStore, "".toCharArray());

    // TrustManager
    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(null, null);
    trustStore.setCertificateEntry("caCert", caCert);

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);

    // SSLContext
    SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new SecureRandom());

    return sslContext.getSocketFactory();
  }

}
