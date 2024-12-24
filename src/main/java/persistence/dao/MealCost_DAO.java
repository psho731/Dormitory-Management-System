package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.MealCost_DTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MealCost_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public List<MealCost_DTO> findAll()
    { // 모든 급식비 정보 조회
        String sql = "SELECT * FROM meal_cost";
        List<MealCost_DTO> list = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        )
        {
            while (rs.next())
            {
                MealCost_DTO mealCostDto = new MealCost_DTO();
                mealCostDto.setMeal_cost_id(rs.getInt("meal_cost_id"));
                mealCostDto.setMeal_option(rs.getString("meal_option"));
                mealCostDto.setCost(rs.getInt("cost"));
                list.add(mealCostDto);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public int updateCost(int mealCostID, int cost)
    { // meal_cost_id의 비용을 cost로 변경
      // 성공 시 1 반환
        String sql = "UPDATE meal_cost SET cost = ? WHERE meal_cost_id = ?";

        int result = 0;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setInt(1, cost);
            pstmt.setInt(2, mealCostID);

            result = pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return result;
    }
}