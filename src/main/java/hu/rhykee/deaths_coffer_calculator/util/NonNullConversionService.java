package hu.rhykee.deaths_coffer_calculator.util;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@Component
@FieldDefaults(makeFinal = true, level = PRIVATE)
@RequiredArgsConstructor
public class NonNullConversionService {

    ConversionService conversionService;

    /**
     * Overloads the basic {@link ConversionService#convert(Object, Class)} method in a way it handles null checks.
     *
     * @param source      The source object.
     * @param targetClass The target class
     * @param <U>         Type of the target object.
     * @param <T>         Type of the source object.
     * @return The result of the conversion.
     * @throws IllegalArgumentException If the source object is null.
     * @throws IllegalStateException    If the result of the conversion is null.
     */
    public <U, T> U convert(T source, Class<U> targetClass) {
        if (source == null) {
            throw new IllegalArgumentException("Source object must be non-null");
        }
        U converted = conversionService.convert(source, targetClass);
        if (converted == null) {
            throw new IllegalStateException("ConversionService returned null");
        }
        return converted;
    }

    /**
     * Converts a collection of objects to another type using a {@link NonNullConversionService} and returns the converted items in a list.
     *
     * @param source      The source collection.
     * @param targetClass The target class of the items.
     * @param <T>         The type of the source objects.
     * @param <U>         The type of the target objects.
     * @return The mapped list of objects.
     */
    public <T, U> List<U> convertList(Collection<T> source, Class<U> targetClass) {
        return convertList(source, targetClass, item -> convert(item, targetClass));
    }

    /**
     * Converts a collection of objects to another type using the provided mapper function, and returns the converted items in a list.
     *
     * @param source         The source collection.
     * @param targetClass    The target class of the items.
     * @param mapperFunction A {@link Function} that will be used to convert the objects.
     * @param <T>            The type of the source objects.
     * @param <U>            The type of the target objects.
     * @return The mapped list of objects.
     */
    public <T, U> List<U> convertList(Collection<T> source, Class<U> targetClass, Function<T, U> mapperFunction) {
        return source.stream()
                .map(mapperFunction)
                .toList();
    }
}
