package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.VwApply_DTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VwApply_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public List<VwApply_DTO> findByBuildingID(int buildingID)
    { // 생활관id로 입사신청 뷰 조회
        String sql = "SELECT * FROM vw_apply WHERE building_id = ? ORDER BY choice_order ASC";

        List<VwApply_DTO> list = new ArrayList<>();

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setInt(1, buildingID);

            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    VwApply_DTO dto = new VwApply_DTO();
                    dto.student_id = rs.getString("student_id");
                    dto.student_name = rs.getString("student_name");
                    dto.choice_order = rs.getInt("choice_order");
                    dto.building_id = rs.getInt("building_id");
                    dto.building_name = rs.getString("building_name");
                    dto.meal_option = rs.getString("meal_option");
                    dto.student_type = rs.getString("student_type");
                    dto.total_score = rs.getDouble("total_score");

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
}