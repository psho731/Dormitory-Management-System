package service;

import persistence.dao.Schedule_DAO;
import persistence.dto.Schedule_DTO;

import java.util.List;

public class Schedule_Service 
{
    private final Schedule_DAO dao;

    public Schedule_Service(Schedule_DAO dao)
    {
        this.dao = dao;
    }

    public List<Schedule_DTO> findAll()
    { // 스케줄 테이블의 모든 내용 조회 (일정 확인할 때 사용)
        List<Schedule_DTO> all = dao.findAll();
        return all;
    }

    public void insert(String name, String startDate, String endDate)
    {// 일정 등록 (일정명과, 시작일, 종료일을 받아서 일정 추가)
     // 관리자가 사용
        Schedule_DTO dto = new Schedule_DTO();

        dto.setName(name); dto.setStart_date(startDate); dto.setEnd_date(endDate);

        dao.insert(dto);
    }

    public void delete(String scheduleId)
    { // 일정 삭제 (관리자가 일정 삭제할 때 사용)
        dao.delete(scheduleId);
    }
}