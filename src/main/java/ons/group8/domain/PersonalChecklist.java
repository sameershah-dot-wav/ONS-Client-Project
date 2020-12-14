package ons.group8.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="personal_checklist")
public class PersonalChecklist {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name="checklist_template_id")
    private ChecklistTemplate checklistTemplate;
    @NotBlank(message="Date Assigned field is mandatory")
    @Column(name="date_assigned", nullable=false)
    private LocalDate dateAssigned;
    @Column(name="date_complete")
    private LocalDate dateComplete;
    @OneToMany(mappedBy = "personalChecklist", cascade = {CascadeType.ALL})
    private List<ChecklistItem> checklistItems;



    public PersonalChecklist(User user, ChecklistTemplate checklistTemplate, LocalDate dateAssigned, List<ChecklistItem> items){
        this(null, user, checklistTemplate, dateAssigned, null, items);
    }
}
