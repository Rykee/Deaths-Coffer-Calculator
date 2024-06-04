package hu.rhykee.deaths_coffer_calculator.converter;

import hu.rhykee.deaths_coffer_calculator.document.ItemDocument;
import hu.rhykee.deaths_coffer_calculator.model.jagex.JagexItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
public class ItemDocumentConverter implements Converter<JagexItem, ItemDocument> {

    @Override
    public @NonNull ItemDocument convert(@NonNull JagexItem source) {
        return ItemDocument.builder()
                .itemId(Integer.parseInt(source.getId()))
                .name(source.getName())
                .grandExchangeGuidePrice(source.getGrandExchangePrice())
                .iconPath(source.getIconPath())
                .lastGrandExchangeUpdate(OffsetDateTime.now())
                .build();
    }

}
