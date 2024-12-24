package persistence.dao;

import persistence.PooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Selection_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public void executeAutoSelection(int totalDormitoryNum)
    { // 자동 선발 실행 메서드
        int[] recruitNumArr = findRecruitNumber(totalDormitoryNum);

        // 대학원생 선발
        autoSelection(totalDormitoryNum, recruitNumArr, "대학원생");

        // 학부생 선발
        autoSelection(totalDormitoryNum, recruitNumArr, "학부생");
    }

    private void autoSelection(int totalDormitoryNum, int[] recruitNumArr, String studentType)
    { // 자동 선발 메서드 (공통 로직)
        String selectQuery =
            "SELECT apply.student_id, apply.building_id, meal_cost_id " +
            "FROM apply " +
            "JOIN student ON apply.student_id = student.student_id " +
            "WHERE student.type = ? AND choice_order = ? AND building_id = ? " +
            "ORDER BY total_score DESC";

        String insertQuery =
            "INSERT IGNORE INTO assignment(student_id, building_id, meal_cost_id) " +
            "VALUES (?, ?, ?)";

        String updateQuery =
            "UPDATE apply " +
            "SET pass = 1 " +
            "WHERE choice_order = ? AND student_id = ? " +
            "AND student_id = (SELECT student_id FROM assignment WHERE student_id = ?)";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement selectStmt = conn.prepareStatement(selectQuery);
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            PreparedStatement updateStmt = conn.prepareStatement(updateQuery)
        )
        {
            for (int choiceOrder = 1; choiceOrder <= 2; choiceOrder++)
            {
                for (int i = 1; i <= totalDormitoryNum; i++)
                {
                    selectStmt.setString(1, studentType);
                    selectStmt.setInt(2, choiceOrder);
                    selectStmt.setInt(3, i);

                    try (ResultSet rs = selectStmt.executeQuery())
                    {
                        while (recruitNumArr[i - 1] > 0 && rs.next())
                        {
                            String studentId = rs.getString("student_id");
                            int buildingId = rs.getInt("building_id");
                            int mealCostId = rs.getInt("meal_cost_id");

                            insertStmt.setString(1, studentId);
                            insertStmt.setInt(2, buildingId);
                            insertStmt.setInt(3, mealCostId);
                            int rowsInserted = insertStmt.executeUpdate();

                            if (rowsInserted > 0)
                            {
                                updateStmt.setInt(1, choiceOrder);
                                updateStmt.setString(2, studentId);
                                updateStmt.setString(3, studentId);
                                updateStmt.executeUpdate();
                                recruitNumArr[i - 1]--;
                            }
                        }
                    }
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int[] findRecruitNumber(int totalDormitoryNum)
    { // 기숙사 선발 가능 인원 조회 메서드
        String sql = "SELECT recruit_number FROM building";
        int[] recruitNumArr = new int[totalDormitoryNum];

        try (
            Connection conn = ds.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        )
        {
            int index = 0;

            while (rs.next() && index < totalDormitoryNum)
            {
                recruitNumArr[index++] = rs.getInt("recruit_number");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return recruitNumArr;
    }
}