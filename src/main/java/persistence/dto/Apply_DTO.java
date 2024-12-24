package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Apply_DTO
{
    private int apply_id; // 신청 id(기본키)
    private String student_id; // 학번
    private int choice_order; // 지망 순위
    private int building_id; // 신청한 생활관 id
    private int meal_cost_id; // 신청한 식사 옵션 id
    private Byte pass; // 합격여부
}