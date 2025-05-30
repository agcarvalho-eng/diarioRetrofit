package com.example.diarioestudanteretrofit.util;

import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.TrustManager;

/**
 * Classe que fornece um OkHttpClient configurado para permitir conexões inseguras (sem validação de certificados SSL).
 * Este código deve ser usado apenas em ambientes de desenvolvimento, pois ignora as verificações de segurança padrão do SSL.
 */
public class OkHttpClient {

    /**
     * Método responsável por fornecer um OkHttpClient que ignora as verificações de certificados SSL.
     * Este cliente não valida as conexões SSL com o servidor, o que pode ser útil para testes em ambientes locais.
     *
     * @return Um OkHttpClient que pode ser utilizado para realizar requisições HTTP/HTTPS sem validação de certificado.
     */
    static okhttp3.OkHttpClient obterOkHttpClientInseguro() {
        try {
            // Criação de um TrustManager que não valida as cadeias de certificados (não segura).
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            // Não realiza nenhuma validação do certificado do cliente.
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                            // Não realiza nenhuma validação do certificado do servidor.
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            // Não define certificados aceitáveis, portanto, aceita qualquer um.
                            return new X509Certificate[]{};
                        }
                    }
            };

            // Configuração do SSLContext para usar o TrustManager que não valida os certificados.
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom()); // Inicializa o SSLContext com o TrustManager e um gerador de números aleatórios.

            // Criação de um SSLSocketFactory com o nosso SSLContext personalizado.
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Construtor do OkHttpClient, configurando o SSL e o hostnameVerifier.
            okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();

            // Configura o OkHttpClient para usar nosso SSLSocketFactory inseguro.
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

            // Configura o HostnameVerifier para aceitar qualquer nome de host (ignorando a validação de hostname).
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true; // Aceita qualquer hostname (não seguro).
                }
            });

            // Criação e retorno do OkHttpClient com as configurações inseguras.
            okhttp3.OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            // Caso ocorra qualquer erro, lança uma exceção runtime.
            throw new RuntimeException(e);
        }
    }
}

