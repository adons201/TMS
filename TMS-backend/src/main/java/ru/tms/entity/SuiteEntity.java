package ru.tms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "suite")
@Getter
@Setter
public class SuiteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String description;

    @ManyToOne()
    @NotFound(action = NotFoundAction.EXCEPTION)
    @JoinColumn(name = "projectId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ProjectEntity project;

    @ManyToOne()
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "parentId")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SuiteEntity parentId;

}
