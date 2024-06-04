package hu.rhykee.deaths_coffer_calculator.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Document(collection = "scrape-details")
public class ScrapeDocument {

    @Id
    private String id;
    private ScareSource scareSource;
    private OffsetDateTime lastScrapeDateTime;

    public enum ScareSource {
        RUNELITE, JAGEX
    }

}
