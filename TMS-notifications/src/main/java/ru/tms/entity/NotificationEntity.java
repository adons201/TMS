package ru.tms.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne()
    @JoinColumn(name = "subscription_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SubscriptionEntity subscription;

    @Column(nullable = false)
    private Boolean read;
}