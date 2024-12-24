package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.VwManualAssignment_DTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VwManualAssignment_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    // 관리자의 수동배정 기능에서 화면에 띄워줄 정보 ( 현재 전체 기숙사와 각 기숙사에 배정된 학생의 학번을 같이 띄워줌)
    // (기숙사id, 건물이름, 방, 침대번호, 학번, 이름)
    public List<VwManualAssignment_DTO> findAssignmentStd()
    {
        String sql = "SELECT * FROM vw_manual_assignment";
        List<VwManualAssignment_DTO> list = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        )
        {
            while (rs.next())
            {
                list.add(mapToVwManualAssignmentDTO(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    private VwManualAssignment_DTO mapToVwManualAssignmentDTO(ResultSet rs) throws SQLException
    {
        VwManualAssignment_DTO dto = new VwManualAssignment_DTO();

        dto.setDormitory_id(rs.getInt("dormitory_id")); // 생활관 ID 설정
        dto.setBuilding_id(rs.getInt("building_id")); // 건물 ID 설정
        dto.setBuilding_name(rs.getString("building_name")); // 건물 이름 설정
        dto.setRoom(rs.getString("room")); // 방 번호 설정
        dto.setBed(rs.getString("bed").charAt(0)); // 침대 정보 설정
        dto.setStudent_id(rs.getString("student_id")); // 학번 설정
        dto.setStudent_name(rs.getString("student_name")); // 학생 이름 설정

        return dto;
    }
}