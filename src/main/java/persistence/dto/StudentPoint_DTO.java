package persistence.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StudentPoint_DTO
{
    private int point_id; // 상벙점 테이블 기본키
    private String student_id; // 학번
    private int score; // 점수
    private String reason; // 이유
    private LocalDateTime date; // 부여 날짜

    public String tostring()
    { // 상벌점 출력에 사용
        return student_id + " " + score + " " + reason + " " + date + "\n";
    }
}