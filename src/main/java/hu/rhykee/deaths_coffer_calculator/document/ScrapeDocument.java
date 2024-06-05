package hu.rhykee.deaths_coffer_calculator.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Document(collection = "scrape-details")
public class ScrapeDocument {

    @Id
    private String id;
    private ScrapeSource scrapeSource;
    private OffsetDateTime lastScrapeDateTime;

    public enum ScrapeSource {
        RUNELITE, JAGEX
    }

}
