package service;

import persistence.dao.Assignment_DAO;
import persistence.dao.Building_DAO;
import persistence.dto.Assignment_DTO;

import java.util.List;

public class Assignment_Service
{
    private final Assignment_DAO dao;

    public Assignment_Service(Assignment_DAO dao)
    {
        this.dao = dao;
    }


    public boolean manualAssignmentSwap(String sid1, String sid2)
    {
        return dao.manualAssignmentSwap(sid1, sid2);
    }

    public void autoAssignment()
    {
        Building_DAO buildingDAO = new Building_DAO();
        int[] buildingRecruitNum = new int[6];
        for(int idx = 0; idx < buildingRecruitNum.length; idx++)
            buildingRecruitNum[idx] = buildingDAO.findRecruitNumber(idx+1);

        for(int i = 1; i <= 6; i++)
        {
            List<Assignment_DTO> list = dao.findBuilding(i);
            if(!list.isEmpty())
            {
                buildingDAO.updateRecruitNum(i, buildingRecruitNum[i-1] - list.size());
                dao.findEmpty(list, i);
            }
        }
    }
}