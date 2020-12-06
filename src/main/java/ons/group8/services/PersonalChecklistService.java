package ons.group8.services;

import ons.group8.domain.ChecklistItem;
import ons.group8.domain.PersonalChecklist;

public interface PersonalChecklistService {

    PersonalChecklist findByUserId_Id(Long userId);

    ChecklistItem findByPersonalChecklist_Id(Long personalCheckId);
}