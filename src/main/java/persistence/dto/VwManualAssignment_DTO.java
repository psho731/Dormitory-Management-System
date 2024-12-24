package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VwManualAssignment_DTO
{
    private int dormitory_id;
    private int building_id;
    private String building_name;
    private String room;
    private char bed;
    private String student_id;
    private String student_name;
}
