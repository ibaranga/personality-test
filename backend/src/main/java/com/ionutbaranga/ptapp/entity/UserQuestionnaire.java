package com.ionutbaranga.ptapp.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Builder(toBuilder = true)
@Data
@EqualsAndHashCode(exclude = "id")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "questionnaire")
public class UserQuestionnaire {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn
    Questionnaire questionnaire;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    List<UserQuestionnaireAnswer> answers;

    @Transient
    public boolean isCompleted() {
        return questionnaire.getItems().size() == this.answers.size();
    }

}
