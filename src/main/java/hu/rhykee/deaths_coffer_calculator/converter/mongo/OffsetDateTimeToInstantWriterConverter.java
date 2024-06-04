package hu.rhykee.deaths_coffer_calculator.converter.mongo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;

@Component
@WritingConverter
public class OffsetDateTimeToInstantWriterConverter implements Converter<OffsetDateTime, Instant> {

    @Override
    public Instant convert(OffsetDateTime source) {
        return source.toInstant();
    }

}
