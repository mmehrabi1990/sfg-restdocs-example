package mehrabi.springframework.sfgrestdocsexample.bootstrap;

import lombok.extern.slf4j.Slf4j;
import mehrabi.springframework.sfgrestdocsexample.domain.Beer;
import mehrabi.springframework.sfgrestdocsexample.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Slf4j
public class BeerLoader implements CommandLineRunner {

    private final BeerRepository beerRepository;

    public BeerLoader(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) {
        loadBeerObjects();
    }

    private void loadBeerObjects() {
        if (beerRepository.count() == 0){
            beerRepository.save(Beer.builder()
                    .beerName("Galaxy Cat")
                    .beerStyle("IPA")
                    .quantityToBrew(200)
                    .minOnHand(12)
                    .upc(337010000001L)
                    .price(new BigDecimal("12.95")).build());

            beerRepository.save(Beer.builder()
                    .beerName("Mango Bobs")
                    .beerStyle("PALE_ALE")
                    .quantityToBrew(200)
                    .minOnHand(12)
                    .upc(337010000002L)
                    .price(new BigDecimal("11.95")).build());

            log.debug("Loaded Beers : "+beerRepository.count());
        }
    }
}
