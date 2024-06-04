package hu.rhykee.deaths_coffer_calculator.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.OffsetDateTime;

@Document(collection = "items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDocument {

    @Id
    private String id;
    private String name;
    private int itemId;
    private long grandExchangeGuidePrice;
    private long buyPrice;
    private long sellPrice;
    private OffsetDateTime lastGrandExchangeUpdate;
    private OffsetDateTime lastRuneLiteUpdate;
    private int tradeLimit;
    private int tradeVolume;
    private String iconPath;
    private byte[] iconImage;

}
