package detentions.client;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.security.sasl.digest.FactoryImpl;

import detentions.common.CharacterFaculty;
import detentions.common.ConfidenceFaculty;
import detentions.common.CreativityFaculty;
import detentions.common.DTODetention;
import detentions.common.DTOStudent;
import detentions.common.FacultyIF;
import detentions.common.LiteracyFaculty;
import detentions.common.MathsFaculty;
import detentions.common.PastoralFaculty;
import detentions.common.SLTFaculty;
import detentions.common.ScienceFaculty;

public class DetentionsUI {
	
	private static FacultyIF mathsFaculty = new MathsFaculty();
	private static FacultyIF creativityFaculty = new CreativityFaculty();
	private static FacultyIF characterFaculty = new CharacterFaculty();
	private static FacultyIF confidenceFaculty = new ConfidenceFaculty();
	private static FacultyIF literacyFaculty = new LiteracyFaculty();
	private static FacultyIF pastoralFaculty = new PastoralFaculty();
	private static FacultyIF scienceFaculty = new ScienceFaculty();
	private static FacultyIF sltFaculty = new SLTFaculty();
	
	private static Map<String, DTOStudent> students = new HashMap<>();

	public static DTOStudent setDetention(DTODetention detention) {

		FacultyIF faculty = getFaculty(detention.getFacultyName());
		DTOStudent student = getStudent(detention.getStudentName());
		
		Calendar cal = new GregorianCalendar();
		cal = checkFacultyAvailability(faculty, cal, student);
				
		// cal now represents the potential detention day.
			
		assignStudentToDetentionDate(student, faculty, cal, detention);
		return student;
	}

	private static void assignStudentToDetentionDate(DTOStudent student, FacultyIF faculty, Calendar cal,
			DTODetention detention) {
		
		List<Calendar> currentDetentionDates = student.getDetentionDates();
		if(currentDetentionDates == null) {
			currentDetentionDates = new ArrayList<Calendar>();
		}
		currentDetentionDates.add(cal);
		student.setDetentionDates(currentDetentionDates);
		students.put(student.getStudentName(), student);
		
		faculty.addStudentToRoom(cal);
	}

	private static Calendar checkFacultyAvailability(FacultyIF faculty, Calendar cal, DTOStudent student) {
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

	private static boolean isThereSpaceInRoom(FacultyIF faculty, Calendar cal) {
		
		try {
			if (faculty.getRoomCapacity() > faculty.getCurrentStudentsInRoom(cal)) {
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

		if(students.containsKey(studentName)) {
			return students.get(studentName);
		} else {
			return new DTOStudent(studentName);
		}
	}

	private static FacultyIF getFaculty(String facultyName) {

		if(facultyName.equals(mathsFaculty.getFacultyName())) {
			return mathsFaculty;
		} if (facultyName.equals(creativityFaculty.getFacultyName())) {
			return creativityFaculty;
		} if (facultyName.equals(characterFaculty.getFacultyName())) {
			return characterFaculty;
		} if (facultyName.equals(confidenceFaculty.getFacultyName())) {
			return confidenceFaculty;
		} if (facultyName.equals(literacyFaculty.getFacultyName())) {
			return literacyFaculty;
		} if (facultyName.equals(pastoralFaculty.getFacultyName())) {
			return pastoralFaculty;
		} if (facultyName.equals(scienceFaculty.getFacultyName())) {
			return scienceFaculty;
		} if (facultyName.equals(sltFaculty.getFacultyName())) {
			return sltFaculty;
		}
		return pastoralFaculty;
	}

}
