package detentions.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import detentions.common.DTOStudent;
import detentions.common.FacultyIF;

public class CsvUtils {
	
	public static Map<String, DTOStudent> importStudentData(String csvFilePath) throws ParseException, FileNotFoundException, IOException {
		
		String line = "";
        String csvSplitBy = ",";
        DTOStudent student;
        Map<String, DTOStudent> students = new HashMap<>();
        
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
            	
            	if(line.startsWith("Student")) {
            		// Skip headings row
            		continue;
            	}

                String[] studentLine = line.split(csvSplitBy);
                student = new DTOStudent(studentLine[0]);
                List<Calendar> detentionDates = new ArrayList<>();
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                
                for(int i = 1; i < studentLine.length; i++) {
                    try {
						sdf.parse(studentLine[i]);
					} catch (ParseException e) {
						throw new ParseException("Error reading date format from CSV file. Please check and try again.", e.getErrorOffset());
					}
                    Calendar cal = sdf.getCalendar();
                	detentionDates.add(cal);
                }
				student.setDetentionDates(detentionDates);
				students.put(student.getStudentName(), student);
            }
		} return students;
	}
	
	public static void exportStudentData(Map<String, DTOStudent> students, String csvOutputFilePath) {
		
	}
	
	public static Map<String, FacultyIF> importFacultyData(String csvFilePath) {
		
	}
	
	public static void exportFacultyData(Map<String, FacultyIF> faculties, String csvOutputFilePath) {
		
	}


}
