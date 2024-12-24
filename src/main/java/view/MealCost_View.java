package view;

import persistence.dto.MealCost_DTO;

import java.util.List;

public class MealCost_View
{
    public String printAll(List<MealCost_DTO> dtos)
    { // 급식비 정보 출력 (관리자가 급식비를 변경할 때 사용)
        StringBuilder sb = new StringBuilder("급식비\n");

        for (MealCost_DTO dto : dtos)
            sb.append(dto).append("\n");

        return sb.toString();
    }
}