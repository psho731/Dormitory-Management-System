package service;

import persistence.dao.StudentPoint_DAO;
import persistence.dto.StudentPoint_DTO;

import java.time.LocalDateTime;
import java.util.List;

public class StudentPoint_Service
{
    private final StudentPoint_DAO dao;

    public StudentPoint_Service(StudentPoint_DAO dao)
    {
        this.dao = dao;
    }

    public void insert(String sid, int score, LocalDateTime date, String reason)
    { // 상벌점 테이블에 내역 추가(관리자의 상벌점 부여)
        dao.insert(sid, score, date, reason);
    }

    public List<StudentPoint_DTO> findAll()
    { // 상벌점 내역 조회
        return dao.findAll();
    }

    public List<StudentPoint_DTO> findStudent(String sid)
    { // 특정 학생의 상벌점 내역 조회
        return dao.findStudent(sid);
    }
}