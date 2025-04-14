package ru.tms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class Subscription {

    private Long id;
    private String userId;
    private String targetType;
    private Long targetObjectId;
}
