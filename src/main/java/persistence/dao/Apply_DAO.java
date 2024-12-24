package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.Apply_DTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Apply_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public int countByStudentID(String studentID)
    { // student_id 개수 조회 (이미 지원했거나 선발됐는지 확인)
        String sql = "SELECT ( " +
                "  (SELECT COUNT(*) FROM apply WHERE student_id = ?) + " +
                "  (SELECT COUNT(*) FROM assignment WHERE student_id = ?) " +
                ") AS total_count";

        int count = 0;

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, studentID);
            pstmt.setString(2, studentID);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                    count = rs.getInt("total_count");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return count;
    }


    public int insert(String studentID, int choiceOrder, String buildingName, String mealOption)
    { // 지원 테이블에 삽입 (입사 신청)
        // building_name와 meal_option가 building과 meal_cost에 존재하고, 생활관과 성별이 맞다면 삽입
        String sql = "INSERT INTO apply (student_id, choice_order, building_id, meal_cost_id) " +
                     "SELECT ?, ?, b.building_id, m.meal_cost_id " +
                     "FROM building b " +
                     "JOIN meal_cost m ON m.meal_option = ? " +
                     "JOIN student s ON s.student_id = ? " +
                     "WHERE b.building_name = ? AND b.gender = s.gender";

        int result = 0;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, studentID);
            pstmt.setInt(2, choiceOrder);
            pstmt.setString(3, mealOption);
            pstmt.setString(4, studentID);
            pstmt.setString(5, buildingName);

            result = pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public int delete(String studentID)
    { // 지원테이블에서 특정 학번 삭제
        String sql = "DELETE FROM apply WHERE student_id = ?";
        int result = 0;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, studentID);
            result = pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return result;
    }

    public Apply_DTO findByStudentID(String studentID)
    { // 특정 학생의 정보 중 합격우선(pass = 1)으로 정렬 후 1행 조회
        String sql = "SELECT * FROM apply WHERE student_id = ? ORDER BY pass DESC LIMIT 1";
        Apply_DTO applyDto = null;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, studentID);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                    applyDto = mapToApplyDTO(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return applyDto;
    }

    private Apply_DTO mapToApplyDTO(ResultSet rs) throws SQLException
    {
        Apply_DTO applyDto = new Apply_DTO();
        applyDto.setApply_id(rs.getInt("apply_id"));
        applyDto.setStudent_id(rs.getString("student_id"));
        applyDto.setChoice_order(rs.getInt("choice_order"));
        applyDto.setBuilding_id(rs.getInt("building_id"));
        applyDto.setMeal_cost_id(rs.getInt("meal_cost_id"));
        applyDto.setPass(rs.getByte("pass"));
        return applyDto;
    }
}