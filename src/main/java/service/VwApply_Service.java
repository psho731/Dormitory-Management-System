package service;

import persistence.dao.VwApply_DAO;
import persistence.dto.VwApply_DTO;

import java.util.ArrayList;
import java.util.List;

public class VwApply_Service
{
    private final VwApply_DAO dao;

    public VwApply_Service(VwApply_DAO dao)
    {
        this.dao = dao;
    }

    public List<List<VwApply_DTO>> findOrderByBuilding()
    { // 생활관별로 조회한 데이터를 리스트로 묶어서 반환 (관별 신청자 조회)
        List<List<VwApply_DTO>> dtos = new ArrayList<>();

        for(int i = 1; i <= 6; i++)
            dtos.add(dao.findByBuildingID(i));

        return dtos;
    }
}