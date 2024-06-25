package hu.rhykee.deaths_coffer_calculator.service;

import hu.rhykee.deaths_coffer_calculator.document.ItemDocument;
import hu.rhykee.deaths_coffer_calculator.repository.ItemRepository;
import hu.rhykee.deaths_coffer_calculator.util.NonNullConversionService;
import hu.rhykee.model.CalculateBestDeathsCofferOfferingsRequest;
import hu.rhykee.model.DeathsCofferCalculationResult;
import hu.rhykee.model.ItemOffering;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class OfferingCalculatorService {

    ItemRepository itemRepository;
    NonNullConversionService conversionService;

    public DeathsCofferCalculationResult calculateBestOfferings(CalculateBestDeathsCofferOfferingsRequest request) {
        Stream<ItemDocument> stream = itemRepository.findAll()
                .stream()
                .filter(itemDocument -> itemDocument.getBuyPrice() != 0);
        if (request.getMaximumPrice() != null) {
            stream = stream.filter(itemDocument -> itemDocument.getBuyPrice() <= request.getMaximumPrice());
        }
        if (request.getMinimumOfferingValue() != null) {
            stream = stream.filter(itemDocument -> itemDocument.getGrandExchangeGuidePrice() * 1.05f >= request.getMinimumOfferingValue());
        }
        if(request.getMinimumTradeVolume()!=null){
            stream = stream.filter(itemDocument -> itemDocument.getTradeVolume() >= request.getMinimumTradeVolume());
        }
        List<ItemDocument> bestOfferings = stream.sorted((o1, o2) -> Long.compare(o2.getGrandExchangeGuidePrice() - o2.getBuyPrice(), o1.getGrandExchangeGuidePrice() - o1.getBuyPrice()))
                .toList();
        return DeathsCofferCalculationResult.builder()
                .bestOfferings(conversionService.convertList(bestOfferings, ItemOffering.class))
                .build();
    }
}
