package hu.rhykee.deaths_coffer_calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Log4j2
public class ScheduleService {

    ItemScraperService scraperService;

    @Scheduled(cron = "0 0 */4 * * *")
    public void scrapeGrandExchange() {
        try {
            scraperService.scrapeGrandExchangePrices();
        } catch (InterruptedException | IOException e) {
            log.error("Failed to scrape GE prices", e);
        }
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void scrapeRuneLite() {
        scraperService.scrapeRuneLitePrices();
    }

}
