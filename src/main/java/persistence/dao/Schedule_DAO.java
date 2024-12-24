package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.Schedule_DTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Schedule_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public List<Schedule_DTO> findAll()
    { // 모든 스케줄 정보 조회 (일정 조회할 때 사용)
        String sql = "SELECT * FROM schedule";
        List<Schedule_DTO> scheduleViewDTOS = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        )
        {
            while (rs.next())
            {
                Schedule_DTO scheduleViewDTO = new Schedule_DTO();

                scheduleViewDTO.setSchedule_id(rs.getString("schedule_id"));
                scheduleViewDTO.setName(rs.getString("name"));
                scheduleViewDTO.setStart_date(rs.getString("start_date"));
                scheduleViewDTO.setEnd_date(rs.getString("end_date"));

                scheduleViewDTOS.add(scheduleViewDTO);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return scheduleViewDTOS;
    }

    public void insert(Schedule_DTO dto)
    { // 일정 추가 (관리자가 일정을 등록할 때 사용)
        String sql = "INSERT INTO schedule (name, start_date, end_date) VALUES (?, ?, ?)";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getStart_date());
            pstmt.setString(3, dto.getEnd_date());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows inserted: " + rowsAffected);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void delete(String scheduleId)
    { // 일정 삭제 (관리자가 일정을 삭제할 때 사용)
        String sql = "DELETE FROM schedule WHERE schedule_id = ?"; // 일정 삭제 SQL
        String sql2 = "ALTER TABLE schedule AUTO_INCREMENT = 1"; // 인조키 초기화

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            Statement stmt = conn.createStatement();
        )
        {
            pstmt.setString(1, scheduleId);
            System.out.println(pstmt.executeUpdate());

            stmt.executeUpdate(sql2);
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }
}