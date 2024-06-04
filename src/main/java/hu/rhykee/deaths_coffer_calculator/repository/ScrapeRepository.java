package hu.rhykee.deaths_coffer_calculator.repository;

import hu.rhykee.deaths_coffer_calculator.document.ScrapeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrapeRepository extends MongoRepository<ScrapeDocument, String> {
}
