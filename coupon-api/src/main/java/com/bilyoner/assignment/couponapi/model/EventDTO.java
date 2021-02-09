package com.bilyoner.assignment.couponapi.model;

import com.bilyoner.assignment.couponapi.entity.EventEntity;
import com.bilyoner.assignment.couponapi.model.enums.EventTypeEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class EventDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Integer mbs;

    @NotNull
    private EventTypeEnum type;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime eventDate;

    public static EventDTO mapToEventDTO(EventEntity eventEntity) {
        return EventDTO.builder()
                       .id(eventEntity.getId())
                       .name(eventEntity.getName())
                       .mbs(eventEntity.getMbs())
                       .type(eventEntity.getType())
                       .eventDate(eventEntity.getEventDate())
                       .build();
    }

    public static EventEntity mapToEventEntity(EventDTO dto) {
        return EventEntity.builder().id(dto.getId()).name(dto.getName()).mbs(dto.getMbs()).type(dto.getType()).eventDate(dto.getEventDate()).build();
    }
}
