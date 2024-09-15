package com.bookstore.dev.configs.http;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class WebClientConfiguration {
    @Value("${http-connection-pool.max-connections-count}")
    private int maxConnectionsCount;
    @Value("${http-connection-pool.read-timeout}")
    private int readTimeout;
    @Value("${http-connection-pool.write-timeout}")
    private int writeTimeout;
    @Value("${http-connection-pool.connect-timeout}")
    private int connectTimeout;
    @Value("${http-connection-pool.socket-timeout}")
    private int socketTimeout;
    @Value("${http-connection-pool.max-idle-time}")
    private int maxIdleTime;
    @Value("${http-connection-pool.max-life-time}")
    private int maxLifeTime;
    @Value("${http-connection-pool.pending-acquire-timeout}")
    private int pendingAcquireTimeout;
    @Value("${http-connection-pool.evict-in-background}")
    private int evictInBackground;

    @Bean
    public WebClient webClientFromBuilder(WebClient.Builder webClientBuilder) {
        final int size = 26 * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();

        ConnectionProvider provider = ConnectionProvider.builder("custom")
                .maxConnections(maxConnectionsCount)
                .maxIdleTime(Duration.ofSeconds(maxIdleTime))
                .maxLifeTime(Duration.ofMinutes(maxLifeTime))
                .pendingAcquireTimeout(Duration.ofSeconds(pendingAcquireTimeout))
                .evictInBackground(Duration.ofSeconds(evictInBackground))
                .build();

        HttpClient httpClient = HttpClient.create(provider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .responseTimeout(Duration.ofMillis(socketTimeout))
                .doOnConnected(connection ->
                        connection
                                .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS)));

        return webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                //.filter(logRequest())
                .filter(logResponse())
                .exchangeStrategies(strategies)
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            return next.exchange(clientRequest);
        };
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            log.info("Response: {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
}
