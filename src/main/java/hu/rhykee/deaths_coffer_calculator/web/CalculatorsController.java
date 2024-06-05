package hu.rhykee.deaths_coffer_calculator.web;

import hu.rhykee.api.CalculatorsApi;
import hu.rhykee.api.ItemsApi;
import hu.rhykee.deaths_coffer_calculator.document.ItemDocument;
import hu.rhykee.deaths_coffer_calculator.repository.ItemRepository;
import hu.rhykee.deaths_coffer_calculator.service.OfferingCalculatorService;
import hu.rhykee.deaths_coffer_calculator.util.NonNullConversionService;
import hu.rhykee.model.CalculateBestDeathsCofferOfferingsRequest;
import hu.rhykee.model.DeathsCofferCalculationResult;
import hu.rhykee.model.GetItemsResponse;
import hu.rhykee.model.Item;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class CalculatorsController implements CalculatorsApi {

    OfferingCalculatorService offeringCalculatorService;

    @Override
    public ResponseEntity<DeathsCofferCalculationResult> calculateBestOfferings(CalculateBestDeathsCofferOfferingsRequest request) {
        return ResponseEntity.ok(offeringCalculatorService.calculateBestOfferings(request));
    }

}
