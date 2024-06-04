package hu.rhykee.deaths_coffer_calculator.converter.mongo;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Date;

import static java.time.ZoneOffset.UTC;

@Component
public class OffsetDateTimeFromDateConverter implements Converter<Date, OffsetDateTime> {

    @Override
    public OffsetDateTime convert(Date source) {
        return source.toInstant().atOffset(UTC);
    }

}
