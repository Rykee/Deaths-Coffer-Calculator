package hu.rhykee.deaths_coffer_calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Log4j2
public class ApplicationStartupHandler implements ApplicationListener<ApplicationReadyEvent> {

    ItemScraperService scraperService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            scraperService.scrapeTradeVolume();
            scraperService.scrapeGrandExchangePrices();
            scraperService.scrapeRuneLitePrices();
            scraperService.scrapeTradeLimit();
        } catch (IOException | InterruptedException e) {
            log.error("Failed to scrape after application startup", e);
            throw new RuntimeException(e);
        }
    }
}
