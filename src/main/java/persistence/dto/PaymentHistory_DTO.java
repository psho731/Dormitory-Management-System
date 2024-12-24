package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentHistory_DTO
{ // 안쓰임
    private int payment_history_id; // 결제내역 id
    private String student_id; // 학번
    private int building_id; // 동id (1 ~ 6)
    private String history_type; // 납부 or 환불
    private int cost; // 비용
    private String bank; // 은행
    private String account_number; // 계좌번호
    private LocalDateTime date; // 날짜(시간)
}