package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
//선발일정테이블의 일정 시작날짜와 종료날짜를 저장하는 DTO
public class CheckDate_DTO
{
    private LocalDateTime start_date;
    private LocalDateTime end_date;
}
