package detentions.common;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DTOFaculty {

	private Map<Calendar,Integer> studentsInRoomData = new HashMap<>();
	private int roomCapacity;
	private List<Integer> availableDays;
	private String facultyName;
	
	public DTOFaculty(String facultyName) {
		setFacultyName(facultyName);
	}
	
	public int getRoomCapacity() {
		return roomCapacity;
	}
	
	public void setRoomCapacity(int roomCapacity) {
		this.roomCapacity = roomCapacity;
	}

	public void addStudentToRoom(Calendar cal) {
		if(getStudentsInRoomData().containsKey(cal)) {
			Integer studentsInRoom = getStudentsInRoomData().get(cal);
			studentsInRoom++;
			getStudentsInRoomData().remove(cal);
			getStudentsInRoomData().put(cal, studentsInRoom);
		} else {
			getStudentsInRoomData().put(cal, 1);
		}
	}

	public int getStudentsInRoomOnDay(Calendar cal) {
		return getStudentsInRoomData().get(cal);
	}

	public List<Integer> getAvailableDays() {
		return availableDays;
	}

	public void setAvailableDays(List<Integer> availableDays) {
		this.availableDays = availableDays;
	}

	public String getFacultyName() {
		return facultyName;
	}

	public void setFacultyName(String facultyName) {
		this.facultyName = facultyName;
	}

	public Map<Calendar,Integer> getStudentsInRoomData() {
		return studentsInRoomData;
	}

	public void setStudentsInRoomData(Map<Calendar,Integer> studentsInRoomData) {
		this.studentsInRoomData = studentsInRoomData;
	}

}
