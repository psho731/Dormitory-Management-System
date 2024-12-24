package view;

import persistence.dto.VwApply_DTO;

import java.util.List;

public class VwApply_View
{
    public String printOrderByBuilding(List<List<VwApply_DTO>> dtos)
    { // 생활관별 입사 신청자 조회 (관리자가 관별 신청자 조회할 때 사용)
        StringBuilder sb = new StringBuilder("학번        이름       지망  타입   총점   식사옵션\n");

        for(List<VwApply_DTO> list : dtos)
        {
            if(!list.isEmpty())
            {
                sb.append(list.get(0).building_name).append("\n");

                for (VwApply_DTO dto : list)
                    sb.append(dto).append("\n");

                sb.append("\n");
            }
        }

        return sb.toString();
    }
}