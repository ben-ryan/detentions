package detentions.common;

import java.util.Calendar;
import java.util.Date;

public class DTODetention {
	
	private int detentionId;
	private Date dateSet;
	private String studentName;
	private String facultyName;
	private int detentionLength;
	private Calendar detentionDate;
	
	public int getDetentionId() {
		return detentionId;
	}
	public void setDetentionId(int detentionId) {
		this.detentionId = detentionId;
	}
	public Date getDateSet() {
		return dateSet;
	}
	public void setDateSet(Date dateSet) {
		this.dateSet = dateSet;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getFacultyName() {
		return facultyName;
	}
	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}
	public int getDetentionLength() {
		return detentionLength;
	}
	public void setDetentionLength(int detentionLength) {
		this.detentionLength = detentionLength;
	}
	public Calendar getDetentionDate() {
		return detentionDate;
	}
	public void setDetentionDate(Calendar detentionDate) {
		this.detentionDate = detentionDate;
	}

}
