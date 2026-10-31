import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class HospitalDAO {
    
    public static void savePatient(Patient p) {
        String sql = "INSERT INTO patients (patient_id, name, age, gender, contact_number, symptoms, registration_time, assessment_notes, urgency_level, is_emergency, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, p.getPatientId());
            pstmt.setString(2, p.getName());
            pstmt.setInt(3, p.getAge());
            pstmt.setString(4, p.getGender());
            pstmt.setString(5, p.getContactNumber());
            pstmt.setString(6, p.getSymptoms());
            pstmt.setTimestamp(7, Timestamp.valueOf(p.getRegistrationTime()));
            pstmt.setString(8, p.getAssessmentNotes());
            pstmt.setInt(9, p.getUrgencyLevel());
            pstmt.setBoolean(10, p.isEmergency());
            pstmt.setString(11, p.getStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updatePatientStatus(String patientId, String status) {
        String sql = "UPDATE patients SET status = ? WHERE patient_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, patientId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateAssessment(String patientId, String notes, int urgency, boolean isEmergency) {
        String sql = "UPDATE patients SET assessment_notes = ?, urgency_level = ?, is_emergency = ? WHERE patient_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, notes);
            pstmt.setInt(2, urgency);
            pstmt.setBoolean(3, isEmergency);
            pstmt.setString(4, patientId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Patient> getAllPatients() {
        List<Patient> list = new ArrayList<>();
        String sql = "SELECT * FROM patients ORDER BY registration_time DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static PriorityQueue<Patient> getActivePriorityQueue() {
        PriorityQueue<Patient> pq = new PriorityQueue<>(new PatientComparator());
        String sql = "SELECT * FROM patients WHERE status != 'Consultation Completed'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                pq.offer(mapResultSetToPatient(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pq;
    }

    private static Patient mapResultSetToPatient(ResultSet rs) throws SQLException {
        return new Patient(
            rs.getString("patient_id"),
            rs.getString("name"),
            rs.getInt("age"),
            rs.getString("gender"),
            rs.getString("contact_number"),
            rs.getString("symptoms"),
            rs.getTimestamp("registration_time").toLocalDateTime(),
            rs.getString("assessment_notes"),
            rs.getInt("urgency_level"),
            rs.getBoolean("is_emergency"),
            rs.getString("status")
        );
    }
}