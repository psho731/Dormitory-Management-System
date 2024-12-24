package view;

import persistence.dto.Certificate_DTO;

import java.util.List;

public class Certificate_View
{
    public String printCertificateFilePath(Certificate_DTO dto)
    {
        return dto.getFile_path();
    }

    // 결핵 진단서 제출자 리스트(CertificateDTO 리스트)를 인자로 받아서 문자열에 동별로 저장하고 반환
    public String print_orderByBuilding(List<List<Certificate_DTO>> dtos)
    {
        StringBuilder sb = new StringBuilder();

        for(List<Certificate_DTO> list : dtos)
        {
            if(!list.isEmpty())
            {
                // 건물 이름 배열
                String[] bname = {"오름 1동","오름 2동","푸름 1동","푸름 2동","푸름 3동","푸름 4동"};
                int idx =list.get(0).getDormitoryNum(); // getDormitoryNum()의 반환값은 오름1동, 오름2동, 푸름1동 ... 순으로 1, 2, 3, ... 이다.
                sb.append(bname[idx-1]).append("\n\n"); // 해당 건물 이름을 먼저 보여줌.
                sb.append(String.format("%-8s %-12s %-4s %-3s\n", "학번", "이름", "학년", "제출여부")); // 필드 명을 보여줌

                // 해당 건물에 배정된 학생 별로, 진단서 제출자를 차례로 보여줌
                for (Certificate_DTO dto : list)
                    sb.append(String.format("%-8s %-12s %-4s %-3s\n", dto.getStudentId(), dto.getName(), dto.getGrade(), "제출"));

                sb.append("\n");
            }
        }

        return sb.toString();
    }
}