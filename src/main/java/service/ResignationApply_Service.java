package service;

import persistence.dao.ResignationApply_DAO;

import java.time.LocalDateTime;

public class ResignationApply_Service 
{
    private final ResignationApply_DAO dao;

    public ResignationApply_Service(ResignationApply_DAO dao) 
    { 
        this.dao = dao;
    }

    // 학번, 시간, 은행, 계좌번호를 인자로 받아서 퇴사신청테이블에 삽입. 성공여부 반환(1: 성공, 0: 실패)
    public int insertResignation(String sid, LocalDateTime date, String bank, String accountNum) 
    {
        return dao.insert(sid, date, bank, accountNum, 1);
    }
}
