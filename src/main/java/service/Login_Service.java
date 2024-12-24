package service;

import persistence.dao.Login_DAO;

public class Login_Service 
{
    private final Login_DAO dao;

    public Login_Service(Login_DAO dao)
    {
        this.dao = dao;
    }

    // id, password를 인자로 받아서 사용자 type을 반환. "s"는 학생, "a"는 관리자 "n"은 실패
    public String accountPrint(String Id, String Pw)
    {
        return dao.findAccount(Id, Pw);
    }
}