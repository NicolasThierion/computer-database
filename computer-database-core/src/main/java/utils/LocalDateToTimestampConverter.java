package utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class LocalDateToTimestampConverter implements AttributeConverter<LocalDate, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(LocalDate entityAttribute) {
        if (entityAttribute == null) {
            return null;
        }
        return Timestamp.valueOf(LocalDateTime.of(entityAttribute, LocalTime.MIN));
    }

    @Override
    public LocalDate convertToEntityAttribute(Timestamp column) {
        if (column == null) {
            return null;
        }
        return column.toLocalDateTime().toLocalDate();
    }

}