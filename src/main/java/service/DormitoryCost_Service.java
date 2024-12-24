package service;

import persistence.dao.DormitoryCost_DAO;
import persistence.dto.DormitoryCost_DTO;

import java.util.List;

public class DormitoryCost_Service
{
    private final DormitoryCost_DAO dao;

    public DormitoryCost_Service(DormitoryCost_DAO dao)
    {
        this.dao = dao;
    }

    public List<DormitoryCost_DTO> findAll()
    { // 모든 생활관비 조회 (관리자가 생활관비 조회할 때 사용)
        List<DormitoryCost_DTO> all = dao.findAll();

        return all;
    }

    public int updateCost(int dormitory_cost_id, int cost)
    { // dormitory_cost_id와 비용을 받아와 생활관비 업데이트
      // 성공 시 1 반환
        return dao.updateCost(dormitory_cost_id, cost);
    }
}