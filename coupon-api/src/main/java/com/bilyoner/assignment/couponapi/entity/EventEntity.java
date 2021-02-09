package com.bilyoner.assignment.couponapi.entity;

import com.bilyoner.assignment.couponapi.model.enums.EventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "event")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer mbs;

    @Enumerated
    @Column(nullable = false)
    private EventTypeEnum type;

    @Column(columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime eventDate;

}
