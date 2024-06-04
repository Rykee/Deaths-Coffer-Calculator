package hu.rhykee.deaths_coffer_calculator.config.mongo;

import hu.rhykee.deaths_coffer_calculator.converter.mongo.OffsetDateTimeFromDateConverter;
import hu.rhykee.deaths_coffer_calculator.converter.mongo.OffsetDateTimeFromInstantReaderConverter;
import hu.rhykee.deaths_coffer_calculator.converter.mongo.OffsetDateTimeToDateConverter;
import hu.rhykee.deaths_coffer_calculator.converter.mongo.OffsetDateTimeToInstantWriterConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.convert.Jsr310Converters;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConverterConfig {

    @Bean
    public MongoCustomConversions customConversions(OffsetDateTimeFromInstantReaderConverter readerConverter,
                                                    OffsetDateTimeToInstantWriterConverter writerConverter,
                                                    OffsetDateTimeFromDateConverter fromDateConverter,
                                                    OffsetDateTimeToDateConverter toDateConverter) {
        List<Object> converters = new ArrayList<>();
        converters.add(readerConverter);
        converters.add(writerConverter);
        converters.add(fromDateConverter);
        converters.add(toDateConverter);
        converters.addAll(Jsr310Converters.getConvertersToRegister());
        return new MongoCustomConversions(converters);
    }

}
