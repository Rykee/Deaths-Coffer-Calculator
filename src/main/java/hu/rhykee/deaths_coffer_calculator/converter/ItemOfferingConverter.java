package hu.rhykee.deaths_coffer_calculator.converter;

import hu.rhykee.deaths_coffer_calculator.document.ItemDocument;
import hu.rhykee.model.ItemOffering;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ItemOfferingConverter implements Converter<ItemDocument, ItemOffering> {

    @Override
    public @NonNull ItemOffering convert(@NonNull ItemDocument source) {
        long grandExchangeGuidePrice = source.getGrandExchangeGuidePrice();
        long deathsCofferValue = (long) Math.floor(grandExchangeGuidePrice * 1.05f);
        Long tradeLimit = source.getTradeLimit();
        return ItemOffering.builder()
                .id(source.getItemId())
                .name(source.getName())
                .buyPrice(source.getBuyPrice())
                .sellPrice(source.getSellPrice())
                .grandExchangeGuidePrice(grandExchangeGuidePrice)
                .lastGrandExchangeUpdate(source.getLastGrandExchangeUpdate())
                .lastRuneLiteUpdate(source.getLastRuneLiteUpdate())
                .tradeLimit(tradeLimit)
                .tradeVolume(source.getTradeVolume())
                .iconPath(source.getIconPath())
                .deathsCofferValue(deathsCofferValue)
                .priceDifference(deathsCofferValue - source.getBuyPrice())
                .roi((double) deathsCofferValue / grandExchangeGuidePrice * 100 - 100)
                .maxOfferingValue(deathsCofferValue * tradeLimit)
                .build();
    }
}
