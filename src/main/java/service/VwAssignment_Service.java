package service;

import persistence.dao.VwAssignment_DAO;
import persistence.dto.VwAssignment_DTO;

import java.util.ArrayList;
import java.util.List;

public class VwAssignment_Service
{
    private final VwAssignment_DAO dao;

    public VwAssignment_Service(VwAssignment_DAO dao)
    {
        this.dao = dao;
    }

    // 배정된 학생을 조회 (학번, 학생이름, 동id, 건물이름, 방, 침대번호), 리스트 반환
    public List<VwAssignment_DTO> findAll()
    {
        return dao.findAll();
    }

    // 학번을 인자로 받아, 해당 학생이 배정된 방과 침대를 조회 (학번, 이름, 동id, 건물이름, 방, 침대번호) 리스트 반환
    public VwAssignment_DTO findStudentID(String student_id)
    {
        return dao.findByStudentID(student_id);
    }

    // 기숙사 동별로 배정된 학생을 조회 (학번, 이름, 동id, 건물이름, 방, 침대번호) 동별로 구분해서 출력하기 위해 이중리스트를 반환
    public List<List<VwAssignment_DTO>> findOrderByBuilding()
    {
        List<List<VwAssignment_DTO>> dtos = new ArrayList<>();

        for(int i = 1; i <= 6; i++)
            dtos.add(dao.findByBuildingID(i));

        return dtos;
    }

    //납부자 학생을 동별로 출력하기 위한 중첩 리스트를 반환하는 메소드
    public List<List<VwAssignment_DTO>> findPaymentStudent()
    {
        List<List<VwAssignment_DTO>> dtos = new ArrayList<>();

        for(int i = 1; i <= 6; i++)
            dtos.add(dao.findStudentsByPaymentStatus(i, 1)); //기숙사 id 순서대로 리스트에 입력

        return dtos;
    }

    //미납부자 학생을 동별로 출력하기 위한 중첩 리스트를 반환하는 메소드
    public List<List<VwAssignment_DTO>> findNonPaymentStudent()
    {
        List<List<VwAssignment_DTO>> dtos = new ArrayList<>();

        for(int i = 1; i <= 6; i++)
            dtos.add(dao.findStudentsByPaymentStatus(i, 0)); //기숙사 id 순서대로 리스트에 입력

        return dtos;
    }
}