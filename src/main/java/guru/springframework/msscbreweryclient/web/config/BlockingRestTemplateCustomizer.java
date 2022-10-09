package guru.springframework.msscbreweryclient.web.config;

import java.io.IOException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Old blocking realization of {@link org.springframework.web.client.RestTemplate}.
 */
//@Component
public class BlockingRestTemplateCustomizer implements RestTemplateCustomizer {

  private ClientHttpRequestFactory customClientHttpRequestFactory() {
    try(final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager()) {
      // TODO externalize properties
      connectionManager.setMaxTotal(100);
      connectionManager.setDefaultMaxPerRoute(20);

      // Set request timeout and socket timeout
      final RequestConfig requestConfig = RequestConfig
          .custom()
          .setConnectionRequestTimeout(3000)
          .setSocketTimeout(3000)
          .build();

      // Create HtpClient with above configuration
      try(final CloseableHttpClient httpClient = HttpClients
          .custom()
          .setConnectionManager(connectionManager)
          .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
          .setDefaultRequestConfig(requestConfig)
          .build()) {

          return new HttpComponentsClientHttpRequestFactory(httpClient);

      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void customize(RestTemplate restTemplate) {
    restTemplate.setRequestFactory(this.customClientHttpRequestFactory());
  }
}
