package persistence.dao;

import persistence.PooledDataSource;
import persistence.dto.Certificate_DTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Certificate_DAO
{
    private final DataSource ds = PooledDataSource.getDataSource();

    // 학번, 날짜, TCP 서버의 파일 경로를 인자로 받아 결핵진단서 테이블에 삽입
    public void insert(String studentId, LocalDateTime date, String path)
    {
        // 같은 학생이 두번 제출하는 경우 진단서 테이블에 최신날짜와 최신파일로 수정
        String sql = "INSERT INTO certificate (student_id, submission_date, file_path) " +
                     "VALUES (?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "file_path = ?, submission_date = ?";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, studentId);
            pstmt.setObject(2, date); // LocalDateTime 처리
            pstmt.setString(3, path);
            pstmt.setString(4, path);
            pstmt.setObject(5, date);
            pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    // 학번을 인자로 받아서 결핵진단서 테이블에 해당 학번의 정보가 저장된 기본키(certificate_id)를 반환
    public int findCertificateID(String studentId)
    {
        String sql = "SELECT certificate_id " +
                     "FROM certificate " +
                     "WHERE student_id = ? AND submission_date = (" +
                     "SELECT MAX(submission_date) FROM certificate WHERE student_id = ?)";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, studentId);
            pstmt.setString(2, studentId);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                    return rs.getInt("certificate_id");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    // 학생이 결핵진단서를 제출하면, 먼저 결핵진단서 테이블에 정보를 추가하고,
    // 그 기본키를 받아와서(findMedicalCertificateId(studentId)) 배정테이블에 결핵진단서 id를 갱신
    // 진단서제출여부를 1로 갱신
    public int updateAssignmentCertificateId(String studentId, int certificateId)
    {
        String sql = "UPDATE assignment " +
                     "SET certificate_id = ?, certificate_status = 1 " +
                     "WHERE student_id = ?";

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setInt(1, certificateId);
            pstmt.setString(2, studentId);

            return pstmt.executeUpdate();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    // 동id를 인자로 받아서 동별로 진단서 제출한 학생의 정보(CertificateDTO)리스트를 반환한다.
    public List<Certificate_DTO> findSubmittedStudent(int buildingID)
    {
        String sql = "SELECT s.student_id, s.name, s.grade, a.certificate_status, a.building_id " +
                     "FROM student s " +
                     "JOIN assignment a ON s.student_id = a.student_id " +
                     "JOIN certificate c ON a.certificate_id = c.certificate_id " +
                     "WHERE a.building_id = ?";

        List<Certificate_DTO> certificateList = new ArrayList<>();

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
                    Certificate_DTO dto = new Certificate_DTO();
                    dto.setStudentId(rs.getString("student_id"));
                    dto.setName(rs.getString("name"));
                    dto.setGrade(rs.getString("grade"));
                    dto.setCertificate_status(rs.getByte("certificate_status"));
                    dto.setDormitoryNum(rs.getInt("building_id"));

                    certificateList.add(dto);
                }
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return certificateList;
    }

    // 학번을 입력받아서 결핵진단서 테이블에서 진단서 파일의 경로(TCP서버의 파일경로)를 가져온다.
    // Certificate_DTO반환. 파일은 TCP서버에 저장되어있음.
    public Certificate_DTO findFilePath(String studentId)
    {
        String sql = "SELECT c.file_path " +
                     "FROM assignment a " +
                     "JOIN certificate c ON a.certificate_id = c.certificate_id " +
                     "WHERE a.student_id = ? AND a.certificate_id IS NOT NULL";

        Certificate_DTO filePathDTO = new Certificate_DTO();

        try (
            Connection conn = ds.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)
        )
        {
            pstmt.setString(1, studentId);

            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                    filePathDTO.setFile_path(rs.getString("file_path"));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return filePathDTO;
    }
}