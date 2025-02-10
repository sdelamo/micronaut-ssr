package com.poc.micronaut.ssr;

import io.micronaut.context.annotation.Context;
import io.micronaut.http.HttpMethod;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Context
@Singleton
public class Utils {
    private static HttpClient client;

    Utils(@Client("/") HttpClient client) {
        Utils.client = client;
    }

    public static PromiseExecutor fetch(String url) {
        var req = HttpRequest
                .create(HttpMethod.GET, url)
                .accept(MediaType.TEXT_PLAIN);
        return async(req);
        //return noAsync(req);
    }

    public static PromiseExecutor noAsync(HttpRequest<?> request) {
        return (onResolve, onReject) -> {
            String str = client.toBlocking().retrieve(request);
            onResolve.execute(str);
        };
    }

    public static PromiseExecutor async(HttpRequest<?> request) {
        return (onResolve, onReject) -> Mono.from(client.retrieve(request))
                .subscribe(str -> {
                    onResolve.execute(str);
                        });


    }
}
