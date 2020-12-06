package ons.group8.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="personal_checklist")
public class PersonalChecklist {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User userId;
    @ManyToOne
    @JoinColumn(name="checklist_template_id")
    private ChecklistTemplate checklistTemplate;
    @Column(name="date_assigned")
    private LocalDate dateAssigned;
    @Column(name="date_complete")
    private LocalDate dateComplete;
    @OneToMany(mappedBy = "personalChecklist", cascade = {CascadeType.ALL})
    private List<ChecklistItem> checklistItems;

    public PersonalChecklist(User userId, ChecklistTemplate checklistTemplate, LocalDate dateAssigned, LocalDate dateComplete, List<ChecklistItem> items) {
        this.userId = userId;
        this.checklistTemplate = checklistTemplate;
        this.dateAssigned = dateAssigned;
        this.dateComplete = dateComplete;
        this.checklistItems = items;
    }

    public PersonalChecklist(User userId, ChecklistTemplate checklistTemplate, LocalDate dateAssigned){
        this(null, userId, checklistTemplate, dateAssigned, null);
    }
}