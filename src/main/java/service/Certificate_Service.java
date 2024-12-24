package service;

import persistence.dao.Certificate_DAO;
import persistence.dto.Certificate_DTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Certificate_Service
{
    private final Certificate_DAO dao;

    public Certificate_Service(Certificate_DAO dao)
    {
        this.dao = dao;
    }

    // 학번, 날짜, 파일의 경로(TCP서버에 저장된 파일경로)를 인자로 받아서 결핵진단서 테이블에 학번, 날짜, 경로를 삽입하고
    // 배정 테이블에 결핵진단서id를 갱신, 진단서 제출여부를 갱신
    public void insertCertificate(String studentId, LocalDateTime date, String path)
    {
        dao.insert(studentId, date, path);
        dao.updateAssignmentCertificateId(studentId, dao.findCertificateID(studentId));
    }

    // 학번을 인자로 받아서 해당 학생이 제출한 결핵진단서 파일이 저장된 경로를 반환
    public Certificate_DTO findFilePath(String studentId)
    {
        Certificate_DTO dto = dao.findFilePath(studentId);

        return dto;
    }

    // 결핵진단서 제출자를 동별로 조회해서 이중리스트로 만들어 반환
    // View에서 출력 문자열을 만들때 동별로 구분하기 위해서
    public List<List<Certificate_DTO>> findOrderByBuilding()
    {
        List<List<Certificate_DTO>> dtos = new ArrayList<>();

        for(int i = 1; i <= 6; i++)
            dtos.add(dao.findSubmittedStudent(i));

        return dtos;
    }
}