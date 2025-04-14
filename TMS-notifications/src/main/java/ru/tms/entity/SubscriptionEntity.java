package ru.tms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Subscriptions")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String targetType;

    @Column(nullable = false)
    private Long targetObjectId;
}