package persistence.dao;

import persistence.PooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class ResignationApply_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    // 학번, 날짜(시간), 은행, 계좌번호, 선발일정id를 입력받아서, 퇴사신청테이블에 삽입하고, 삽입 성공여부 (1: 성공, 0: 실패) 반환
    public int insert(String student_id, LocalDateTime date, String bank, String account_number, int schedule_id)
    {
        //결제내역 테이블에서 특정 학번의 가장 최근 일자의 데이터(납부 내역)를 조회하고
        // 조회한 데이터의 정보를 사용해 필요한 값을 채운다.
        // schedule_id는 입사일과 퇴사일을 찾을 때 사용
        // cost는 입력일과 퇴사일의 차이를 입사일와 퇴사일의 차이로 나눠 비율을 구한 후 곱한다.
        // 비율은 1을 넘을 수 없게 LEAST를 사용
        // 입력일이 현재 날짜 초과, 퇴사일 이하여야 하게 제한
        String sql =
                "INSERT INTO resignation_apply (student_id, payment_history_id, building_id, date, bank, account_number, refund_status, refund_cost) " +
                "SELECT ?, payment_history_id, building_id, ?, ?, ?, 0, cost * " +
                "LEAST(1, DATEDIFF((SELECT end_date FROM schedule WHERE schedule_id = ?), ?) / " +
                "DATEDIFF((SELECT end_date FROM schedule WHERE schedule_id = ?), (SELECT start_date FROM schedule WHERE schedule_id = ?))) " +
                "FROM payment_history " +
                "WHERE student_id = ? " +
                "AND ? <= (SELECT end_date FROM schedule WHERE schedule_id = ?) " +
                "AND ? > NOW() " +
                "ORDER BY date DESC LIMIT 1";

        // 성공여부, 1이면 테이블 삽입성공, 0이면 실패
        int rows_affected = 0;

        try (Connection conn = ds.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql))
        {
            pstmt.setString(1, student_id);
            pstmt.setObject(2, date);
            pstmt.setString(3, bank);
            pstmt.setString(4, account_number);
            pstmt.setInt(5, schedule_id);
            pstmt.setObject(6, date);
            pstmt.setInt(7, schedule_id);
            pstmt.setInt(8, schedule_id);
            pstmt.setString(9, student_id);
            pstmt.setObject(10, date);
            pstmt.setInt(11, schedule_id);
            pstmt.setObject(12, date);

            rows_affected = pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return rows_affected;
    }
}