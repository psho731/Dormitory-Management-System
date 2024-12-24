package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.DormitoryCost_DTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DormitoryCost_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public List<DormitoryCost_DTO> findAll()
    { // 모든 생활관비 정보 조회
        String sql = "SELECT * FROM dormitory_cost";
        List<DormitoryCost_DTO> list = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        )
        {
            while (rs.next())
            {
                DormitoryCost_DTO dormitoryCostDto = new DormitoryCost_DTO();
                dormitoryCostDto.setDormitory_cost_id(rs.getInt("dormitory_cost_id"));
                dormitoryCostDto.setDormitory_type(rs.getString("dormitory_type"));
                dormitoryCostDto.setCost(rs.getInt("cost"));
                list.add(dormitoryCostDto);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public int updateCost(int dormitoryCostID, int cost)
    { // dormitory_cost_id의 비용을 cost로 변경
      // 성공 시 1 반환
        String sql = "UPDATE dormitory_cost SET cost = ? WHERE dormitory_cost_id = ?";

        int result = 0;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setInt(1, cost);
            pstmt.setInt(2, dormitoryCostID);

            result = pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return result;
    }
}