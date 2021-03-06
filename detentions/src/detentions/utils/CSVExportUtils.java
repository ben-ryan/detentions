package detentions.utils;

import java.util.List;
import java.io.IOException;
import java.io.Writer;

public class CSVExportUtils {
	
	 private static final char DEFAULT_SEPARATOR = ',';

	    private static String followCSVformat(String value) {

	        String result = value;
	        if (result.contains("\"")) {
	            result = result.replace("\"", "\"\"");
	        }
	        return result;
	    }

	    public static void writeLine(Writer w, List<String> values) throws IOException {

	        boolean first = true;

	        char separators = DEFAULT_SEPARATOR;

	        StringBuilder sb = new StringBuilder();
	        for (String value : values) {
	            if (!first) {
	                sb.append(separators);
	            }
	            sb.append(followCSVformat(value));

	            first = false;
	        }
	        sb.append("\n");
	        w.append(sb.toString());
	    }
}
