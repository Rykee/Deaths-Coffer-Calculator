package hu.rhykee.deaths_coffer_calculator.model.jagex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class GetCatalogueResponse {

    private long total;
    @JsonProperty("items")
    private List<JagexItem> jagexItems;

}
