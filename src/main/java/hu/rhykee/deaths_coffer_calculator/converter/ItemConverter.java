package hu.rhykee.deaths_coffer_calculator.converter;

import hu.rhykee.deaths_coffer_calculator.document.ItemDocument;
import hu.rhykee.model.Item;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class ItemConverter implements Converter<ItemDocument, Item> {

    @Override
    public @NonNull Item convert(@NonNull ItemDocument source) {
        return Item.builder()
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
                .build();
    }

}
