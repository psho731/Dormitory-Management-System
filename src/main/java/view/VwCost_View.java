package view;

import persistence.dto.VwCost_DTO;

import java.util.List;

public class VwCost_View
{
    public String printFormat(List<VwCost_DTO> dtos)
    { // 데이터를 특정 형식에 맞춰 출력 (생활관비, 급식비 조회할 때 사용)
        StringBuilder sb = new StringBuilder("성별   생활관   모집인원  유형     생활관비     식사옵션    식사비용\n");
        String gender = "";
        String building_name = "";
        int max_cost_len = 0, max_number_len = 0;

        for (VwCost_DTO dto : dtos)
        {
            if(max_cost_len < String.format("%,d", dto.getDormitory_cost()).length())
                max_cost_len = String.format("%,d", dto.getDormitory_cost()).length();

            if(max_number_len < String.format("%,d", dto.getRecruit_number()).length())
                max_number_len = String.format("%,d", dto.getRecruit_number()).length();
        }

        StringBuilder dormitory_cost_len = new StringBuilder();
        StringBuilder recruit_number_len = new StringBuilder();

        for(int i = 0; i < max_cost_len; i++)
            dormitory_cost_len.append(" ");

        for(int i = 0; i < max_number_len; i++)
            recruit_number_len.append(" ");

        for (VwCost_DTO dto : dtos)
        {
            StringBuilder diff_dormitory_cost_len = new StringBuilder();
            int diff_dormitory = max_cost_len - String.format("%,d", dto.getDormitory_cost()).length();

            for(int i = 0; i < diff_dormitory; i++)
                diff_dormitory_cost_len.append(" ");

            StringBuilder diff_recruit_number_len = new StringBuilder();

            int diff_recruit = max_number_len - String.valueOf(dto.getRecruit_number()).length();

            for(int i = 0; i < diff_recruit; i++)
                diff_recruit_number_len.append(" ");

            if(gender.equals(dto.getGender()))
            {
                sb.append("      ");

                if(building_name.equals(dto.getBuilding_name()))
                {
                    sb.append("                     ");
                    sb.append(dormitory_cost_len);
                    sb.append(recruit_number_len);
                }
                else
                {
                    building_name = dto.getBuilding_name();
                    sb.append(dto.getBuilding_name()).append(" | ").append(diff_recruit_number_len).append(dto.getRecruit_number()).append("명").append(" | ").append(dto.getRoom_type()).append(" | ").append(diff_dormitory_cost_len).append(String.format("%,d", dto.getDormitory_cost())).append("원");
                }
            }
            else
            {
                gender = dto.getGender();
                building_name = dto.getBuilding_name();
                sb.append(dto.getGender()).append(" | ").append(dto.getBuilding_name()).append(" | ").append(diff_recruit_number_len).append(dto.getRecruit_number()).append("명").append(" | ").append(dto.getRoom_type()).append(" | ").append(diff_dormitory_cost_len).append(String.format("%,d", dto.getDormitory_cost())).append("원");
            }

            sb.append(" | ").append(dto.getMeal_option()).append(" | ").append(String.format("%,d", dto.getMeal_cost())).append("원").append("\n");
        }
        return sb.toString();
    }
}