package com.ionutbaranga.ptapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "questionnaire")
public class QuestionnaireItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn
    @JsonIgnore // TODO use dedicated models for request/responses
    Questionnaire questionnaire;

    @Column
    String value;

    @Column
    Integer index;

    @Singular
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    List<QuestionnaireItemOption> options;
}