package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VwResignationApply_DTO
{ // 퇴사신청뷰 DTO
    private int building_id;
    private String building_name;
    private String student_id;
    private String student_name;
    private LocalDateTime date;
    private byte refund_status;
    private int refund_cost;
    private String bank;
    private String account_number;

    public String toString()
    { // 관리자가 관별 퇴사 신청자 조회할 때 사용
        return student_id + " | " + student_name + " | " + refund_status + " | " + String.format("%,d", refund_cost) + " | " + bank + " | " + account_number + " | " + date;
    }
}