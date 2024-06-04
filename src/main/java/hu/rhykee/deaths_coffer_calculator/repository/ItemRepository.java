package hu.rhykee.deaths_coffer_calculator.repository;

import hu.rhykee.deaths_coffer_calculator.document.ItemDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends MongoRepository<ItemDocument, String> {

    Optional<ItemDocument> findByItemId(int id);

    List<ItemDocument> findAllByOrderByName();
}
