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
        return ItemOffering.builder()
                .id(source.getItemId())
                .name(source.getName())
                .buyPrice(source.getBuyPrice())
                .sellPrice(source.getSellPrice())
                .grandExchangeGuidePrice(source.getGrandExchangeGuidePrice())
                .lastGrandExchangeUpdate(source.getLastGrandExchangeUpdate())
                .lastRuneLiteUpdate(source.getLastRuneLiteUpdate())
                .tradeLimit(source.getTradeLimit())
                .tradeVolume(source.getTradeVolume())
                .iconPath(source.getIconPath())
                .deathsCofferValue((long) Math.floor(source.getGrandExchangeGuidePrice() * 1.05f))
                .build();
    }
}
