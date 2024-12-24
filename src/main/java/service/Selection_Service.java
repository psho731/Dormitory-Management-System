package service;

import persistence.dao.Selection_DAO;

public class Selection_Service 
{
    Selection_DAO dao;

    public Selection_Service(Selection_DAO dao)
    {
        this.dao = dao;
    }

    public void executeAutoSelection(int totalDormitoryNum)
    { //선발 실핼
        dao.executeAutoSelection(totalDormitoryNum);
    }
}