package hu.rhykee.deaths_coffer_calculator.web;

import hu.rhykee.api.ItemsApi;
import hu.rhykee.deaths_coffer_calculator.document.ItemDocument;
import hu.rhykee.deaths_coffer_calculator.repository.ItemRepository;
import hu.rhykee.deaths_coffer_calculator.util.NonNullConversionService;
import hu.rhykee.model.GetItemsResponse;
import hu.rhykee.model.Item;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ItemsController implements ItemsApi {

    ItemRepository itemRepository;
    NonNullConversionService conversionService;

    @Override
    public ResponseEntity<Item> getItem(Integer id) {
        Optional<ItemDocument> item = itemRepository.findByItemId(id);
        return item.map(itemDocument -> ResponseEntity.ok(conversionService.convert(itemDocument, Item.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<GetItemsResponse> getItems() {
        List<Item> items = itemRepository.findAllByOrderByName().stream()
                .map(itemDocument -> conversionService.convert(itemDocument, Item.class))
                .toList();
        GetItemsResponse response = new GetItemsResponse();
        response.setItems(items);
        return ResponseEntity.ok(response);
    }

}
