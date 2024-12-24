package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.Assignment_DTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Assignment_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    // 배정테이블에서 배정dto 리스트 만들어서 반환 배정dto(배정id,학번,동id, 식사비id,기숙사id,결제내역id,진단서id,비용납부여부,진단서제출여부)
    public List<Assignment_DTO> findAll()
    {
        String sql = "SELECT * FROM assignment";

        List<Assignment_DTO> list = new ArrayList<>();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()
        )
        {
            while (rs.next())
            {
                Assignment_DTO assignmentDto = new Assignment_DTO();
                assignmentDto.setAssignment_id(rs.getInt("assignment_id"));
                assignmentDto.setStudent_id(rs.getString("student_id"));
                assignmentDto.setBuilding_id(rs.getInt("building_id"));
                assignmentDto.setMeal_cost_id(rs.getInt("meal_cost_id"));
                assignmentDto.setDormitory_id(rs.getInt("dormitory_id"));
                assignmentDto.setPayment_history_id(rs.getInt("payment_history_id"));
                assignmentDto.setCertificate_id(rs.getInt("certificate_id"));
                assignmentDto.setPayment_status(rs.getByte("payment_status"));
                assignmentDto.setCertificate_status(rs.getByte("certificate_status"));

                list.add(assignmentDto);
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    // 동별로 아직 배정되지 않은 학생 조회, AssignmentDTO 리스트에 담아서 반환
    public List<Assignment_DTO> findBuilding(int buildingID)
    {
        String sql = "SELECT student_id, building_id FROM assignment WHERE dormitory_id IS NULL AND building_id = ?";
        List<Assignment_DTO> list = new ArrayList<>();

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
                    Assignment_DTO assignmentDto = new Assignment_DTO();
                    assignmentDto.setStudent_id(rs.getString("student_id"));
                    assignmentDto.setBuilding_id(rs.getInt("building_id"));

                    list.add(assignmentDto);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return list;
    }

    // 학생배정( 학번과 기숙사id를 인자로 받아, 배정테이블 학번과 일치하는 행의 기숙사id 업데이트)
    public void manualAssignment(String studentID, int dormitoryID)
    {
        String sql = "UPDATE assignment SET dormitory_id = ? WHERE student_id = ?";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setInt(1, dormitoryID); // 첫 번째 매개변수에 dormitoryID 설정
            pstmt.setString(2, studentID); // 두 번째 매개변수에 studentID 설정

            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // 수동배정, 기숙사에 배정된 두 학생의 학번을 받아서 두 학생의 방을 바꾸는 방식, db프로시저 호출
    public boolean manualAssignmentSwap(String studentID1, String studentID2)
    {
        String sql = "{CALL manual_assignment_procedure(?, ?, ?)}";
        int result = 0;

        try (
            Connection conn = ds.getConnection();
            CallableStatement stmt = conn.prepareCall(sql)
        )
        {
            // IN 매개변수 설정
            stmt.setString(1, studentID1);
            stmt.setString(2, studentID2);

            // OUT 매개변수 등록
            stmt.registerOutParameter(3, java.sql.Types.INTEGER);

            // 프로시저 실행
            stmt.execute();

            // OUT 매개변수 값 가져오기
            result = stmt.getInt(3);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return result == 1;
    }

    // 배정되지 않은 학생(AssignmentDTO)리스트를 동별로 받아서 비어있는 기숙사에 배정하기
    public void findEmpty(List<Assignment_DTO> list, int buildingId)
    {
        String sql = "SELECT dormitory_id " +
                     "FROM vw_manual_assignment " +
                     "WHERE building_id = ? AND student_id IS NULL";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setInt(1, buildingId);

            try (ResultSet rs = pstmt.executeQuery())
            {
                int idx = 0;

                // 결과 처리
                while (rs.next() && idx < list.size())
                { // 리스트의 남은 크기만큼 수행
                    int dormitoryId = rs.getInt("dormitory_id");

                    // 학생에 기숙사 ID를 배정
                    manualAssignment(list.get(idx).getStudent_id(), dormitoryId);
                    idx++;
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}