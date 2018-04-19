package com.carlt.doride.http;

import com.carlt.doride.utils.ILog;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by yyun on 17-9-19.
 */

public class LogInterceptor implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();


        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        Connection connection = chain.connection();
        String requestStartMessage = "--> "
                + request.method()
                + ' ' + request.url()
                + (connection != null ? " " + connection.protocol() : "");
        if (hasRequestBody) {
            requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
        }

        ILog.e("http",requestStartMessage);

        if (hasRequestBody) {
            // Request body headers are only present when installed as a network interceptor. Force
            // them to be included (when available) so there values are known.
            if (requestBody.contentType() != null) {
                ILog.e("http","Content-Type: " + requestBody.contentType());
            }
            if (requestBody.contentLength() != -1) {
                ILog.e("http","Content-Length: " + requestBody.contentLength());
            }
        }

        Headers headers = request.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            // Skip headers from the request body as they are explicitly logged above.
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                ILog.e("http",name + ": " + headers.value(i));
            }
        }

        if (bodyEncoded(request.headers())) {
            ILog.e("http","--> END " + request.method() + " (encoded body omitted)");
        } else {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            ILog.e("http","");
            if (isPlaintext(buffer)) {
                ILog.e("http",buffer.readString(charset));
                ILog.e("http","--> END " + request.method()
                        + " (" + requestBody.contentLength() + "-byte body)");
            } else {
                ILog.e("http","--> END " + request.method() + " (binary "
                        + requestBody.contentLength() + "-byte body omitted)");
            }
        }

        long startNs = System.nanoTime();
        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            ILog.e("http","<-- HTTP FAILED: " + e);
            throw e;
        }
        long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
        ILog.e("http","<-- " + response.code()
                + (response.message().isEmpty() ? "" : ' ' + response.message())
                + ' ' + response.request().url()
                + " (" + tookMs + "ms" + ", " + bodySize + " body" + ')');

        headers = response.headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            ILog.e("http",headers.name(i) + ": " + headers.value(i));
        }

        if (bodyEncoded(response.headers())) {
            ILog.e("http","<-- END HTTP (encoded body omitted)");
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }

            if (!isPlaintext(buffer)) {
                ILog.e("http","");
                ILog.e("http","<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                return response;
            }

            if (contentLength != 0) {
                ILog.e("http","");
                ILog.e("http",buffer.clone().readString(charset));
            }

            ILog.e("http","<-- END HTTP (" + buffer.size() + "-byte body)");
        }


        return response;
    }


    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }


}
