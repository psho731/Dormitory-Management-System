package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DormitoryCost_DTO
{
    private int dormitory_cost_id;
    private String dormitory_type;
    private int cost;

    public String toString()
    {
        return dormitory_cost_id + ". " + dormitory_type + " : " + String.format("%,d", cost) + "원";
    }
}