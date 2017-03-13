package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InternetExample {


	    public static void main(String[] args) {
	        try {
	            BufferedReader reader = new BufferedReader(new FileReader(args[0]));
	            String line;
	            while ((line = reader.readLine()) != null) {
	            	long startTime = System.currentTimeMillis();
	          	  System.out.println("Start time: " + startTime);
	                System.out.println(reassemble(line)); 
	                long endTime = System.currentTimeMillis();
	        	      System.out.println("Finish time: " + endTime);
	        	      long timeTaken = endTime - startTime;
	        	      System.out.println("Time taken (ms): " + timeTaken);
	            }
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    /**
	     * Assemble the fragments in the correct order.
	     * 
	     * @param line
	     *            String containing the fragments of the document.
	     * @return the line in the correct order.
	     */
	    private static String reassemble(String line) {

	        // Each line contains text fragments separated by a semicolon.
	        String[] fragments = line.split(";");
	        List<String> list = new ArrayList<String>(Arrays.asList(fragments));

	        // Sort the fragments by size
	        Collections.sort(list, new Comparator<String>() {
	            @Override
	            public int compare(String o1, String o2) {
	                return o2.length() - o1.length();
	            }
	        });

	        // Pick up the first fragment
	        String text = list.get(0);
	        list.remove(0);

	        // Start the algorithm
	        for (int i = list.size() - 1; i >= 0; i--) {
	            int max = 0;
	            int idx = 0;
	            int match = 0;
	            int m = 0;
	            int n = 0;
	            for (int j = list.size() - 1; j >= 0; j--) {
	                String findMe = list.get(j);
	                m = text.length();
	                n = findMe.length();
	                for (int k = 1 - findMe.length(); k < text.length(); k++) {
	                    if (k < 0) { // Prefix
	                        int l = n + k;
	                        if (text.regionMatches(0, findMe, -k, l)) {
	                            if (l > max) {
	                                idx = k;
	                                max = l;
	                                match = j;
	                            }
	                        }
	                    } else { // Suffix
	                        int l = k + n <= m ? n : m - k;
	                        if (text.regionMatches(k, findMe, 0, l)) {
	                            if (l > max) {
	                                idx = k;
	                                max = l;
	                                match = j;
	                            }
	                        }
	                    }
	                }
	            }

	            if (idx < 0) { // Prefix
	                text = list.get(match).substring(0, -idx) + text;
	            } else if (idx > m - list.get(match).length()) { // Suffix
	                text = text + list.get(match).substring(m - idx);
	            }
	            list.remove(match);
	        }
	        return text;
	    }

	

}
