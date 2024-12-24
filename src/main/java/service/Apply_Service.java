package service;

import persistence.dao.Apply_DAO;
import persistence.dto.Apply_DTO;

public class Apply_Service
{
    private final Apply_DAO dao;

    public Apply_Service(Apply_DAO dao)
    {
        this.dao = dao;
    }

    public byte findPassByStudentID(String student_id)
    { // 특정 학생의 합격 결과 조회 (학생이 합격 결과 조회할 때 사용)
        Apply_DTO dto = dao.findByStudentID(student_id);

        if(dto != null)
            return dto.getPass();

        return -1;
    }

    public boolean insert(String student_id, String building_name1, String meal_option1, String building_name2, String meal_option2)
    { // 입사 신청
        // 입력 형식이 맞지 않거나, 1지망과 2지망이 같거나, 이미 지원했거나, 선발된 학생이라면 지원 실패 처리
        if(!building_name1.substring(0, 2).equals(meal_option1.substring(0, 2)) || !building_name2.substring(0, 2).equals(meal_option2.substring(0, 2)) || building_name1.equals(building_name2) || (0 < dao.countByStudentID(student_id)))
            return false;

        int ok;
        // 1지망 삽입
        ok = dao.insert(student_id, 1, building_name1, meal_option1);
        // 1지망 성공 시
        if(0 < ok)
        {
            // 2지망 삽입
            ok = dao.insert(student_id, 2, building_name2, meal_option2);
            // 지원 성공 처리
            if(0 < ok)
                return true;
            else // 2지망 실패 시 1지망 삭제
                dao.delete(student_id);
        }

        return false;
    }
}