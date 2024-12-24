package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VwCost_DTO
{ // 생활관과 식사관련 정보를 가지고 있는 뷰 DTO
    private String gender;
    private String building_name;
    private int recruit_number;
    private String room_type;
    private int dormitory_cost;
    private String meal_option;
    private int meal_cost;
}