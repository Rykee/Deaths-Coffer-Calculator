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
        Item item = new Item();
        item.setId(source.getItemId());
        item.setBuyPrice(source.getBuyPrice());
        item.setName(source.getName());
        item.setIconPath(source.getIconPath());
        item.setSellPrice(source.getSellPrice());
        item.setGrandExchangeGuidePrice(source.getGrandExchangeGuidePrice());
        item.setLastGrandExchangeUpdate(source.getLastGrandExchangeUpdate());
        item.setLastRuneLiteUpdate(source.getLastRuneLiteUpdate());
        item.setTradeLimit(source.getTradeLimit());
        item.setTradeVolume(source.getTradeVolume());
        return item;
    }

}
