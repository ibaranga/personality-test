package com.ionutbaranga.ptapp.entity;

import lombok.*;

import javax.persistence.*;

@Data
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class QuestionnaireItemOption {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column
    Long index;

    @Column(nullable = false)
    String value;

    @Column(nullable = false)
    Double introvertScore;

    @Column(nullable = false)
    Double extrovertScore;
}
