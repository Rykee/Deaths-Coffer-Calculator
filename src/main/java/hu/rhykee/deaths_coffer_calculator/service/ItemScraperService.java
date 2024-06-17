package hu.rhykee.deaths_coffer_calculator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.rhykee.deaths_coffer_calculator.config.AppConfig;
import hu.rhykee.deaths_coffer_calculator.document.ItemDocument;
import hu.rhykee.deaths_coffer_calculator.model.jagex.GetCatalogueResponse;
import hu.rhykee.deaths_coffer_calculator.model.jagex.JagexItem;
import hu.rhykee.deaths_coffer_calculator.model.wiki.GetLatestPricesResponse;
import hu.rhykee.deaths_coffer_calculator.model.wiki.GetTradeInfoResponse;
import hu.rhykee.deaths_coffer_calculator.model.wiki.ItemInfo;
import hu.rhykee.deaths_coffer_calculator.repository.ItemRepository;
import hu.rhykee.deaths_coffer_calculator.repository.ScrapeRepository;
import hu.rhykee.deaths_coffer_calculator.util.NonNullConversionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
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
    NonNullConversionService conversionService;

    public void scrapeRuneLitePrices() {
        log.info("Getting RuneLite prices...");
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
        log.info("Finished Getting RuneLite prices.");
    }

    public void scrapeGrandExchangePrices() throws InterruptedException, IOException {
        log.info("Getting GrandExchange prices...");
        List<JagexItem> jagexItems = getAllItemsFromGe().getJagexItems();
        Map<Integer, ItemDocument> itemsById = itemRepository.findAll().stream()
                .collect(Collectors.toMap(ItemDocument::getItemId, Function.identity()));
        jagexItems.forEach(jagexItem -> {
            int itemId = Integer.parseInt(jagexItem.getId());
            itemsById.computeIfPresent(itemId, (_, itemDocument) -> {
                itemDocument.setGrandExchangeGuidePrice(jagexItem.getGrandExchangePrice());
                itemDocument.setIconPath(jagexItem.getIconPath());
                itemDocument.setLastGrandExchangeUpdate(OffsetDateTime.now());
                return itemDocument;
            });
            itemsById.computeIfAbsent(itemId, _ -> conversionService.convert(jagexItem, ItemDocument.class));
        });
        itemRepository.saveAll(itemsById.values());
        log.info("Finished Getting GrandExchange prices.");
    }

    public void scrapeTradeLimit() throws JsonProcessingException {
        log.info("Getting trade limits.");
        String body = restClient.get()
                .uri(appConfig.getWikiItemInfoUrl())
                .retrieve()
                .body(String.class);
        List<ItemInfo> itemInfos = objectMapper.readerForListOf(ItemInfo.class).readValue(body);
        Map<Integer, Long> limitsById = itemInfos.stream()
                .collect(Collectors.toMap(ItemInfo::getId, ItemInfo::getLimit));
        List<ItemDocument> items = itemRepository.findAll();
        items.forEach(itemDocument -> itemDocument.setTradeLimit(limitsById.get(itemDocument.getItemId())));
        itemRepository.saveAll(items);
        log.info("Finished setting limits");
    }

    public void scrapeTradeVolume() {
        log.info("Getting trade volume for last hour");
        Map<Integer, Long> lastHourVolumeById = restClient.get()
                .uri(appConfig.getWikiTradeInfoUrl())
                .retrieve()
                .body(GetTradeInfoResponse.class)
                .getData().entrySet().stream()
                .collect(Collectors.toMap(entry -> Integer.parseInt(entry.getKey()),
                        entry -> entry.getValue().getLowPriceVolume() + entry.getValue().getHighPriceVolume()));
        List<ItemDocument> items = itemRepository.findAll();
        items.forEach(itemDocument -> {
            Long tradeVolume = lastHourVolumeById.get(itemDocument.getItemId());
            itemDocument.setTradeVolume(tradeVolume == null ? 0 : tradeVolume);
        });
        itemRepository.saveAll(items);
        log.info("Finished setting trade volume");
    }

    private GetCatalogueResponse getAllItemsFromGe() throws InterruptedException, IOException {
        GetCatalogueResponse response = new GetCatalogueResponse();
        response.setJagexItems(new ArrayList<>());
        for (char letter : ALPHABET) {
            log.info("Getting item info starting with letter: {} from GrandExchange", letter);
            GetCatalogueResponse grandExchangeCatalog = new GetCatalogueResponse();
            int page = 1;
            do {
                Thread.sleep(4000);
                String items = restClient.get()
                        .uri(getJagexUri(letter, page))
                        .retrieve()
                        .body(String.class);
                if (items == null) {
                    log.info("Jagex cant handle all these requests smh");
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
