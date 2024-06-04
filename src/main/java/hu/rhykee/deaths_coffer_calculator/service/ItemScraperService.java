package hu.rhykee.deaths_coffer_calculator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.rhykee.deaths_coffer_calculator.config.AppConfig;
import hu.rhykee.deaths_coffer_calculator.document.ItemDocument;
import hu.rhykee.deaths_coffer_calculator.model.jagex.GetCatalogueResponse;
import hu.rhykee.deaths_coffer_calculator.model.jagex.JagexItem;
import hu.rhykee.deaths_coffer_calculator.model.wiki.GetLatestPricesResponse;
import hu.rhykee.deaths_coffer_calculator.repository.ItemRepository;
import hu.rhykee.deaths_coffer_calculator.repository.ScrapeRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = PRIVATE)
@Log4j2
public class ItemScraperService {

    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    ScrapeRepository scrapeRepository;
    ItemRepository itemRepository;
    RestClient restClient;
    AppConfig appConfig;
    ObjectMapper objectMapper;
    ConversionService conversionService;

    public void scrapeRuneLitePrices() {
        Map<String, GetLatestPricesResponse.PriceDetail> latestWikiPrices = restClient.get()
                .uri(appConfig.getWikiPriceUrl())
                .retrieve()
                .body(GetLatestPricesResponse.class)
                .getData();
        latestWikiPrices.forEach((itemId, priceDetail) -> {
            int id = Integer.parseInt(itemId);
            Optional<ItemDocument> itemDoc = itemRepository.findByItemId(id);
            itemDoc.ifPresent(itemDocument -> {
                setRuneLiteFields(itemDocument, priceDetail);
                itemRepository.save(itemDocument);
            });
        });
    }

    public void scrapeGrandExchangePrices() throws InterruptedException, IOException {
        List<JagexItem> jagexItems = getAllItemsFromGe().getJagexItems();
        Map<Integer, ItemDocument> itemsById = itemRepository.findAll().stream()
                .collect(Collectors.toMap(ItemDocument::getItemId, Function.identity()));
        jagexItems.forEach(jagexItem -> {
            int itemId = Integer.parseInt(jagexItem.getId());
            itemsById.computeIfPresent(itemId, (_, itemDocument) -> {
                itemDocument.setGrandExchangeGuidePrice(jagexItem.getGrandExchangePrice());
                return itemDocument;
            });
            itemsById.computeIfAbsent(itemId, _ -> conversionService.convert(jagexItem, ItemDocument.class));
        });
        itemRepository.saveAll(itemsById.values());
    }

    private GetCatalogueResponse getAllItemsFromGe() throws InterruptedException, IOException {
        GetCatalogueResponse response = new GetCatalogueResponse();
        response.setJagexItems(new ArrayList<>());
        for (char letter : ALPHABET) {
            GetCatalogueResponse grandExchangeCatalog = new GetCatalogueResponse();
            int page = 1;
            do {
                Thread.sleep(4000);
                log.info("Calling with character {} with page {}", letter, page);
                String items = restClient.get()
                        .uri(getJagexUri(letter, page))
                        .retrieve()
                        .body(String.class);
                if (items == null) {
                    continue;
                }
                grandExchangeCatalog = objectMapper.reader().readValue(items, GetCatalogueResponse.class);
                response.getJagexItems().addAll(grandExchangeCatalog.getJagexItems());
                page++;
            } while (!grandExchangeCatalog.getJagexItems().isEmpty());
        }
        return response;
    }

    private URI getJagexUri(char c, int page) {
        String baseUrl = appConfig.getJagexPriceUrl();
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("category", 1)
                .queryParam("alpha", Character.toString(c))
                .queryParam("page", page)
                .build()
                .toUri();
    }

    private void setRuneLiteFields(ItemDocument itemDocument, GetLatestPricesResponse.PriceDetail priceDetail) {
        itemDocument.setBuyPrice(priceDetail.getCurrentBuyingPrice());
        itemDocument.setSellPrice(priceDetail.getCurrentSellingPrice());
        itemDocument.setLastRuneLiteUpdate(OffsetDateTime.now());
    }

}
