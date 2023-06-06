package ru.ecommerce.highstylewear.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@MappedSuperclass
public abstract class GenericModel {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_generator")
    private Long id;

    @Column(name = "created_when")
    private LocalDateTime createdWhen;

    @Column(name = "by")
    private String createdBy;

    @Column(name = "deleted_when")
    private LocalDateTime deletedWhen;

    @Column(name = "deleted_By")
    private String deletedBy;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private Boolean isDeleted;
}
