package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// 배정 테이블 (배정id, 학번, 동id, 식사비id, 기숙사id(침대단위id), 결제내역id, 결핵진단서id, 비용납부여부, 진단서제출여부)
public class Assignment_DTO
{
    private int assignment_id;
    private String student_id;
    private int building_id;
    private int meal_cost_id;
    private int dormitory_id;
    private int payment_history_id;
    private int certificate_id;
    private byte payment_status;
    private byte certificate_status;
}
