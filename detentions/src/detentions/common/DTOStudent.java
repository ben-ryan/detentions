package detentions.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DTOStudent {
	
	private String studentName;
	private int detentionsToSet;
	private List<DTODetention> detentionsSet = new ArrayList<DTODetention>();
	
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
		
		List<Calendar> dates = new ArrayList<>();
		for (DTODetention detention : detentionsSet) {
			dates.add(detention.getDetentionDate());
		}
		return dates;
	}
	public List<DTODetention> getDetentionsSet() {
		return detentionsSet;
	}
	public void setDetentionsSet(List<DTODetention> detentionsSet) {
		this.detentionsSet = detentionsSet;
	}
	public void addDetentionSet(DTODetention detention) {
		this.detentionsSet.add(detention);
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
}
