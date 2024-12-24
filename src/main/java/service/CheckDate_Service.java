package service;

import persistence.dao.CheckDate_DAO;
import persistence.dto.CheckDate_DTO;

import java.time.LocalDateTime;

public class CheckDate_Service
{
    private final CheckDate_DAO dao;

    public CheckDate_Service(CheckDate_DAO dao)
    {
        this.dao = dao;
    }

    // 선발일정id를 인자로 받아서 DB현재 시간이 해당 일정 기간에 포함되는지 여부를 반환
    public boolean checkDate(int id)
    {
        boolean result = true;

        //일정 dto
        CheckDate_DTO dto = dao.findDate(id);
        //DB 현재 시간
        LocalDateTime now = dao.findNow();

        // 해당 선발일정에 속하는지 비교
        if(!(dto.getStart_date().isBefore(now) && dto.getEnd_date().isAfter(now)))
            result = false;

        return result;
    }
}
