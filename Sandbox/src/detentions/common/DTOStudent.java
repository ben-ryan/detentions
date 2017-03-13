package detentions.common;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DTOStudent {
	
	private String studentName;
	private int detentionsToSet;
	private List<Calendar> detentionDates;
	
	public DTOStudent(String studentName) {
		setStudentName(studentName);
	}
	
	public int getDetentionsToSet() {
		return detentionsToSet;
	}
	public void setDetentionsToSet(int detentionsToSet) {
		this.detentionsToSet = detentionsToSet;
	}
	public List<Calendar> getDetentionDates() {
		return detentionDates;
	}
	public void setDetentionDates(List<Calendar> detentionDates) {
		this.detentionDates = detentionDates;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
}
