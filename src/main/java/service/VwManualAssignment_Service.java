package service;

import persistence.dao.VwManualAssignment_DAO;
import persistence.dto.VwManualAssignment_DTO;

import java.util.List;

public class VwManualAssignment_Service
{
    private final VwManualAssignment_DAO dao;

    public VwManualAssignment_Service(VwManualAssignment_DAO dao)
    {
        this.dao = dao;
    }

    // 관리자의 수동배정 기능에서 화면에 띄워줄 정보
    // (기숙사id, 건물이름, 방, 침대번호, 학번, 이름) (전체 기숙사id와 각 기숙사방에 배정된 학생정보)
    public List<VwManualAssignment_DTO> findAssignmentStd()
    {
        return dao.findAssignmentStd();
    }
}
