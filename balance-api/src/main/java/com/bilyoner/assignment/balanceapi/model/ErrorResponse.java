package com.bilyoner.assignment.balanceapi.model;

import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Integer code;

    private String message;

    private LocalDateTime dateTime;
}
