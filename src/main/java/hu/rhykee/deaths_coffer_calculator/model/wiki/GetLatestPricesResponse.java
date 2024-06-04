package hu.rhykee.deaths_coffer_calculator.model.wiki;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

@Data
@NoArgsConstructor
public class GetLatestPricesResponse {

    private Map<String, PriceDetail> data;

    @NoArgsConstructor
    @Data
    public static class PriceDetail {

        @JsonProperty("high")
        private long currentBuyingPrice;
        @JsonDeserialize(using = EpochSecondsDeserializer.class)
        @JsonProperty("highTime")
        private OffsetDateTime lastBuyTime;
        @JsonProperty("low")
        private long currentSellingPrice;
        @JsonDeserialize(using = EpochSecondsDeserializer.class)
        @JsonProperty("lowTime")
        private OffsetDateTime lastSellTime;
    }

    private static class EpochSecondsDeserializer extends JsonDeserializer<OffsetDateTime> {
        @Override
        public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            long epochSeconds = p.getValueAsLong();
            Instant instant = Instant.ofEpochSecond(epochSeconds);
            return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC); // Or any other zone offset you prefer
        }
    }
}
