package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
// 퇴사신청 id, 학번, 동id, 결제내역id, 퇴사일, 은행, 계좌번호, 환불여부, 환불금
public class ResignationApply_DTO // 안 쓰임
{
    private int resignation_id;
    private String student_id;
    private int building_id;
    private int payment_history_id; // 결제내역테이블의 기본키 (결제내역 테이블에 환불 관련 정보가 저장된다.)
    private LocalDateTime date;
    private String bank;
    private String account_number;
    private boolean refund_status;
    private int refund_cost;

    public String toString()
    {
        return String.format("%-9s %-7s %-4s", student_id, refund_cost, refund_status);
    }
}
