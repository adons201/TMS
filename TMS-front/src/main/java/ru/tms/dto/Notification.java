package ru.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class Notification {

    private Long id;
    private String content;
    private LocalDateTime createAt;
    private String userId;
    private Boolean read;
}