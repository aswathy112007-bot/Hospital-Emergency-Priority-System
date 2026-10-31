import java.util.Comparator;

public class PatientComparator implements Comparator<Patient> {
    @Override
    public int compare(Patient p1, Patient p2) {
        // Emergency alerts take absolute precedence
        if (p1.isEmergency() && !p2.isEmergency()) return -1;
        if (!p1.isEmergency() && p2.isEmergency()) return 1;

        // Compare clinician-assigned urgency level (0 highest, 3 lowest)
        if (p1.getUrgencyLevel() != p2.getUrgencyLevel()) {
            return Integer.compare(p1.getUrgencyLevel(), p2.getUrgencyLevel());
        }

        // FIFO tie-breaker: Earlier registration time comes first
        return p1.getRegistrationTime().compareTo(p2.getRegistrationTime());
    }
}