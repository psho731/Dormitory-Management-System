package view;

import persistence.dto.VwResignationApply_DTO;

import java.util.List;

public class VwResignationApply_View
{
    public String printOrderByBuilding(List<List<VwResignationApply_DTO>> dtos)
    { // 생활관별 퇴사신청자 조회 (관리자가 관별 퇴사 신청자 조회할 때 사용)
        StringBuilder sb = new StringBuilder("학번  이름  환불여부  환불금액  은행  계좌번호  일자\n");

        for(List<VwResignationApply_DTO> list : dtos)
        {
            if(!list.isEmpty())
            {
                sb.append(list.get(0).getBuilding_name()).append("\n");

                for (VwResignationApply_DTO dto : list)
                    sb.append(dto).append("\n");

                sb.append("\n");
            }
        }

        return sb.toString();
    }

    public String printRefund(VwResignationApply_DTO dto)
    { // 사용자(학생)의 환불 상태를 조회할 때 사용
        if(dto != null)
        {
            String status;

            if (dto.getRefund_status() == 0)
                status = "미환불";
            else
                status = "환불";

            return "학번: " + dto.getStudent_id() + " | 이름: " + dto.getStudent_name() + " | 환불 상태: " + status + " | 금액: " + String.format("%,d", dto.getRefund_cost()) + "\n";
        }

        return "환불상태 없음";
    }
}