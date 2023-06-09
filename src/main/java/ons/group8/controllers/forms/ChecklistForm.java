package ons.group8.controllers.forms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ons.group8.domain.checklist.Topic;
import org.springframework.context.annotation.Scope;


import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Scope("session") // this applies to a user's individual session
public class ChecklistForm {
    private String title;
    private String titleDescription;
    private List<Topic> topics;
    private List<Long> assignedTo;
    private String deadline;

    public List<Topic> getTopics(){
        if (topics == null){
            topics = new ArrayList<>();
        }
        return topics;
    }

    public List<Long> getAssignedTo(){
        if (topics == null){
            topics = new ArrayList<>();
        }
        return assignedTo;
    }
}
