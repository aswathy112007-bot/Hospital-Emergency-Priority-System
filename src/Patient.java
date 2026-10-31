import java.time.LocalDateTime;

public class Patient {
    private String patientId;
    private String name;
    private int age;
    private String gender;
    private String contactNumber;
    private String symptoms;
    private LocalDateTime registrationTime;
    private String assessmentNotes;
    private int urgencyLevel; // 0 = Emergency Red, 1 = High, 2 = Moderate, 3 = Routine
    private boolean isEmergency;
    private String status; // Waiting, In Consultation, Referred, Consultation Completed

    public Patient(String patientId, String name, int age, String gender, String contactNumber, 
                   String symptoms, LocalDateTime registrationTime, String assessmentNotes, 
                   int urgencyLevel, boolean isEmergency, String status) {
        this.patientId = patientId;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.symptoms = symptoms;
        this.registrationTime = registrationTime;
        this.assessmentNotes = assessmentNotes;
        this.urgencyLevel = urgencyLevel;
        this.isEmergency = isEmergency;
        this.status = status;
    }

    // Getters and Setters
    public String getPatientId() { return patientId; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getContactNumber() { return contactNumber; }
    public String getSymptoms() { return symptoms; }
    public LocalDateTime getRegistrationTime() { return registrationTime; }
    public String getAssessmentNotes() { return assessmentNotes; }
    public void setAssessmentNotes(String assessmentNotes) { this.assessmentNotes = assessmentNotes; }
    public int getUrgencyLevel() { return urgencyLevel; }
    public void setUrgencyLevel(int urgencyLevel) { this.urgencyLevel = urgencyLevel; }
    public boolean isEmergency() { return isEmergency; }
    public void setEmergency(boolean emergency) { isEmergency = emergency; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}