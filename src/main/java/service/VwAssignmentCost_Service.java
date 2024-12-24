package service;

import persistence.dao.VwAssignmentCost_DAO;
import persistence.dto.VwAssignmentCost_DTO;

public class VwAssignmentCost_Service
{
    private final VwAssignmentCost_DAO dao;

    public VwAssignmentCost_Service(VwAssignmentCost_DAO dao)
    {
        this.dao = dao;
    }

    public VwAssignmentCost_DTO getTotalCost(String studentId)
    { // 특정 학생이 납부해야 할 총 금액 조회
        return dao.getTotalCost(studentId);
    }
}