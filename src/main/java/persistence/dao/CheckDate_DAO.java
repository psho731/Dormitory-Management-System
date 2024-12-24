package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.CheckDate_DTO;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;

public class CheckDate_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    // 선발일정id를 인자로 받아서 기간 시작날짜(시간)과 종료날짜(시간)을 CheckDateDTO에 저장하고 반환
    public CheckDate_DTO findDate(int id)
    {
        String sql = "SELECT start_date, end_date FROM schedule WHERE schedule_id = ?";
        CheckDate_DTO checkDateDTO = new CheckDate_DTO();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            // 매개변수 바인딩
            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    LocalDateTime startDate = rs.getObject("start_date", LocalDateTime.class);
                    LocalDateTime endDate = rs.getObject("end_date", LocalDateTime.class);

                    checkDateDTO.setStart_date(startDate);
                    checkDateDTO.setEnd_date(endDate);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return checkDateDTO;
    }

    // DB의 현재 날짜(시간)를 조회 후 반환
    public LocalDateTime findNow()
    {
        String sql = "SELECT CURRENT_TIMESTAMP";
        LocalDateTime nowDate = null;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        )
        {
            if (rs.next())
                nowDate = rs.getObject(1, LocalDateTime.class);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return nowDate;
    }
}
