package com.tadamski.arij.util.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tadamski.arij.BuildConfig;
import com.tadamski.arij.account.service.LoginInfo;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by tmszdmsk on 02.07.13.
 */
public class RestAdapterProvider {

    public static <T> T get(Class<T> clazz, LoginInfo loginInfo) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new ISODateAdapter()).create();

        return new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.BASIC)
                .setServer(loginInfo.getBaseURL())
                .setRequestInterceptor(new AuthorizationInterceptor(loginInfo))
                .setErrorHandler(new JiraErrorHandler())
                .setClient(new UrlConnectionClient(loginInfo.isSecureHttps()))
                .build()
                .create(clazz);
    }


    public static class UrlConnectionClient implements Client {
        private final Field methodField;
        private boolean secureHttps;

        public UrlConnectionClient(boolean secureHttps) {
            this.secureHttps = secureHttps;
            try {
                this.methodField = HttpURLConnection.class.getDeclaredField("method");
                this.methodField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw RetrofitError.unexpectedError(null, e);
            }
        }

        @Override
        public Response execute(Request request) throws IOException {
            HttpURLConnection connection = openConnection(request);
            prepareRequest(connection, request);
            return readResponse(connection);
        }

        protected HttpURLConnection openConnection(Request request) throws IOException {
            HttpURLConnection connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
            connection.setConnectTimeout(15 * 1000);
            connection.setReadTimeout(15 * 1000);
            if (connection instanceof HttpsURLConnection && !secureHttps) {
                try {
                    HttpsURLConnection httpsConn = (HttpsURLConnection) connection;
                    final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(final X509Certificate[] chain, final String authType) {
                        }

                        @Override
                        public void checkServerTrusted(final X509Certificate[] chain, final String authType) {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }};
                    final SSLContext sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                    final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                    httpsConn.setSSLSocketFactory(sslSocketFactory);
                } catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
            return connection;
        }

        void prepareRequest(HttpURLConnection connection, Request request) throws IOException {
            // HttpURLConnection artificially restricts request method
            try {
                connection.setRequestMethod(request.getMethod());
            } catch (ProtocolException e) {
                try {
                    methodField.set(connection, request.getMethod());
                } catch (IllegalAccessException e1) {
                    throw RetrofitError.unexpectedError(request.getUrl(), e1);
                }
            }

            connection.setDoInput(true);

            for (Header header : request.getHeaders()) {
                connection.addRequestProperty(header.getName(), header.getValue());
            }

            TypedOutput body = request.getBody();
            if (body != null) {
                connection.setDoOutput(true);
                connection.addRequestProperty("Content-Type", body.mimeType());
                long length = body.length();
                if (length != -1) {
                    connection.addRequestProperty("Content-Length", String.valueOf(length));
                }
                body.writeTo(connection.getOutputStream());
            }
        }

        Response readResponse(HttpURLConnection connection) throws IOException {
            int status = connection.getResponseCode();
            String reason = connection.getResponseMessage();

            List<Header> headers = new ArrayList<Header>();
            for (Map.Entry<String, List<String>> field : connection.getHeaderFields().entrySet()) {
                String name = field.getKey();
                for (String value : field.getValue()) {
                    headers.add(new Header(name, value));
                }
            }

            String mimeType = connection.getContentType();
            int length = connection.getContentLength();
            InputStream stream;
            if (status >= 400) {
                stream = connection.getErrorStream();
            } else {
                stream = connection.getInputStream();
            }
            TypedInput responseBody = new TypedInputStream(mimeType, length, stream);
            return new Response(status, reason, headers, responseBody);
        }

        private static class TypedInputStream implements TypedInput {
            private final String mimeType;
            private final long length;
            private final InputStream stream;

            private TypedInputStream(String mimeType, long length, InputStream stream) {
                this.mimeType = mimeType;
                this.length = length;
                this.stream = stream;
            }

            @Override
            public String mimeType() {
                return mimeType;
            }

            @Override
            public long length() {
                return length;
            }

            @Override
            public InputStream in() throws IOException {
                return stream;
            }
        }
    }
}
