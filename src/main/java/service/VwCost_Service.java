package service;

import persistence.dao.VwCost_DAO;
import persistence.dto.VwCost_DTO;

import java.util.List;

public class VwCost_Service
{
    private final VwCost_DAO dao;

    public VwCost_Service(VwCost_DAO dao)
    {
        this.dao = dao;
    }

    public List<VwCost_DTO> findAll()
    { // dao에서 받은 데이터 반환 (생활관비, 급식비 조회할 때 사용)
        return dao.findAll();
    }
}