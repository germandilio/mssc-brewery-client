package guru.springframework.msscbreweryclient.web.config;

import java.io.IOException;
import lombok.Setter;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.conn.PoolingNHttpClientConnectionManager;
import org.apache.http.impl.nio.reactor.DefaultConnectingIOReactor;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.reactor.IOReactorException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsAsyncClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * New nonblocking realization of {@link org.springframework.web.client.RestTemplate}.
 */
@Setter
@ConfigurationProperties(prefix = "rest.template.reactive.config", ignoreUnknownFields = false)
@Component
public class NioRestTemplateCustomizer implements RestTemplateCustomizer {
  private int connectionTimeout;
  private int ioThreadCount;
  private int socketTimeout;

  private int maxConnectionsPerRoute;
  private int maxConnectionsTotal;

  private ClientHttpRequestFactory customClientHttpRequestFactory() throws IOReactorException {
    final DefaultConnectingIOReactor ioReactor = new DefaultConnectingIOReactor(IOReactorConfig
        .custom()
        .setConnectTimeout(connectionTimeout)
        .setIoThreadCount(ioThreadCount)
        .setSoTimeout(socketTimeout)
        .build());

    final PoolingNHttpClientConnectionManager connectionManager = new PoolingNHttpClientConnectionManager(ioReactor);
    connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
    connectionManager.setMaxTotal(maxConnectionsTotal);

    try(CloseableHttpAsyncClient httpAsyncClient = HttpAsyncClients
        .custom()
        .setConnectionManager(connectionManager)
        .build()) {

        // in Spring 5.0 deprecated with no direct replacement (avoid by using WebClient instead of RestTemplate)
        return new HttpComponentsAsyncClientHttpRequestFactory(httpAsyncClient);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void customize(RestTemplate restTemplate) {
    try {
      restTemplate.setRequestFactory(this.customClientHttpRequestFactory());
    } catch (IOReactorException e) {
      throw new RuntimeException(e);
    }
  }
}
