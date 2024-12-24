package view;

import persistence.dto.Schedule_DTO;

import java.util.List;

public class Schedule_View
{
    public String printAll(List<Schedule_DTO> dtos)
    { // 일정 출력 (사용자가 일정을 조회할 때 사용)
        StringBuilder sb = new StringBuilder();

        sb.append(Schedule_DTO.getTopLine());

        for (Schedule_DTO dto : dtos)
            sb.append(dto.getFormattedStr());

        return sb.toString();
    }
}