package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BenedictRyanOld {

	public static void main(String[] args) { 
		try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) { 
			String fragmentProblem; 
			while ((fragmentProblem = in.readLine()) != null) { 
				long startTime = System.currentTimeMillis();
		    	  System.out.println("Start time: " + startTime);
		          System.out.println(reassemble(fragmentProblem)); 
		          long endTime = System.currentTimeMillis();
		  	      System.out.println("Finish time: " + endTime);
		  	      long timeTaken = endTime - startTime;
		  	      System.out.println("Time taken (ms): " + timeTaken);
			 } 
	  	} catch (Exception e) { 
	  		e.printStackTrace(); 
	  	} 
	}

	private static String reassemble(String fragmentProblem) {

		List<String> fragments = getFragmentsAsList(fragmentProblem);
		return reassembleRecursively(fragments);
	}

	private static String reassembleRecursively(List<String> fragments) {
						
		FragmentPair currentOverlapPair = new FragmentPair();
		for(String fragment : fragments) {
			if(fragment.isEmpty()) {
				continue;
			}
			ArrayList<String> innerFragments = new ArrayList<String>(fragments);
			innerFragments.remove(fragment);
			// Compare this fragment against all others
			for(String innerFragment : innerFragments) {
 				if(innerFragment.isEmpty()) {
 					continue;
 				}
 				FragmentPair newOverlapPair = findOverlap(fragment, innerFragment);
				if(newOverlapPair.getOverlap() > currentOverlapPair.getOverlap()) {
					currentOverlapPair = newOverlapPair;
				}
			}
			innerFragments.add(fragment);			
		}
		fragments.remove(currentOverlapPair.getFirstArg());
		fragments.remove(currentOverlapPair.getSecondArg());
		fragments.add(currentOverlapPair.getMergedFragment());
		fragments.removeAll(Arrays.asList(null,""));
		
		if(fragments.size() > 1) {
			reassembleRecursively(fragments);
		} 
		
		return fragments.get(0);
	}

	/**
	 * Compares the final characters of the first parameter with the first characters
	 * of the second in order to determine the overlap between them, if any.
	 * 
	 * @param fragment
	 * @param innerFragment
	 * @return overlap
	 */
	private static FragmentPair findOverlap(String fragment, String innerFragment) {

		FragmentPair fragmentPair = new FragmentPair();
		fragmentPair.setFirstArg(fragment);
		fragmentPair.setSecondArg(innerFragment);
		
		String largerFragment = "";
		String smallerFragment = "";
		if(fragment.length() > innerFragment.length()) {
			largerFragment = fragment;
			smallerFragment = innerFragment;
			
		} else {
			largerFragment = innerFragment;
			smallerFragment = fragment;
		}
		
	    int maxPossOverlap = smallerFragment.length();

	    // Deal with the special case where one fragment fully contains the other
		if(largerFragment.contains(smallerFragment)) {
			fragmentPair.setOverlap(maxPossOverlap);
			fragmentPair.setMergedFragment(largerFragment);
			return fragmentPair;
		}
	    
		for(int i = maxPossOverlap; i > 0; i--) {
			if(fragment.regionMatches(fragment.length() - i, innerFragment, 0, i)) {
				fragmentPair.setOverlap(i);
				fragmentPair.setMergedFragment(fragment.concat(innerFragment.substring(i)));
				return fragmentPair;
			} 
		}
		return fragmentPair;
	}

	private static List<String> getFragmentsAsList(String fragmentProblem) {

		String[] fragmentsArray = fragmentProblem.split(";");
		return new ArrayList<String>(Arrays.asList(fragmentsArray));
	}
	
	/**
	 * The order of the two String parameters is important.
	 *
	 */
	public static class FragmentPair {

	    private String firstArg;
	    private String secondArg;
	    private String mergedFragment;
	    private int overlap;
	    
	    public FragmentPair() {
	        this.setFirstArg("");
	        this.setSecondArg("");
	        this.setMergedFragment("");
	        this.setOverlap(0);
	    }

	    //TODO delete?
	    public FragmentPair(String firstArg, String secondArg, int overlap) {
	        this.setFirstArg(firstArg);
	        this.setSecondArg(secondArg);
	        this.setOverlap(overlap);
	    }

		public int getOverlap() {
			return overlap;
		}

		public String getFirstArg() {
			return firstArg;
		}

		public void setFirstArg(String firstArg) {
			this.firstArg = firstArg;
		}

		public String getSecondArg() {
			return secondArg;
		}

		public void setSecondArg(String secondArg) {
			this.secondArg = secondArg;
		}

		public void setOverlap(int overlap) {
			this.overlap = overlap;
		}

		public String getMergedFragment() {
			return mergedFragment;
		}

		public void setMergedFragment(String mergedFragment) {
			this.mergedFragment = mergedFragment;
		}

	}
}