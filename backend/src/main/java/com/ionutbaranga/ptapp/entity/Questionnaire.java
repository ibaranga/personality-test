package com.ionutbaranga.ptapp.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Questionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    Long id;

    @Column
    String name;

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "questionnaire", fetch = FetchType.EAGER)
    List<QuestionnaireItem> items;

    public void setItems(List<QuestionnaireItem> items) {
        items.forEach(item -> item.setQuestionnaire(this));
        this.items = items;
    }
}
