package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.VwCost_DTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VwCost_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public List<VwCost_DTO> findAll()
    { // 성별, 생활관, 식사옵션으로 정렬 후 조회 (생활관비, 급식비 조회)
        String sql = "SELECT * FROM vw_cost ORDER BY gender ASC, building_name ASC, meal_option DESC";
        List<VwCost_DTO> list = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        )
        {
            while (rs.next())
            {
                list.add(mapToVwCostDTO(rs));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    private VwCost_DTO mapToVwCostDTO(ResultSet rs) throws SQLException
    {
        VwCost_DTO dto = new VwCost_DTO();
        dto.setGender(rs.getString("gender"));
        dto.setBuilding_name(rs.getString("building_name"));
        dto.setRecruit_number(rs.getInt("recruit_number"));
        dto.setRoom_type(rs.getString("room_type"));
        dto.setDormitory_cost(rs.getInt("dormitory_cost"));
        dto.setMeal_option(rs.getString("meal_option"));
        dto.setMeal_cost(rs.getInt("meal_cost"));
        return dto;
    }
}