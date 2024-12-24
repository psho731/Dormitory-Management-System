package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.StudentPoint_DTO;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class StudentPoint_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public void insert(String studentID, int score, LocalDateTime date, String reason)
    { // 상벌점 테이블에 내역 삽입
        String insertSQL = "INSERT INTO student_point (student_id, score, date, reason) VALUES (?, ?, ?, ?)";
        String updateSQL = "UPDATE student SET total_score = total_score + ? WHERE student_id = ?";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement insertStmt = conn.prepareStatement(insertSQL);
            PreparedStatement updateStmt = conn.prepareStatement(updateSQL)
        )
        {
            // INSERT 쿼리 파라미터 설정
            insertStmt.setString(1, studentID);
            insertStmt.setInt(2, score);
            insertStmt.setTimestamp(3, Timestamp.valueOf(date));
            insertStmt.setString(4, reason);
            insertStmt.executeUpdate();

            // UPDATE 쿼리 파라미터 설정
            double total = score * 0.1;
            updateStmt.setDouble(1, total);
            updateStmt.setString(2, studentID);
            updateStmt.executeUpdate();

        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public List<StudentPoint_DTO> findAll()
    { // 상벌점 테이블의 모든 정보 조회
        String sql = "SELECT * FROM student_point";
        List<StudentPoint_DTO> pointDTOs = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)
        )
        {
            while (rs.next())
            {
                pointDTOs.add(mapToStudentPointDTO(rs));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return pointDTOs;
    }

    private StudentPoint_DTO mapToStudentPointDTO(ResultSet rs) throws SQLException
    {
        StudentPoint_DTO pointDTO = new StudentPoint_DTO();
        pointDTO.setPoint_id(rs.getInt("point_id"));
        pointDTO.setStudent_id(rs.getString("student_id"));
        pointDTO.setScore(rs.getInt("score"));
        pointDTO.setReason(rs.getString("reason"));
        pointDTO.setDate(rs.getTimestamp("date").toLocalDateTime());
        return pointDTO;
    }

    public List<StudentPoint_DTO> findStudent(String studentID)
    { // 특정 학번으로 상벌점 테이블 조회
        String sql = "SELECT * FROM student_point WHERE student_id = ?";
        List<StudentPoint_DTO> pointDTOs = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, studentID);
            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    StudentPoint_DTO pointDTO = new StudentPoint_DTO();
                    pointDTO.setPoint_id(rs.getInt("point_id"));
                    pointDTO.setStudent_id(rs.getString("student_id"));
                    pointDTO.setScore(rs.getInt("score"));
                    pointDTO.setReason(rs.getString("reason"));
                    pointDTO.setDate(rs.getTimestamp("date").toLocalDateTime());
                    pointDTOs.add(pointDTO);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return pointDTOs;
    }
}