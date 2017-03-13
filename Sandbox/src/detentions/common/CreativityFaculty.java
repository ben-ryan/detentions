package detentions.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreativityFaculty implements FacultyIF {

	private Map<Calendar,Integer> studentsInRoomData = new HashMap<>();

	public int getRoomCapacity() {
		return 30;
	}
	
	public List<Integer> getAvailableDays() {
		List<Integer> availableDays = new ArrayList<>();
		availableDays.add(Calendar.TUESDAY);
		availableDays.add(Calendar.THURSDAY);
		return availableDays;
	}
	
	public String getFacultyName() {
		return "Creativity";
	}

	@Override
	public void addStudentToRoom(Calendar cal) {
		if(studentsInRoomData.containsKey(cal)) {
			Integer studentsInRoom = studentsInRoomData.get(cal);
			studentsInRoom++;
			studentsInRoomData.remove(cal);
			studentsInRoomData.put(cal, studentsInRoom);
		} else {
			studentsInRoomData.put(cal, 1);
		}
	}

	@Override
	public int getCurrentStudentsInRoom(Calendar cal) {
		return studentsInRoomData.get(cal);
	}

}
