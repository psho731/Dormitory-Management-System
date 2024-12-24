package view;

import persistence.dto.StudentPoint_DTO;

import java.util.List;

public class StudentPoint_View
{
    public String printPointByStudent(List<StudentPoint_DTO> dtos)
    { // 상벌점 내역과 총점 출력(학생이 자신에게 부여된 목록을 조회할 때 사용)
        StringBuilder sb = new StringBuilder("학번        점수          날짜          사유\n");
        int total = 0;

        for(StudentPoint_DTO dto : dtos)
        {
            sb.append(String.format("%-8s %-3s %-15s %-20s \n",dto.getStudent_id(),dto.getScore(),dto.getDate(),dto.getReason())).append("\n");
            total += dto.getScore();
        }

        sb.append("총점: ").append(total).append("점\n");

        return sb.toString();
    }

    public String printPointByStudentAdmin(List<StudentPoint_DTO> dtos)
    { // 상벌점 내역 출력(관리자가 모든 내역을 조회할 때 사용)
        StringBuilder sb = new StringBuilder("학번        점수          날짜          사유\n");

        for(StudentPoint_DTO dto : dtos)
        {
            sb.append(String.format("%-8s %-3s %-15s %-20s \n",dto.getStudent_id(),dto.getScore(),dto.getDate(),dto.getReason())).append("\n");
        }


        return sb.toString();
    }
}