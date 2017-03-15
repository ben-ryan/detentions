package detentions.client;

import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import detentions.common.DTODetention;
import detentions.common.DTOFaculty;
import detentions.common.DTOStudent;
import detentions.server.DetentionModel;
import detentions.utils.CsvUtils;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;
import java.awt.event.ActionEvent;

public class DetentionsUI extends JDialog {
	
	public static final FileNameExtensionFilter FILENAME_FILTER =
		    new FileNameExtensionFilter("CSV", "csv");
	
	private static final Preferences prefs = Preferences.userNodeForPackage(DetentionsUI.class);
	private static final String lastFilePref = "last_file";
	
	private JTextField txtStudentsLocation;
	private JTextField txtFacultiesLocation;
	private JTextField txtDetentionsLocation;
	
	public DetentionsUI() {
		
		jbinit();
		
	}

	private void jbinit() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblLblspacertl = new JLabel(" ");
		GridBagConstraints gbc_lblLblspacertl = new GridBagConstraints();
		gbc_lblLblspacertl.insets = new Insets(0, 0, 5, 5);
		gbc_lblLblspacertl.gridx = 1;
		gbc_lblLblspacertl.gridy = 0;
		getContentPane().add(lblLblspacertl, gbc_lblLblspacertl);
		
		JLabel lblSpacerTR = new JLabel("  ");
		GridBagConstraints gbc_lblSpacerTR = new GridBagConstraints();
		gbc_lblSpacerTR.insets = new Insets(0, 0, 5, 0);
		gbc_lblSpacerTR.gridx = 6;
		gbc_lblSpacerTR.gridy = 0;
		getContentPane().add(lblSpacerTR, gbc_lblSpacerTR);
		
		JLabel lblStudentscsvLocation = new JLabel("Students.csv Location:");
		GridBagConstraints gbc_lblStudentscsvLocation = new GridBagConstraints();
		gbc_lblStudentscsvLocation.anchor = GridBagConstraints.EAST;
		gbc_lblStudentscsvLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblStudentscsvLocation.gridx = 2;
		gbc_lblStudentscsvLocation.gridy = 1;
		getContentPane().add(lblStudentscsvLocation, gbc_lblStudentscsvLocation);
		
		JButton btnSelectStudentsFile = new JButton("Select File...");
		btnSelectStudentsFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectStudents_actionPerformed();
			}
		});
		GridBagConstraints gbc_btnSelectStudentsFile = new GridBagConstraints();
		gbc_btnSelectStudentsFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnSelectStudentsFile.gridx = 3;
		gbc_btnSelectStudentsFile.gridy = 1;
		getContentPane().add(btnSelectStudentsFile, gbc_btnSelectStudentsFile);
		
		txtStudentsLocation = new JTextField();
		GridBagConstraints gbc_txtStudentsLocation = new GridBagConstraints();
		gbc_txtStudentsLocation.gridwidth = 2;
		gbc_txtStudentsLocation.insets = new Insets(0, 0, 5, 5);
		gbc_txtStudentsLocation.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtStudentsLocation.gridx = 4;
		gbc_txtStudentsLocation.gridy = 1;
		getContentPane().add(txtStudentsLocation, gbc_txtStudentsLocation);
		txtStudentsLocation.setColumns(10);
		
		JLabel lblFacultiescsvLocation = new JLabel("Faculties.csv Location:");
		GridBagConstraints gbc_lblFacultiescsvLocation = new GridBagConstraints();
		gbc_lblFacultiescsvLocation.anchor = GridBagConstraints.EAST;
		gbc_lblFacultiescsvLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblFacultiescsvLocation.gridx = 2;
		gbc_lblFacultiescsvLocation.gridy = 2;
		getContentPane().add(lblFacultiescsvLocation, gbc_lblFacultiescsvLocation);
		
		JButton btnSelectFacultiesFile = new JButton("Select File...");
		btnSelectFacultiesFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectFaculties_actionPerformed();
			}
		});
		GridBagConstraints gbc_btnSelectFacultiesFile = new GridBagConstraints();
		gbc_btnSelectFacultiesFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnSelectFacultiesFile.gridx = 3;
		gbc_btnSelectFacultiesFile.gridy = 2;
		getContentPane().add(btnSelectFacultiesFile, gbc_btnSelectFacultiesFile);
		
		txtFacultiesLocation = new JTextField();
		txtFacultiesLocation.setColumns(10);
		GridBagConstraints gbc_txtFacultiesLocation = new GridBagConstraints();
		gbc_txtFacultiesLocation.gridwidth = 2;
		gbc_txtFacultiesLocation.insets = new Insets(0, 0, 5, 5);
		gbc_txtFacultiesLocation.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtFacultiesLocation.gridx = 4;
		gbc_txtFacultiesLocation.gridy = 2;
		getContentPane().add(txtFacultiesLocation, gbc_txtFacultiesLocation);
		
		JLabel lblDetentionscsvLocation = new JLabel("Detentions.csv Location:");
		GridBagConstraints gbc_lblDetentionscsvLocation = new GridBagConstraints();
		gbc_lblDetentionscsvLocation.anchor = GridBagConstraints.EAST;
		gbc_lblDetentionscsvLocation.insets = new Insets(0, 0, 5, 5);
		gbc_lblDetentionscsvLocation.gridx = 2;
		gbc_lblDetentionscsvLocation.gridy = 3;
		getContentPane().add(lblDetentionscsvLocation, gbc_lblDetentionscsvLocation);
		
		JButton btnSelectDetentionsFile = new JButton("Select File...");
		btnSelectDetentionsFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSelectDetentions_actionPerformed();
			}
		});
		GridBagConstraints gbc_btnSelectDetentionsFile = new GridBagConstraints();
		gbc_btnSelectDetentionsFile.insets = new Insets(0, 0, 5, 5);
		gbc_btnSelectDetentionsFile.gridx = 3;
		gbc_btnSelectDetentionsFile.gridy = 3;
		getContentPane().add(btnSelectDetentionsFile, gbc_btnSelectDetentionsFile);
		
		txtDetentionsLocation = new JTextField();
		txtDetentionsLocation.setColumns(10);
		GridBagConstraints gbc_txtDetentionsLocation = new GridBagConstraints();
		gbc_txtDetentionsLocation.gridwidth = 2;
		gbc_txtDetentionsLocation.insets = new Insets(0, 0, 5, 5);
		gbc_txtDetentionsLocation.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtDetentionsLocation.gridx = 4;
		gbc_txtDetentionsLocation.gridy = 3;
		getContentPane().add(txtDetentionsLocation, gbc_txtDetentionsLocation);
		
		JLabel label = new JLabel("  ");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 6;
		getContentPane().add(label, gbc_label);
		
		JLabel label_1 = new JLabel("  ");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 5;
		gbc_label_1.gridy = 4;
		getContentPane().add(label_1, gbc_label_1);
		
		JButton btnAssignDetentions = new JButton("Assign Detentions");
		btnAssignDetentions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnAssignDetentions_actionPerformed();
			}
		});
		GridBagConstraints gbc_btnAssignDetentions = new GridBagConstraints();
		gbc_btnAssignDetentions.insets = new Insets(0, 0, 0, 5);
		gbc_btnAssignDetentions.gridx = 3;
		gbc_btnAssignDetentions.gridy = 5;
		getContentPane().add(btnAssignDetentions, gbc_btnAssignDetentions);
	}

	protected void btnAssignDetentions_actionPerformed() {
		

		if(txtStudentsLocation.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter location of Students CSV File", "Cannot Locate File", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(txtFacultiesLocation.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Please enter location of Faculties CSV File", "Cannot Locate File", JOptionPane.ERROR_MESSAGE);
			return;
		}
		if(txtDetentionsLocation.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "Please select location of Detentions CSV File", "Cannot Locate File", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		Map<String, DTOFaculty> facultyData = null;
		Map<String, DTOStudent> studentData = null;
		List<DTODetention> detentionData = null;
		try {
			facultyData = CsvUtils.importFacultyData(txtFacultiesLocation.getText());
			studentData = CsvUtils.importStudentData(txtStudentsLocation.getText());
			detentionData = CsvUtils.importDetentionData(txtDetentionsLocation.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, e.toString(), "Error Reading CSV File(s)", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		} 
		DetentionModel.loadFacultyData(facultyData);
		DetentionModel.loadStudentData(studentData);
		
		detentionData = DetentionModel.setDetentions(detentionData);
		
		
		
		try {
			CsvUtils.exportFacultyData(DetentionModel.getFaculties(), txtFacultiesLocation.getText());
			CsvUtils.exportStudentData(DetentionModel.getStudents(), txtStudentsLocation.getText());
//			detentionData = CsvUtils.exportDetentionData(detentionData, txtDetentionsLocation.getText());
		} catch (Exception e) {
			new JOptionPane(e.getMessage(), JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return;
		} 
		
	}

	protected void btnSelectStudents_actionPerformed() {
		selectCsvFile(txtStudentsLocation);
	}

	protected void btnSelectFaculties_actionPerformed() {
		selectCsvFile(txtFacultiesLocation);
		
	}

	protected void btnSelectDetentions_actionPerformed() {
		selectCsvFile(txtDetentionsLocation);
		
	}

	private void selectCsvFile(JTextField textField) {
		JFileChooser fc = new JFileChooser();
	    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	    fc.setAcceptAllFileFilterUsed(false);
	    fc.setFileFilter(FILENAME_FILTER);
	    
	    // get last opened location, if it exists
	    Path lastFileLoc = Paths.get(prefs.get(lastFilePref, ""));
	    if (Files.exists(lastFileLoc)) {
	      fc.setCurrentDirectory(lastFileLoc.toFile());
	    }
	    
	    int returnState = fc.showOpenDialog(this);
	    
	    if (returnState == JFileChooser.APPROVE_OPTION) {
	        Path dest = fc.getSelectedFile().toPath();

	        if (!FILENAME_FILTER.accept(dest.toFile())) {
	          dest =
	            Paths.get(dest.toAbsolutePath() + "." + FILENAME_FILTER.getExtensions()[0]);
	        }
	        prefs.put(lastFilePref, dest.toAbsolutePath().toString());
	        textField.setText(dest.toString());
	     } else {
	    	 textField.setText("");
	     }
	     
	}
	
	  public static void main(String[] args) {
	
		    SwingUtilities.invokeLater(new Runnable() {
		      @Override
		      public void run() {
	
		        DetentionsUI ui = new DetentionsUI();
		        ui.pack();
		        ui.setSize(new Dimension(600, 200));
		        ui.setTitle("Assign Detentions");
		        ui.setLocationRelativeTo(null);
		        ui.setVisible(true);
		      }
		    });
		  }

}
