package guru.springframework.msscbreweryclient.web.client;

import static org.junit.jupiter.api.Assertions.*;

import guru.springframework.msscbreweryclient.web.model.BeerDto;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BreweryClientTest {
  @Autowired
  BreweryClient breweryClient;

  @Test
  void getBeerById() {
    UUID uuid = UUID.randomUUID();
    var beer = breweryClient.getBeerById(uuid);

    final BeerDto expected = BeerDto.builder()
        .id(uuid)
        .beerName("Galaxy Cat")
        .beerStyle("Pale Ale")
        .build();

    assertEquals(expected, beer);
  }

  @Test
  void saveNewBeer() throws URISyntaxException {
    final BeerDto beerToSave = BeerDto.builder()
        .id(UUID.randomUUID())
        .beerName("New Beer")
        .beerStyle("Pale Ale v2")
        .build();

    URI location = breweryClient.saveNewBeer(beerToSave);
    assertNotNull(location);
  }

  @Test
  void updateBeer() {
    UUID uuid = UUID.randomUUID();
    final BeerDto beer = BeerDto.builder()
        .id(uuid)
        .beerName("Galaxy Cat")
        .beerStyle("Pale Ale")
        .build();

    assertDoesNotThrow(() -> breweryClient.updateBeer(uuid, beer));
  }

  @Test
  void deleteBeer() {
    UUID uuid = UUID.randomUUID();

    assertDoesNotThrow(() -> breweryClient.deleteBear(uuid));
  }
}