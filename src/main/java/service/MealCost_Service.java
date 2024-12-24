package service;

import persistence.dao.MealCost_DAO;
import persistence.dto.MealCost_DTO;

import java.util.List;

public class MealCost_Service
{
    private final MealCost_DAO dao;

    public MealCost_Service(MealCost_DAO dao)
    {
        this.dao = dao;
    }

    public List<MealCost_DTO> findAll()
    { // 모든 급식비 조회 (관리자가 급식비 조회할 때 사용)
        List<MealCost_DTO> all = dao.findAll();
        return all;
    }

    public int updateCost(int meal_cost_id, int cost)
    { // meal_cost_id와 비용을 받아와 급식비 업데이트
        // 성공 시 1 반환
        return dao.updateCost(meal_cost_id, cost);
    }
}