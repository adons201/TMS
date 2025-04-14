package ru.tms.entity;


import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.*;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Entity
@Table(name = "test")
@Getter
@Setter
public class TestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String status;

    @Column
    private String description;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "suiteId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SuiteEntity suiteId;

    @ManyToOne
    @NotFound(action = NotFoundAction.EXCEPTION)
    @JoinColumn(name = "projectId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectEntity projectId;

    @Column
    private Boolean automated;

    @JdbcTypeCode(SqlTypes.JSON)
    private List<Step> steps;
}
