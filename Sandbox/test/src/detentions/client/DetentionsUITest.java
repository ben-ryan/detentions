package detentions.client;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;

import org.junit.Test;

import detentions.common.DTODetention;
import detentions.common.DTOStudent;
import detentions.utils.CsvUtils;

public class DetentionsUITest {

	@Test
	public void testSetDetention() {
		DTODetention detention = new DTODetention();
		detention.setDateSet(new GregorianCalendar().getTime());
		detention.setDetentionId(9999);
		detention.setDetentionLength(60);
		detention.setStudentName("Alfie Richardson");
		detention.setFacultyName("Maths");
		
		DTOStudent student = DetentionsUI.setDetention(detention);
		// Next Tuesday
		assertEquals(new Date(117, 02, 14).getDate(), student.getDetentionDates().get(0));		
	}
	
	@Test
	public void testNextDayOfWeek() {
		Calendar day = DetentionsUI.nextDayOfWeek(Calendar.MONDAY, 0);
		assertEquals(13, day.get(Calendar.DATE));
	}
	
	@Test
	public void testImportStudents() throws Exception {
		Map<String, DTOStudent> students = CsvUtils.importStudentData("C:\\Users\\Ben Ryan\\Documents\\TestData\\Students.csv");
		assertTrue(!students.isEmpty());
	}
	
	@Test
	public void testNextDayOfWeek_nextWeek() {
		Calendar day = DetentionsUI.nextDayOfWeek(Calendar.MONDAY, 7);
		assertEquals(20, day.get(Calendar.DATE));
	}

}
