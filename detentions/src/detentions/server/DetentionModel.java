package detentions.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import detentions.common.DTODetention;
import detentions.common.DTOFaculty;
import detentions.common.DTOStudent;

public class DetentionModel {
	
	private static Map<String, DTOStudent> students = new HashMap<>();
	private static Map<String, DTOFaculty> faculties = new HashMap<>();
	
	public static void loadStudentData(Map<String, DTOStudent> studentData) {
		setStudents(studentData);
	}
	
	public static void loadFacultyData(Map<String, DTOFaculty> facultyData) {
		setFaculties(facultyData);
	}
	
	public static List<DTODetention> setDetentions(List<DTODetention> detentions) {
		for (DTODetention detention : detentions) {
			setDetention(detention);
		}
		return detentions;
	}

	private static void setDetention(DTODetention detention) {

		DTOFaculty faculty = getFaculty(detention.getFacultyName());
		DTOStudent student = getStudent(detention.getStudentName());
		
		Calendar cal = new GregorianCalendar();
		cal = checkFacultyAvailability(faculty, cal, student);
				
		// cal now represents the potential detention day.
			
		assignStudentToDetentionDate(student, faculty, cal, detention);
	}

	private static void assignStudentToDetentionDate(DTOStudent student, DTOFaculty faculty, Calendar detentionDate, DTODetention detention) {
		
		List<Calendar> currentDetentionDates = student.getDetentionDates();
		currentDetentionDates.add(detentionDate);
		detention.setDetentionDate(detentionDate);
		student.addDetentionSet(detention);
		getStudents().put(student.getStudentName(), student);
		
		faculty.addStudentToRoom(detentionDate);
	}

	private static Calendar checkFacultyAvailability(DTOFaculty faculty, Calendar cal, DTOStudent student) {
		Calendar detentionDay = null;
		Calendar potentialDay = null;
		int daysOffset = 0;
		while (detentionDay == null) {
			for (int facultyDay : faculty.getAvailableDays()) {
				potentialDay = nextDayOfWeek(facultyDay, daysOffset);
				if(isThereSpaceInRoom(faculty, potentialDay)) {
					if(isStudentAvailable(student, potentialDay)) {
						return potentialDay;
					}
				}
			}
			// Try next week
			daysOffset += 7;
		}	
		// Failed to find a suitable day, ever.
		return potentialDay;
	}
	
	public static Calendar nextDayOfWeek(int dow, int daysOffset) {
        Calendar date = Calendar.getInstance();
        int diff = dow - date.get(Calendar.DAY_OF_WEEK);
        if (!(diff > 0)) {
            diff += (7 + daysOffset);
        }
        date.add(Calendar.DAY_OF_MONTH, diff);
        return date;
    }

	private static boolean isStudentAvailable(DTOStudent student, Calendar cal) {
		try {
			if(student.getDetentionDates().contains(cal)) {
				return false;
			} else {
				return true;
			}
		} catch (NullPointerException npe) {
			// This is the first student to be put in this room
			return true;
		}	
	}

	private static boolean isThereSpaceInRoom(DTOFaculty faculty, Calendar cal) {
		
		try {
			if (faculty.getRoomCapacity() > faculty.getStudentsInRoomOnDay(cal)) {
				return true;
			} else {
				return false;
			}
		} catch (NullPointerException npe) {
			// This is the first student to be put in this room
			return true;
		}
	}

	private static DTOStudent getStudent(String studentName) {

		if(getStudents().containsKey(studentName)) {
			return getStudents().get(studentName);
		} else {
			return new DTOStudent(studentName);
		}
	}

	private static DTOFaculty getFaculty(String facultyName) {
		
		if(getFaculties().containsKey(facultyName)) {
			return getFaculties().get(facultyName);
		} else {
			throw new IllegalArgumentException("The faculty '" + facultyName + "' was not found in the Faculties CSV file uploaded.");
		}
	}

	public static Map<String, DTOStudent> getStudents() {
		return students;
	}

	public static void setStudents(Map<String, DTOStudent> students) {
		DetentionModel.students = students;
	}

	public static Map<String, DTOFaculty> getFaculties() {
		return faculties;
	}

	public static void setFaculties(Map<String, DTOFaculty> faculties) {
		DetentionModel.faculties = faculties;
	}

}
