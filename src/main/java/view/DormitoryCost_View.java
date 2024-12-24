package view;

import persistence.dto.DormitoryCost_DTO;

import java.util.List;

public class DormitoryCost_View
{
    public String printAll(List<DormitoryCost_DTO> dtos)
    { // 생활관비 정보 출력 (관리자가 생활관비를 변경할 때 사용)
        StringBuilder sb = new StringBuilder("생활관비\n");

        for (DormitoryCost_DTO dto : dtos)
            sb.append(dto).append("\n");

        return sb.toString();
    }
}