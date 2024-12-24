package persistence.dao;

import persistence.PooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Building_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public int findRecruitNumber(int buildingID)
    { // 현재 기숙사의 배정될 수 있는 인원 수를 반환하는 메소드
        // building 테이블의 정보를 가져오는 sql
        String sql = "SELECT recruit_number FROM building WHERE building_id = ?";

        int rn = 0;

        try (
                Connection conn = ds.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
        )
        {
            pstmt.setInt(1, buildingID);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                    rn = rs.getInt("recruit_number");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rn;
    }

    public void updateRecruitNum(int buildingID, int recruitNumber)
    { // 현재 기숙사의 배정될 수 있는 인원 수를 갱신하는 메소드
        // building 테이블에 입력받은 buildingID에 해당하는 열의 recruit_number를 입력받은 recruitNumber로 갱신하는 메소드
        String sql = "UPDATE building SET recruit_number = ? WHERE building_id = ?";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setInt(1, recruitNumber);
            pstmt.setInt(2, buildingID);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
