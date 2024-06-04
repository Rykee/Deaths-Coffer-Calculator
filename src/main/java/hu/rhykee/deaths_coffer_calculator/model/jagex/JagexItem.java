package hu.rhykee.deaths_coffer_calculator.model.jagex;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.IOException;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@ToString
public class JagexItem {

    private String id;
    private String name;
    @JsonProperty("current")
    private CurrentPrice grandExchangePrice;
    private long currentBuyPrice;
    private long currentSellPrice;
    @JsonProperty("icon_large")
    private String iconPath;
    private OffsetDateTime lastBuyTime;
    private OffsetDateTime lastSellTime;

    public long getGrandExchangePrice() {
        return grandExchangePrice.getPrice();
    }

    @Data
    @NoArgsConstructor
    @ToString
    public static final class CurrentPrice {
        @JsonDeserialize(using = PriceDeserializer.class)
        private long price;
    }

    private static class PriceDeserializer extends JsonDeserializer<Long> {
        @Override
        public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String price = p.getValueAsString();
            if (price.matches(".*[0-9]+")) {
                return Long.parseLong(price.replaceAll(",", ""));
            }
            double numberPart = Double.parseDouble(price.replaceAll(",", "")
                    .substring(0, price.length() - 1));
            return switch (price.charAt(price.length() - 1)) {
                case 'k' -> (long) (numberPart * 1_000);
                case 'm' -> (long) (numberPart * 1_000_000L);
                case 'b' -> (long) (numberPart * 1_000_000_000);
                default -> throw new IllegalStateException("Unexpected value: " + price.charAt(price.length() - 1));
            };
        }
    }
}
