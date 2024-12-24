package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Schedule_DTO
{
    private String schedule_id; // 선발일정 id
    private String name; // 일정명
    private String start_date; // 시작날짜(시간)
    private String end_date; // 종료날짜(시간)

    // 사용자 화면에 출력해줄 문자열 형태로 반환
    public String getFormattedStr()
    {
        return String.format("%2s %-25s %-20s %-20s\n", schedule_id, name, start_date, end_date);
    }

    // 사용자 화면에 출력해줄 필드명
    public static String getTopLine()
    {
        return String.format("%2s %-25s %-20s %-20s\n", "id", "일정명", "시작날짜", "종료날짜");
    }
}