package detentions.common;

import java.util.Calendar;
import java.util.List;

public interface FacultyIF {
	
	public void addStudentToRoom(Calendar cal);
	
	public int getCurrentStudentsInRoom(Calendar cal);
	
	public abstract int getRoomCapacity();
	
	public abstract List<Integer> getAvailableDays();
	
	public abstract String getFacultyName();

}
