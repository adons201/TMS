package ru.tms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "comments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String targetType;

    @Column(nullable = false)
    private Long targetObjectId;

    @Column(nullable = false)
    private Boolean changed;
}
