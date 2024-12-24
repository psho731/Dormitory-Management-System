package persistence.dao;

import persistence.PooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public String findAccount(String id, String pw)
    {
        // 사용자 계정 테이블에서 id로 검색
        String sql = "SELECT password, type FROM member_account WHERE id = ?";
        String result = "n"; // 기본값 "n" (계정 없음 또는 비밀번호 불일치)

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    String dbPassword = rs.getString("password");
                    String type = rs.getString("type");

                    // 비밀번호 검증
                    if (pw.equals(dbPassword))
                        result = type; // "s"(학생) 또는 "a"(관리자) 반환
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return result;
    }
}