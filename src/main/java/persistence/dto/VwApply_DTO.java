package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VwApply_DTO
{ // 지원자의 지망과 희망 생활관, 식사옵션, 학생타입(학부생, 대학원생), 총점을 가지는 뷰 DTO
    public String student_id;
    public String student_name;
    public int choice_order;
    public int building_id;
    public String building_name;
    public String meal_option;
    public String student_type;
    public double total_score;

    public String toString()
    { // 입사 신청자 조회할 때 사용
        return String.format("%-8s | %-8s | %-1s | %-4s | %-5s | %-5s", student_id , student_name , choice_order ,student_type , total_score, meal_option ) ;
    }
}