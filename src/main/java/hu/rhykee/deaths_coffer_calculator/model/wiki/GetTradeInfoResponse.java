package hu.rhykee.deaths_coffer_calculator.model.wiki;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
public class GetTradeInfoResponse {

    private Map<String, TradeInfo> data;

    @Data
    @NoArgsConstructor
    public static class TradeInfo {
        private long highPriceVolume;
        private long lowPriceVolume;
    }

}
