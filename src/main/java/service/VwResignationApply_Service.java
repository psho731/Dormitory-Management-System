package service;

import persistence.dao.VwResignationApply_DAO;
import persistence.dto.VwResignationApply_DTO;

import java.util.ArrayList;
import java.util.List;

public class VwResignationApply_Service
{
    private final VwResignationApply_DAO dao;

    public VwResignationApply_Service(VwResignationApply_DAO dao)
    {
        this.dao = dao;
    }

    public List<List<VwResignationApply_DTO>> findOrderByBuilding()
    { // 생활관별 리스트를 조회하고, 리스트들을 묶어서 반환
      // 생활관별 조회할 때 사용
        List<List<VwResignationApply_DTO>> dtos = new ArrayList<>();

        for(int i = 1; i <= 6; i++)
            dtos.add(dao.findByBuildingID(i));

        return dtos;
    }

    public VwResignationApply_DTO findRefund(String student_id)
    { // 특정 학생의 dto를 반환
      // 환불여부 조회할 때 사용
        return dao.findRefund(student_id);
    }
}