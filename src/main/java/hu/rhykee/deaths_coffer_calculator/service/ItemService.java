package hu.rhykee.deaths_coffer_calculator.service;

import hu.rhykee.deaths_coffer_calculator.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class ItemService {

    ItemRepository itemRepository;

}
