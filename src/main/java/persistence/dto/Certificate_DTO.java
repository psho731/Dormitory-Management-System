package persistence.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Certificate_DTO
{
    private String studentId; // 학번
    private String name; // 이름
    private String grade;// 학년
    private byte certificate_status; // 결핵진단서 제출 여부
    private int dormitoryNum; // 기숙사 번호
    private String file_path; // 결핵진단서 테이블의 경로

    public String toString()
    {
        return studentId + "  " + name + "  " + grade + "  " + certificate_status;
    }
}

