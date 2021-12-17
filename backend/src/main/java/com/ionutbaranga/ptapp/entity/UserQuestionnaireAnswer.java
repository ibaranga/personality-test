package com.ionutbaranga.ptapp.entity;

import lombok.*;

import javax.persistence.*;

@Builder(toBuilder = true)
@NoArgsConstructor
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
@Entity
@Getter
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"userQuestionnaireId", "questionnaireItemId"})
)
public class UserQuestionnaireAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column
    Long userQuestionnaireId;

    @Column
    Long questionnaireItemId;

    @Column
    Long questionnaireItemOptionId;
}
