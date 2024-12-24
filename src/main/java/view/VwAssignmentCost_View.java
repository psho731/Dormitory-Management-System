package view;

import persistence.dto.VwAssignmentCost_DTO;

public class VwAssignmentCost_View
{
    public String print_cost(VwAssignmentCost_DTO dto)
    { // 학생이 납부해야 할 비용을 출력
        return "비용\n" + dto + "\n";
    }
}