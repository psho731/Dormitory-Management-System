package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VwAssignmentCost_DTO
{ // 지원자의 비용을 가지는 뷰의 DTO
    private String student_id;
    private int dormitoryCost;
    private int mealCost;
    private int totalCost;

    public String toString()
    { // 최종비용 조회할 때 사용
        return "생활관비: " + String.format("%,d", dormitoryCost) + " + 급식비: " + String.format("%,d", mealCost) + " = 총금액: " + String.format("%,d", totalCost);
    }
}