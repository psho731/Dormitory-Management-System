package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.VwResignationApply_DTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VwResignationApply_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    public List<VwResignationApply_DTO> findByBuildingID(int buildingID)
    { // 생활관id로 Vw_resignation_apply 조회 (퇴사신청자 관별 조회에 사용)
        String sql = "SELECT * FROM vw_resignation_apply WHERE building_id = ? ORDER BY date ASC";
        List<VwResignationApply_DTO> list = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setInt(1, buildingID);

            try (ResultSet rs = pstmt.executeQuery())
            {
                while (rs.next())
                {
                    list.add(mapToVwResignationApplyDTO(rs));
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    public VwResignationApply_DTO findRefund(String studentID)
    { // 퇴사신청 테이블에서 특정 학생을 조회
        String sql = "SELECT * FROM vw_resignation_apply WHERE student_id = ?";
        VwResignationApply_DTO dto = null;

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, studentID);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                    dto = mapToVwResignationApplyDTO(rs);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return dto;
    }

    private VwResignationApply_DTO mapToVwResignationApplyDTO(ResultSet rs) throws SQLException
    {
        VwResignationApply_DTO dto = new VwResignationApply_DTO();
        dto.setBuilding_id(rs.getInt("building_id"));
        dto.setBuilding_name(rs.getString("building_name"));
        dto.setStudent_id(rs.getString("student_id"));
        dto.setStudent_name(rs.getString("student_name"));
        dto.setDate(rs.getTimestamp("date").toLocalDateTime());
        dto.setRefund_status(rs.getByte("refund_status"));
        dto.setRefund_cost(rs.getInt("refund_cost"));
        dto.setBank(rs.getString("bank"));
        dto.setAccount_number(rs.getString("account_number"));
        return dto;
    }
}