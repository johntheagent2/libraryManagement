package org.example.librarymanagement.service.criteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.librarymanagement.exception.exception.BadRequestException;
import tech.jhipster.service.Criteria;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class TimeCriteria implements Criteria {

    String fromTime;

    String toTime;

    String modifiedFromTime;

    String modifiedToTime;

    public TimeCriteria(TimeCriteria other) {
        this.fromTime = other.fromTime;
        this.toTime = other.toTime;
        this.modifiedFromTime = other.modifiedFromTime;
        this.modifiedToTime = other.modifiedToTime;
    }

    @Override
    public Criteria copy() {
        return null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        TimeCriteria that = (TimeCriteria) object;
        return Objects.equals(fromTime, that.fromTime) && Objects.equals(toTime, that.toTime) && Objects.equals(modifiedFromTime, that.modifiedFromTime) && Objects.equals(modifiedToTime, that.modifiedToTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromTime, toTime, modifiedFromTime, modifiedToTime);
    }

    public LocalDateTime getFormatFromTime() {
        LocalDateTime from;
        try {
            from = fromString(this.fromTime);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Please enter right format of date ddMMyyyy HHmmss",
                    "service.time-criteria.from-time-invalid");
        }
        return from;
    }

    public LocalDateTime getFormatToTime() {
        LocalDateTime to;
        LocalDateTime from;
        try {
            from = getFormatFromTime();
            to = fromString(this.toTime);
            if (from.isAfter(to)) {
                throw new BadRequestException("The from time must is smaller than to time",
                        "service.time-criteria.from-to-invalid");
            }

        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Please enter right format of date ddMMyyyy HHmmss",
                    "service.time-criteria.to-time-invalid");
        }
        return to;
    }

    public LocalDateTime getModifiedFromTime() {
        LocalDateTime from;
        try {
            from = fromString(this.modifiedFromTime);
        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Please enter right format of date ddMMyyyy HHmmss",
                    "service.time-criteria.from-time-invalid");
        }
        return from;
    }

    public LocalDateTime getModifiedToTime() {
        LocalDateTime to;
        LocalDateTime from;
        try {
            from = getModifiedFromTime();
            to = fromString(this.modifiedToTime);
            if (from.isAfter(to)) {
                throw new BadRequestException("The from time must is smaller than to time",
                        "service.time-criteria.from-to-invalid");
            }

        } catch (DateTimeParseException ex) {
            throw new BadRequestException("Please enter right format of date ddMMyyyy HHmmss",
                    "service.time-criteria.to-time-invalid");
        }
        return to;
    }

    private LocalDateTime fromString(String dateTime) throws DateTimeParseException {
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("ddMMyyyy HHmmss"));
    }
}
