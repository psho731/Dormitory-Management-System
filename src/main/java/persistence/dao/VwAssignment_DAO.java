package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.VwAssignment_DTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VwAssignment_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public List<VwAssignment_DTO> findAll()
    { // 배정된 학생을 조회 Vw_assignment_DTO리스트(학번, 학생이름, 동id, 건물이름, 방, 침대번호)
        String sql = "SELECT * FROM vw_assignment";
        List<VwAssignment_DTO> list = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        )
        {
            while (rs.next())
            {
                list.add(mapToVwAssignmentDTO(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public VwAssignment_DTO findByStudentID(String studentID)
    { // 학번을 인자로 받아, 학생 1명에 대해 배정된 방과 침대를 조회 Vw_assignment_DTO(학번, 이름, 동id, 건물이름, 방, 침대번호)
        String sql = "SELECT * FROM vw_assignment WHERE student_id = ?";
        VwAssignment_DTO dto = null;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, studentID);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                    dto = mapToVwAssignmentDTO(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return dto;
    }

    // 기숙사 동별로 배정된 학생을 조회 VwAssignment_DTO리스트 반환(학번, 이름, 동id, 건물이름, 방, 침대번호)
    public List<VwAssignment_DTO> findByBuildingID(int buildingID)
    {
        String sql = "SELECT * FROM vw_assignment WHERE building_id = ?";
        List<VwAssignment_DTO> list = new ArrayList<>();

        try (
                Connection conn = ds.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setInt(1, buildingID);

            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    list.add(mapToVwAssignmentDTO(rs));
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }
    
    // 납부 상태에 따른 학생 목록 조회
    public List<VwAssignment_DTO> findStudentsByPaymentStatus(int buildingID, int paymentStatus)
    {
        String sql =
                "SELECT * " +
                "FROM vw_assignment " +
                "WHERE payment_status = ? AND building_id = ?";

        List<VwAssignment_DTO> list = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setInt(1, paymentStatus);
            pstmt.setInt(2, buildingID);

            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next()) {
                    VwAssignment_DTO dto = mapToVwAssignmentDTO(rs); // 데이터 매핑 메서드 호출
                    list.add(dto);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    private VwAssignment_DTO mapToVwAssignmentDTO(ResultSet rs) throws SQLException
    {
        VwAssignment_DTO dto = new VwAssignment_DTO();

        dto.setStudent_id(rs.getString("student_id")); // 학번 설정
        dto.setStudent_name(rs.getString("student_name")); // 학생 이름 설정
        dto.setBuilding_id(rs.getInt("building_id")); // 건물 ID 설정
        dto.setBuilding_name(rs.getString("building_name")); // 건물 이름 설정
        dto.setRoom(rs.getString("room")); // 방 번호 설정
        dto.setBed(rs.getString("bed").charAt(0)); // 침대 정보 설정
        dto.setMeal_cost(rs.getInt("meal_cost")); // 급식 비용 설정
        dto.setDormitory_cost(rs.getInt("dormitory_cost")); // 기숙사 비용 설정
        dto.setTotal_cost(rs.getInt("total_cost")); // 총 비용 설정
        dto.setPayment_status(rs.getByte("payment_status")); // 납부 상태 설정

        return dto;
    }
}