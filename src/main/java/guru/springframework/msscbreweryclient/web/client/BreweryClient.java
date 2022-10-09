package guru.springframework.msscbreweryclient.web.client;

import guru.springframework.msscbreweryclient.web.model.BeerDto;
import guru.springframework.msscbreweryclient.web.model.CustomerDto;
import java.net.URI;
import java.util.UUID;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Setter
@ConfigurationProperties(value = "sfg.brewery", ignoreUnknownFields = false)
@Component
public class BreweryClient {
  private String beerPathV1;
  private String customerPathV1;

  private String apiHost;
  private final RestTemplate restTemplate;

  @Autowired
  public BreweryClient(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public BeerDto getBeerById(UUID id) {
    return restTemplate.getForObject(apiHost + beerPathV1 + id.toString(), BeerDto.class);
  }

  public URI saveNewBeer(BeerDto beer) {
    return restTemplate.postForLocation(apiHost + beerPathV1, beer);
  }

  public void updateBeer(UUID id, BeerDto beer) {
    restTemplate.put(apiHost + beerPathV1 + id, beer);
  }

  public void deleteBear(UUID id) {
    restTemplate.delete(apiHost + beerPathV1 + id);
  }

  public CustomerDto getCustomerById(UUID customerId) {
    return restTemplate.getForObject(apiHost+ customerPathV1 + customerId.toString(), CustomerDto.class);
  }

  public URI saveNewCustomer(CustomerDto customerDto) {
    return  restTemplate.postForLocation(apiHost + customerPathV1, customerDto);
  }

  public void updateCustomer(UUID customerId, CustomerDto customerDto) {
    restTemplate.put(apiHost + customerPathV1 + customerId, customerDto);
  }

  public void deleteCustomer(UUID customerId) {
    restTemplate.delete(apiHost + customerPathV1 + customerId);
  }
}
