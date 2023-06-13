package de.nikolas.einsteinmedia;

import de.nikolas.einsteinmedia.commons.httpserver.log.Logger;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;

public class ReverseProxy {


    /**
     * Not used because didn't work properly
     * (maybe in the future)
     *
     * @throws IOException
     */

    public ReverseProxy() throws IOException {
        final Logger logger = Logger.Factory.createLogger(ReverseProxy.class);
        int localPort = 8083;

        String remoteHost = "localhost";
        int remotePort = 8081;

        String keystoreFile = "/Users/nikolas/dev/ssl/keystore.p12";
        String keystorePassword = "pass123";

        SSLServerSocketFactory sslServerSocketFactory = createSSLServerSocketFactory(keystoreFile, keystorePassword);
        ServerSocket serverSocket = sslServerSocketFactory.createServerSocket(localPort);
        logger.info("Reverse proxy server is listening on port " + localPort);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            Thread requestHandlerThread = new Thread(() -> {
                try {
                    // Connect to the backend server
                    SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
                    Socket backendSocket = sslSocketFactory.createSocket(remoteHost, remotePort);

                    // Forward client request to the backend server
                    Thread clientToBackendThread = new Thread(() -> {
                        try {
                            InputStream clientInput = clientSocket.getInputStream();
                            OutputStream backendOutput = backendSocket.getOutputStream();
                            byte[] buffer = new byte[4096];
                            int bytesRead;
                            while ((bytesRead = clientInput.read(buffer)) != -1) {
                                backendOutput.write(buffer, 0, bytesRead);
                            }
                            backendOutput.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    clientToBackendThread.start();

                    // Forward backend response to the client
                    InputStream backendInput = backendSocket.getInputStream();
                    OutputStream clientOutput = clientSocket.getOutputStream();
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = backendInput.read(buffer)) != -1) {
                        clientOutput.write(buffer, 0, bytesRead);
                    }
                    clientOutput.flush();

                    // Close the sockets
                    clientSocket.close();
                    backendSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            requestHandlerThread.start();
        }
    }

    private SSLServerSocketFactory createSSLServerSocketFactory(String keystoreFile, String keystorePassword) {
        try {
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            InputStream keystoreInputStream = new FileInputStream(keystoreFile);
            keystore.load(keystoreInputStream, keystorePassword.toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keystore, keystorePassword.toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keystore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            return sslContext.getServerSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
