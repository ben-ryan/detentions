package detentions.client;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import detentions.common.DTODetention;
import detentions.common.DTOFaculty;
import detentions.common.DTOStudent;
import detentions.server.DetentionModel;
import detentions.utils.CsvUtils;

public class DetentionsModelTest {
	
	DetentionModel uut = new DetentionModel();

	@Test
	public void testSetDetention() {
		DTODetention detention = new DTODetention();
		detention.setDateSet(new GregorianCalendar().getTime());
		detention.setDetentionId(9999);
		detention.setDetentionLength(60);
		detention.setStudentName("Alfie Richardson");
		detention.setFacultyName("Maths");
		
		List<DTODetention> detentions = new ArrayList<>();
		detentions.add(detention);
		
		uut.setDetentions(detentions);
		// Next Tuesday
//		assertEquals(new Date(117, 02, 14).getDate(), .getDetentionDates().get(0));		
	}
	
	@Test
	public void testNextDayOfWeek() {
		Calendar day = uut.nextDayOfWeek(Calendar.MONDAY, 0);
		assertEquals(13, day.get(Calendar.DATE));
	}
	
	@Test
	public void testImportStudents() throws Exception {
		Map<String, DTOStudent> students = CsvUtils.importStudentData("H:\\detentions\\test\\data\\Students_exported.csv");
		CsvUtils.exportStudentData(students, "H:\\detentions\\test\\data\\Students_exported_2.csv");
		assertTrue(!students.isEmpty());
	}
	
	@Test
	public void testImportExportFaculties() throws Exception {
		Map<String, DTOFaculty> faculties = CsvUtils.importFacultyData("H:\\detentions\\test\\data\\Faculties.csv");
		CsvUtils.exportFacultyData(faculties, "H:\\detentions\\test\\data\\Faculties_exported.csv");
		assertTrue(!faculties.isEmpty());
	}
	
	@Test
	public void testNextDayOfWeek_nextWeek() {
		Calendar day = uut.nextDayOfWeek(Calendar.MONDAY, 7);
		assertEquals(20, day.get(Calendar.DATE));
	}

}
