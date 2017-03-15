package detentions.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import detentions.common.DTODetention;
import detentions.common.DTOFaculty;
import detentions.common.DTOStudent;

public class CsvUtils {

    public static SimpleDateFormat SDF_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
    
	public static Map<String, DTOStudent> importStudentData(String csvFilePath) throws ParseException, FileNotFoundException, IOException {
		
		String line = "";
        String csvSplitBy = ",";
        DTOStudent student;
        Map<String, DTOStudent> students = new TreeMap<>();
        
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
            	
            	if(line.startsWith("Student")) {
            		// Skip headings row
            		continue;
            	}
            	
            	int i = 0;

                String[] studentLine = line.split(csvSplitBy);
                student = new DTOStudent(studentLine[i++]); // Student Name
                
                i++; // Skip the "Oustanding Detentions" column
                
                DTODetention detention;
                while(i < studentLine.length) {
                    try {
                    	detention = new DTODetention();
                    	SimpleDateFormat SDF_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
                    	SDF_ddmmyyyy.parse(studentLine[i++]); // Detention Date
						detention.setDetentionDate(SDF_ddmmyyyy.getCalendar());
						detention.setFacultyName(studentLine[i++]); // Detention Faculty
						detention.setDetentionLength(Integer.parseInt(studentLine[i++])); // Detention Length
						student.addDetentionSet(detention);
					} catch (ArrayIndexOutOfBoundsException e) {
						throw new ArrayIndexOutOfBoundsException("Some data is missing from Students CSV file. Please check and try again.");
					} catch (ParseException e) {
						throw new ParseException("Error reading date format from Students CSV file. Please check and try again.", e.getErrorOffset());
					}
                }
				students.put(student.getStudentName(), student);
            }
		} catch (FileNotFoundException fnfe) {
			throw new FileNotFoundException("Cannot find Students CSV file at location " + csvFilePath);
		}
		return students;
	}
	
	public static void exportStudentData(Map<String, DTOStudent> students, String csvOutputFilePath) throws IOException {
		
		// remove detentions in the past
		removeStudentDatesInThePast(students);
		
		File outputFile = new File(csvOutputFilePath);
			
		try {
			try(FileWriter fw = new FileWriter(outputFile)) {
				
				List<String> headers = new ArrayList<>();
				headers.add("Student Name");
				headers.add("Oustanding Detentions");
				headers.add("Detention Date");
				headers.add("Detention Faculty");
				headers.add("Detention Length");
				CSVExportUtils.writeLine(fw, headers);
				
				for(String studentName : students.keySet()) {
					
					DTOStudent student = students.get(studentName);
					List<String> dataToWrite = new ArrayList<>();
					dataToWrite.add(studentName); // Student Name
					dataToWrite.add("" + student.getDetentionsSet().size()); // Outstanding Detentions
					for(DTODetention detention : student.getDetentionsSet()) {
						SimpleDateFormat SDF_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
						dataToWrite.add(SDF_ddmmyyyy.format(detention.getDetentionDate().getTime()));
						dataToWrite.add(detention.getFacultyName());
						dataToWrite.add("" + detention.getDetentionLength());
					}
					CSVExportUtils.writeLine(fw, dataToWrite);	
				}
					
			}
		} catch (IOException e) {
			throw new IOException("Error updating CSV file: "+csvOutputFilePath, e);
		}
		
	}
	
	public static Map<String, DTOFaculty> importFacultyData(String csvFilePath) throws FileNotFoundException, IOException, ParseException {
		
		String line = "";
        String csvSplitBy = ",";
        DTOFaculty faculty;
        Map<String, DTOFaculty> faculties = new TreeMap<>();
        
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
            	
            	if(line.startsWith("Faculty")) {
            		// Skip headings row
            		continue;
            	}

                String[] facultyLine = line.split(csvSplitBy);
                
                int parserPosition = 0;
                faculty = new DTOFaculty(facultyLine[parserPosition++]); // Faculty Name
                faculty.setRoomCapacity(Integer.parseInt(facultyLine[parserPosition++])); // Room Capacity
                faculty.setAvailableDays(parseAvailableDays(facultyLine, parserPosition)); // Available Days
                parserPosition += 5; // There will always be five columns for the Days
                faculty.setStudentsInRoomData(parseStudentsInRoomData(facultyLine, parserPosition)); // Current Room Capacity
                
				faculties.put(faculty.getFacultyName(), faculty);
            }
		} catch (FileNotFoundException fnfe) {
			throw new FileNotFoundException("Cannot find Faculty CSV file at location " + csvFilePath);
		}
		return faculties;
	}
	
	private static int parseDayOfWeek(String day)
            throws ParseException {
        SimpleDateFormat dayFormat = new SimpleDateFormat("E", Locale.UK);
        Date date = dayFormat.parse(day);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

	/**
	 * eg. Monday,Tuesday,Wednesday
	 * 
	 * become
	 * 
	 * {1,2,3}
	 * 
	 * @throws ParseException 
	 */
	private static List<Integer> parseAvailableDays(String[] facultyLine, int parserPosition) throws ParseException {

		List<Integer> availableDays = new ArrayList<>();
		try {
			for(int i = parserPosition; i < facultyLine.length; i++) {
				if(facultyLine[i].trim().equals("") || facultyLine[i] == null) {
					continue;
				} else if(!facultyLine[i].matches("([A-Z])([a-z])+")) {
					break;
				}
				availableDays.add(parseDayOfWeek(facultyLine[i]));
	        }
		} catch (ParseException e) {
			throw new ParseException("Error reading date format from Faculties CSV file. Please check and try again.", e.getErrorOffset());
		}
		
		return availableDays;
	}
	
	private static Map<Calendar, Integer> parseStudentsInRoomData(String[] facultyLine, int parserPosition) throws ParseException {
		
		Map<Calendar, Integer> studentsInRoom = new TreeMap<>();
		Integer numberOfStudents;
		
		try {
			int i = parserPosition;
			while(i < facultyLine.length) {
				SimpleDateFormat SDF_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
				SDF_ddmmyyyy.parse(facultyLine[i++]);
				Calendar day = SDF_ddmmyyyy.getCalendar();
				numberOfStudents = Integer.parseInt(facultyLine[i++]);
				studentsInRoom.put(day, numberOfStudents);
	        }
		} catch (ParseException e) {
			throw new ParseException("Error reading date format from Faculties CSV file. Please check and try again.", e.getErrorOffset());
		}
		
		return studentsInRoom;
	}

	public static void exportFacultyData(Map<String, DTOFaculty> faculties, String csvOutputFilePath) throws IOException {
		
		// remove dates in the past
		removeFacultyDatesInThePast(faculties);
		
		File outputFile = new File(csvOutputFilePath);
			
		try {
			try(FileWriter fw = new FileWriter(outputFile)) {
				
				List<String> headers = new ArrayList<>();
				headers.add("Faculty Name");
				headers.add("Room Capacity");
				headers.add("Available Days");
				headers.add("");
				headers.add("");
				headers.add("");
				headers.add("");
				headers.add("Current Students Scheduled in Room");
				CSVExportUtils.writeLine(fw, headers);
				
				for(String facultyName : faculties.keySet()) {
					
					DTOFaculty faculty = faculties.get(facultyName);
					List<String> dataToWrite = new ArrayList<>();
					dataToWrite.add(facultyName);
					dataToWrite.add("" + faculty.getRoomCapacity());
					for(int day : faculty.getAvailableDays()) {
						Calendar cal = Calendar.getInstance();
						cal.set(Calendar.DAY_OF_WEEK, day);
						dataToWrite.add("" + cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.UK));
					}
					for(int i = faculty.getAvailableDays().size(); i < 6; i++) {
						// may need to add blank columns to make it up to five
						dataToWrite.add("");
					}
					Set<Calendar> allDates = faculty.getStudentsInRoomData().keySet();
					for(Calendar cal : allDates) {
						SimpleDateFormat SDF_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
						dataToWrite.add(SDF_ddmmyyyy.format(cal.getTime()));
						dataToWrite.add(""+ faculty.getStudentsInRoomData().get(cal));
					}
						
					CSVExportUtils.writeLine(fw, dataToWrite);	
				}
					
			}
		} catch (IOException e) {
			throw new IOException("Error updating CSV file: "+csvOutputFilePath, e);
		}
		
	}

	private static void removeFacultyDatesInThePast(Map<String, DTOFaculty> faculties) {
		
		// Want to compare to the end of yesterday
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, -1);
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);

		for(String facultyName : faculties.keySet()) {
			DTOFaculty faculty = faculties.get(facultyName);
			Map<Calendar, Integer> roomData = faculty.getStudentsInRoomData();
			List<Calendar> datesToRemove = new ArrayList<>();
			for(Calendar date : roomData.keySet()) {
				if(date.before(today)) {
					datesToRemove.add(date);
				}
			}
			for(Calendar date : datesToRemove) {
				roomData.remove(date);
			}
		}
	}

	private static void removeStudentDatesInThePast(Map<String, DTOStudent> students) {
		
		// Want to compare to the end of yesterday
		Calendar today = Calendar.getInstance();
		today.add(Calendar.DATE, -1);
		today.set(Calendar.HOUR_OF_DAY, 23);
		today.set(Calendar.MINUTE, 59);
		today.set(Calendar.SECOND, 59);

		for(String studentName : students.keySet()) {
			DTOStudent student = students.get(studentName);
			List<DTODetention> studentDetentions = student.getDetentionsSet();
			List<DTODetention> detentionsToRemove = new ArrayList<>();
			for(DTODetention detention : studentDetentions) {
				if(detention.getDetentionDate().before(today)) {
					detentionsToRemove.add(detention);
				}
			}
			studentDetentions.removeAll(detentionsToRemove);
			student.setDetentionsSet(studentDetentions);
		}
	}

	public static List<DTODetention> importDetentionData(String csvFilePath) throws NumberFormatException, IOException, ParseException {
		String line = "";
        String csvSplitBy = ",";
        DTODetention detention;
        List<DTODetention> detentions = new ArrayList<>();
        
		try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            while ((line = br.readLine()) != null) {
            	
            	if(line.startsWith("Detention")) {
            		// Skip headings row
            		continue;
            	}

                String[] facultyLine = line.split(csvSplitBy);
                
                int parserPosition = 0;
                detention = new DTODetention(); 
                detention.setDetentionId(Integer.parseInt(facultyLine[parserPosition++])); // Detention ID
                SimpleDateFormat SDF_ddmmyyyy = new SimpleDateFormat("dd/MM/yyyy", Locale.UK);
            	detention.setDateSet(SDF_ddmmyyyy.parse(facultyLine[parserPosition++])); // Date Detention Set
                detention.setFacultyName(facultyLine[parserPosition++]); // Detention Faculty Name
                detention.setDetentionLength(Integer.parseInt(facultyLine[parserPosition++])); // Detention Length
                detention.setStudentName((facultyLine[parserPosition++])); // Student Name
                
                detentions.add(detention);
            }
		} catch (FileNotFoundException fnfe) {
			throw new FileNotFoundException("Cannot find Detentions CSV file at location " + csvFilePath);
		}
		return detentions;
	}
	
	
}
