package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VwAssignment_DTO // AssignmentDTO와 달리 동테이블과 학생테이블을 조인한 결과를 담는 dto
{
    private String student_id;
    private String student_name;
    private int building_id;
    private String building_name;
    private String room;
    private char bed;
    private int meal_cost;
    private int dormitory_cost;
    private int total_cost;
    private byte payment_status;


    // Vw_assignment_View 에서 사용. 사용자화면에 띄워줄 포맷을 정의
    public String toString()
    {
        if(student_id != null)
            return  student_id + " | " +student_name + " | " + room + " | " + bed;
        else
            return  "--------" + " | " + "-----" + " | " + room + " | " + bed;
    }
}