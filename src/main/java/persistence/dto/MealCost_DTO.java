package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealCost_DTO
{ // 식사비id, 식사타입(오름5일식, 오름7일식, 푸름0일식, 푸름5일식, 푸름7일식), 식사비용
    private int meal_cost_id;
    private String meal_option;
    private int cost;

    public String toString()
    {
        return meal_cost_id + ". " + meal_option + " : " + String.format("%,d", cost) + "원";
    }
}