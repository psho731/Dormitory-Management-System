package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.VwAssignmentCost_DTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VwAssignmentCost_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public VwAssignmentCost_DTO getTotalCost(String studentId)
    { // 특정 학번으로 vw_assignment_cost(학생의 비용 정보를 가짐) 테이블에서 조회
        String sql = "SELECT * FROM vw_assignment_cost WHERE student_id = ?";
        VwAssignmentCost_DTO vwAssignmentCostDTO = null;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, studentId);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                    vwAssignmentCostDTO = mapToVwAssignmentCostDTO(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return vwAssignmentCostDTO;
    }

    // 데이터 매핑을 위한 메서드
    private VwAssignmentCost_DTO mapToVwAssignmentCostDTO(ResultSet rs) throws SQLException
    {
        VwAssignmentCost_DTO dto = new VwAssignmentCost_DTO();
        dto.setStudent_id(rs.getString("student_id"));
        dto.setDormitoryCost(rs.getInt("dormitory_cost"));
        dto.setMealCost(rs.getInt("meal_cost"));
        dto.setTotalCost(rs.getInt("total_cost"));
        return dto;
    }
}