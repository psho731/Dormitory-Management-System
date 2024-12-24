package persistence.dao;

import persistence.PooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentHistory_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public int processPayment(String studentId, String bank, String accountNum)
    { // 특정 학생의 납부처리 진행
        // 내역 추가 (building_id를 선발 테이블에서 가져오고,
        // 결제 금액을 vw_assignment_cost에서 납부해야 할 총금액을 가져와 삽입)
        String sql1 =
            "INSERT INTO payment_history(student_id, building_id, history_type, cost, bank, account_number, date) " +
            "VALUES (?, " +
            "       (SELECT building_id FROM assignment WHERE student_id = ?), " +
            "       '납부', " +
            "       (SELECT total_cost FROM vw_assignment_cost WHERE student_id = ?), ?, ?, NOW())";

        // 추가한 내역id를 선발 테이블의 payment_history_id에 등록
        // 선발 테이블의 납부 여부를 1로 변경 (납부 처리)
        String sql2 =
            "UPDATE assignment " +
            "SET payment_history_id = (SELECT MAX(payment_history_id) FROM payment_history WHERE student_id = ?), " +
            "    payment_status = 1 " +
            "WHERE student_id = ?";

        int affectedRows = 0;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            PreparedStatement pstmt2 = conn.prepareStatement(sql2)
        )
        {
            conn.setAutoCommit(false);

            // 첫 번째 SQL 쿼리 실행
            pstmt1.setString(1, studentId);
            pstmt1.setString(2, studentId);
            pstmt1.setString(3, studentId);
            pstmt1.setString(4, bank);
            pstmt1.setString(5, accountNum);

            affectedRows += pstmt1.executeUpdate();

            // 두 번째 SQL 쿼리 실행
            pstmt2.setString(1, studentId);
            pstmt2.setString(2, studentId);

            affectedRows += pstmt2.executeUpdate();

            conn.commit(); // 트랜잭션 커밋
        }
        catch (SQLException e)
        {
            try (Connection conn = ds.getConnection())
            {
                conn.rollback(); // 롤백 처리
                System.err.println("Transaction rolled back due to error.");
            }
            catch (SQLException rollbackEx)
            {
                rollbackEx.printStackTrace(); // 롤백 실패 시 예외 처리
            }
            e.printStackTrace();
        }
        finally
        {
            try (Connection conn = ds.getConnection())
            {
                conn.setAutoCommit(true); // autoCommit 복구
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        return affectedRows;
    }

    public int processRefund(String studentID)
    { // 특정 학생의 환불처리 진행
        String updateRefundStatusSql =  // 학생의 환불 여부를 1로 변경 (환불 처리)
            "UPDATE resignation_apply SET refund_status = 1 WHERE student_id = ?";
        String insertRefundHistorySql =  // 환불 내역 삽입, 퇴사 신청 테이블에서 학생의 퇴사 신청 정보를 가져와 사용
            "INSERT INTO payment_history (student_id, building_id, history_type, cost, bank, account_number, date) " +
            "SELECT r.student_id, r.building_id, '환불', r.refund_cost, r.bank, r.account_number, NOW() " +
            "FROM resignation_apply r " +
            "WHERE r.student_id = ?";
        String updatePaymentHistoryIdSql = // 퇴사 신청 테이블의 payment_history_id를 위에서 추가한 id로 변경
            "UPDATE resignation_apply SET payment_history_id = ? WHERE student_id = ?";

        int affectedRows = 0;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmtUpdateRefundStatus = conn.prepareStatement(updateRefundStatusSql);
            PreparedStatement pstmtInsertRefundHistory = conn.prepareStatement(insertRefundHistorySql, PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement pstmtUpdatePaymentHistoryId = conn.prepareStatement(updatePaymentHistoryIdSql)
        )
        {
            conn.setAutoCommit(false); // 트랜잭션 시작

            // 1. 환불 상태 업데이트
            pstmtUpdateRefundStatus.setString(1, studentID);
            affectedRows += pstmtUpdateRefundStatus.executeUpdate();

            // 2. 환불 내역 삽입
            pstmtInsertRefundHistory.setString(1, studentID);
            pstmtInsertRefundHistory.executeUpdate();

            // 3. 생성된 payment_history_id 가져오기
            try (ResultSet generatedKeys = pstmtInsertRefundHistory.getGeneratedKeys())
            {
                if (generatedKeys.next())
                {
                    int paymentHistoryId = generatedKeys.getInt(1);

                    // 4. resignation_apply의 payment_history_id 갱신
                    pstmtUpdatePaymentHistoryId.setInt(1, paymentHistoryId);
                    pstmtUpdatePaymentHistoryId.setString(2, studentID);
                    affectedRows += pstmtUpdatePaymentHistoryId.executeUpdate();
                }
            }

            conn.commit(); // 트랜잭션 커밋
        }
        catch (SQLException e)
        {
            try (Connection conn = ds.getConnection())
            {
                conn.rollback(); // 오류 발생 시 롤백
                System.err.println("Transaction rolled back due to error.");
            }
            catch (SQLException rollbackEx)
            {
                rollbackEx.printStackTrace(); // 롤백 실패 시 예외 처리
            }
            e.printStackTrace();
        }

        return affectedRows;
    }
}