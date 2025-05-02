package util;

import okhttp3.*;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Serviço HTTP baseado em OkHttp com método shutdown() reforçado:
 *  • Cancela requisições pendentes;
 *  • Encerra executor e pool;
 *  • Faz awaitTermination para dar tempo às threads;
 *  • Fecha cache, se existir.
 */
public class ServicoHttp {

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .build();

    /** Encerra completamente as estruturas internas do OkHttp. */
    public static void shutdown() {
        // 1) Cancela qualquer chamada ainda aberta
        CLIENT.dispatcher().cancelAll();

        // 2) Fecha executor & pool
        CLIENT.dispatcher().executorService().shutdown();
        CLIENT.connectionPool().evictAll();

        // 3) Aguarda até 5 s para as threads terminarem graciosamente
        try {
            CLIENT.dispatcher().executorService()
                  .awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }

        // 4) Fecha cache (se configurado)
        Cache cache = CLIENT.cache();
        if (cache != null) try { cache.close(); } catch (IOException ignore) {}
    }

    /* ---------------- operações HTTP ---------------- */

    public Response fazerRequisicaoHttpGET(String url) throws IOException {
        Request req = new Request.Builder()
                .url(url)
                .addHeader("Accept-Encoding", "identity")
                .build();
        return CLIENT.newCall(req).execute();
    }

    public String fazerRequisicaoHttpGET(String url, String cookie) throws IOException {
        Request req = new Request.Builder()
                .url(url)
                .addHeader("Cookie", cookie)
                .addHeader("Accept-Encoding", "identity")
                .build();
        try (Response resp = CLIENT.newCall(req).execute()) {
            return Objects.requireNonNull(resp.body()).string();
        }
    }

    public String fazerRequisicaoHttpPOST(String url,
                                          String postBody,
                                          String cookie) throws IOException {

        MediaType FORM = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(postBody, FORM);

        Request req = new Request.Builder()
                .url(url)
                .addHeader("Cookie", cookie)
                .addHeader("Accept-Encoding", "identity")
                .post(body)
                .build();

        try (Response resp = CLIENT.newCall(req).execute()) {
            return Objects.requireNonNull(resp.body()).string();
        }
    }
}
