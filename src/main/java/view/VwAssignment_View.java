package view;

import persistence.dto.VwAssignment_DTO;

import java.util.List;

public class VwAssignment_View
{
    // 선발내역조회 기능에서, 화면에 출력할 형태로 문자열을 만들어서 반환
    public String printAll(List<List<VwAssignment_DTO>> dtos)
    {
        StringBuilder sb = new StringBuilder();

        for(List<VwAssignment_DTO> list : dtos)
        {
            if(!list.isEmpty())
            {
                sb.append(list.get(0).getBuilding_name()).append("\n");
                sb.append("학번        이름          기숙사\n");

                for (VwAssignment_DTO dto : list)
                {
                    if (dto.getStudent_id() == null)
                        sb.append(String.format("%-8s   %-8s   %-4s", dto.getStudent_id(), dto.getStudent_name(), dto.getBuilding_name())).append("\n");

                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    // 학생의 호실확인 기능에서 출력할 형태로 문자열을 만들어서 반환, 불합격 학생이라면 null이 넘어오므로 "불합격" 문자열을 반환
    public String printStudentID(VwAssignment_DTO dto)
    {
        if(dto == null)
            return "불합격\n";

        return "생활관: " + dto.getBuilding_name() + ", 호실: " + dto.getRoom() + ", 침대번호: " + dto.getBed() + "\n";
    }

    // 기숙사 동별로 배정된 학생을 조회 (학번, 이름, 호실, 침대번호)
    public String printOrderByBuilding(List<List<VwAssignment_DTO>> dtos)
    {
        StringBuilder sb = new StringBuilder();

        for(List<VwAssignment_DTO> list : dtos) // 건물별로 문자열에 추가
        {
            if(!list.isEmpty())
            {
                sb.append(list.get(0).getBuilding_name()).append("\n"); //건물이름 추가
                sb.append(String.format("%-9s %-4s %-1s %-1s\n", "학번",  "이름", " 호실",  "침대번호")); // 포맷 추가

                for (VwAssignment_DTO dto : list)
                    sb.append(dto).append("\n"); //각 학생별 학번, 이름, 호실, 침대번호 추가

                sb.append("\n");
            }
        }

        return sb.toString();
    }

    //출력할 납부자/미납부자 정보 문자열을 만드는 메소드
    public String printPaymentStudent(List<List<VwAssignment_DTO>> dtos)
    {
        //납부자/미납부자 정보 구분자 문자열을 담은 StringBuilder 객체
        StringBuilder sb = new StringBuilder("학번  이름  납부여부  금액\n");

        for(List<VwAssignment_DTO> list : dtos)
        {
            if(!list.isEmpty())
            {
                String status = "미납부";
                sb.append(list.get(0).getBuilding_name()).append("\n"); //기숙사 동 이름을 입력

                if(list.get(0).getPayment_status() == 1) //refund_status 필드가 1이라면
                {
                    status = "납부";
                }

                for (VwAssignment_DTO dto : list)
                    //납부 정보 입력
                    sb.append(dto.getStudent_id()).append("  ").append(dto.getStudent_name()).append("  ").append(status).append("  ").append(dto.getTotal_cost()).append("\n");

                sb.append("\n");
            }
        }

        return sb.toString();
    }
}