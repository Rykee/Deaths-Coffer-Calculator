package hu.rhykee.deaths_coffer_calculator.converter.mongo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;

@Component
@ReadingConverter
public class OffsetDateTimeFromInstantReaderConverter implements Converter<Instant, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(Instant source) {
        return OffsetDateTime.ofInstant(source, UTC);
    }

}
