package hu.rhykee.deaths_coffer_calculator.service;

import hu.rhykee.model.CalculateBestDeathsCofferOfferingsRequest;
import hu.rhykee.model.DeathsCofferCalculationResult;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
public class OfferingCalculatorService {

    public DeathsCofferCalculationResult calculateBestOfferings(CalculateBestDeathsCofferOfferingsRequest request) {
        return null;
    }
}
