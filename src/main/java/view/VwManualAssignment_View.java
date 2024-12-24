package view;

import persistence.dto.VwManualAssignment_DTO;

import java.util.List;

public class VwManualAssignment_View
{
    // 관리자의 수동배정 기능에서 수동배정전 화면에 띄워주는 정보. 기숙사 전체와 각 기숙사별로 배정된 학생의 학번, 이름으로 구성된다.
    public String printAssignmentStd(List<VwManualAssignment_DTO> dtos)
    {
        //상단에 띄워주는 정보
        StringBuilder sb = new StringBuilder(String.format("%-3s %-6s %-4s %-3s %-8s %-12s\n", "id", "동", "방", "침대", "배정학번", "이름"));

        for (VwManualAssignment_DTO dto : dtos)
        {
            String id, name;
            if (dto.getStudent_id() == null)
            { // 비어있는 방에는 학번과 이름을 대신 지정된 문자열을 추가
                id = "--------";
                name = "-----";
            }
            else
            {
                id = dto.getStudent_id();
                name = dto.getStudent_name();
            }

            //기숙사의 각 침대 단위로, 기숙사id, 동id, 방, 침대번호, 학번, 이름을 추가한다.
            sb.append(String.format("%-3s %-5s %-5s %-4s %-11s %-12s\n", dto.getDormitory_id(), dto.getBuilding_name(), dto.getRoom(), dto.getBed(), id, name));
        }
        return sb.toString();
    }
}