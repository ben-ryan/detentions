package fragment.submissions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BenedictRyan {

  /**
   * 
   * Expects a single parameter which is the absolute path to a text file 
   * containing a number of fragment problems, each on a separate line.
   * <p>
   * The fragments within each problem are separated by semi-colons.
   * <p>
   * For each problem, this program determines the original text from
   * which the fragments were produced and prints this to the console.
   * <p>
   * @param args
   */
  public static void main(String[] args) { 
    try (BufferedReader in = new BufferedReader(new FileReader(args[0]))) { 
      String fragmentProblem; 
      while ((fragmentProblem = in.readLine()) != null) { 
          System.out.println(reassemble(fragmentProblem)); 
       } 
      } catch (Exception e) { 
        e.printStackTrace(); 
      } 
  }

  /**
   * Reassemble the given fragment problem. 
   * 
   * This creates a list of all possible fragment pairings and determines 
   * the overlap between each pair, then orders them by overlap size,
   * descending.
   * 
   * Beginning the with pair with the largest overlap, the pairs are merged
   * one by one to produce a single fragment, and following their merging every
   * instance of either of the fragments which were merged in the remaining
   * pairs is replaced with the merged text.
   * 
   * By the time the end of the list is reached, the final fragment ought to,
   * once merged, produce the reassembled text.
   * 
   * @param fragmentProblem 	
   * 			- the semicolon-separated list of fragments to reassemble
   * @return a single line containing the reassembled text
   */
  private static String reassemble(String fragmentProblem) {

    String[] fragmentsArray = fragmentProblem.split(";");
    List<String> fragments = new ArrayList<String>(Arrays.asList(fragmentsArray));
    
    if(fragments.size() == 1) {
		return fragments.get(0);
    }
    
    List<FragmentPair> overlapList = getAllPairings(fragments);
    Comparator<FragmentPair> comparator = Collections.reverseOrder();
    Collections.sort(overlapList, comparator);
    ArrayList<FragmentPair> innerFragments = new ArrayList<FragmentPair>(overlapList);
    
    for (FragmentPair fragmentPair : overlapList) {
    	innerFragments.remove(fragmentPair);
    	replaceWithMergedFragment(innerFragments, fragmentPair);
    }

    return overlapList.get(overlapList.size() - 1).getMergedFragment();
  }

  /**
   * Generates a list of all possible pairings from the given list of
   * fragments, and the overlap between each pair. 
   * <p>
   * The order of the pair is ignored - ie. {@code <A,B> = <B,A>} for the 
   * purposes of this list. 
   * <p>
   * Hence, there should be {@code (n!/2)} entries in the resultant 
   * list for a parameter with {@code n} fragments.
   * 
   * @param fragments
   * 		- the list of all fragments in the problem to be reassembled
   * @return the list of all possible pairings
   */
	private static List<FragmentPair> getAllPairings(List<String> fragments) {
	
		List<FragmentPair> overlapList = new ArrayList<>();
		ArrayList<String> innerFragments = new ArrayList<>(fragments);
		  
	    for(String fragment : fragments) {      
	      innerFragments.remove(fragment);
	      // Pair this fragment up with all others in the list
	      for(String innerFragment : innerFragments) {
	        FragmentPair newOverlapPair = findOverlap(fragment, innerFragment);
	        // Don't allow duplicates
	        if(!overlapList.contains(newOverlapPair)) {
	        	overlapList.add(newOverlapPair);
	        }
	      }
	      innerFragments.add(fragment);     
	    }
		return overlapList;
	}
	
	/**
	 * For each FragmentPair in the given list, if either fragment matches one of the fragments in
	 * the pair which have just been merged, replace that fragment with the merged fragment and 
	 * re-calculate the overlap and resultant merged text for this pair.
	 * 
	 * @param overlapList
	 * @param mergedFragmentPair
	 */
	private static void replaceWithMergedFragment(List<FragmentPair> overlapList, FragmentPair mergedFragmentPair) {

	   for(FragmentPair innerPair : overlapList) {
		  if(innerPair.getFirstArg().equals(mergedFragmentPair.getFirstArg())
		  || innerPair.getFirstArg().equals(mergedFragmentPair.getSecondArg())) {
			  
			    innerPair.setFirstArg(mergedFragmentPair.getMergedFragment());
			    FragmentPair revisedPair = findOverlap(innerPair.getFirstArg(), innerPair.getSecondArg());
			    innerPair.setOverlap(revisedPair.getOverlap());
			    innerPair.setMergedFragment(revisedPair.getMergedFragment());
		  }
	
		  if (innerPair.getSecondArg().equals(mergedFragmentPair.getFirstArg()) 
		   || innerPair.getSecondArg().equals(mergedFragmentPair.getSecondArg())) {
			  
			    innerPair.setSecondArg(mergedFragmentPair.getMergedFragment());
			    FragmentPair revisedPair = findOverlap(innerPair.getFirstArg(), innerPair.getSecondArg());
			    innerPair.setOverlap(revisedPair.getOverlap());
			    innerPair.setMergedFragment(revisedPair.getMergedFragment());	  
		  }
 	   }
    }

  /**
   * Given two text fragments, this will determine the length of the overlap
   * between them, and the resultant merged fragment.
   * <p>
   * The order of the two fragment parameters does not matter; the overlap will
   * be the same either way.
   * <p>
   * The three possible ways to overlap are:
   * <ul>
   * <li> one fragment fully contains the other,
   * <li> the end of the first fragment overlaps with the beginning of the second, or
   * <li> the end of the second fragment overlaps with the beginning of the first.
   * </ul>
   * These are each analysed, and the method producing the greatest overlap is used 
   * to merge the fragments.
   * <p>
   * Where two or more of these methods produce the same overlap, the method to be 
   * used is chosen according to the order in which they are listed above. 
   * <p>
   * For example,
   * <blockquote><pre>
   * "ABCD" and "DEF" have overlap of 1 and merge to make "ABCDEF"
   * "BCDEF" and "ABCD" have overlap of 3 and merge to make "ABCDEF"
   * "ABCDEF" and "BCDE" have overlap of 4 and merge to make "ABCDEF"
   * "ABCD" and "CDXYZABC" have overlap of 3 and merge to make "CDXYZABCD"
   * "ABCD" and "BCDXYZABC" have overlap of 3 and merge to make "ABCDXYZABC"
   * "ABCD" and "ABCE" have no overlap and cannot merge (returning an empty String)
   * </pre></blockquote>
   *  
   * @param fragment
   * @param innerFragment
   * @return overlap
   */
  private static FragmentPair findOverlap(String fragment, String innerFragment) {

    FragmentPair fragmentPair = new FragmentPair();
    fragmentPair.setFirstArg(fragment);
    fragmentPair.setSecondArg(innerFragment);
    
    String largerFragment = innerFragment;
    String smallerFragment = fragment;
    if(fragment.length() > innerFragment.length()) {
      largerFragment = fragment;
      smallerFragment = innerFragment;
    } 
    
    int maxPossOverlap = smallerFragment.length();

    // First possibility: one fragment fully contains the other
    if(largerFragment.contains(smallerFragment)) {
      fragmentPair.setOverlap(maxPossOverlap);
      fragmentPair.setMergedFragment(largerFragment);
    }
      
    // Second possibility: the end of the first fragment overlaps with the beginning of the second
    for(int i = maxPossOverlap; i > 0; i--) {
      if(fragment.regionMatches(fragment.length() - i, innerFragment, 0, i)) {
    	  if(i > fragmentPair.getOverlap()) {
    		  fragmentPair.setOverlap(i);
    	      fragmentPair.setMergedFragment(fragment.concat(innerFragment.substring(i)));
    	      break;
    	  }
       } 
    }
    
    // Third possibility: the end of the second fragment overlaps with the beginning of the first
    for(int i = maxPossOverlap; i > 0; i--) {
      if(innerFragment.regionMatches(innerFragment.length() - i, fragment, 0, i)) {
  	    if(i > fragmentPair.getOverlap()) {
	        fragmentPair.setOverlap(i);
	        fragmentPair.setMergedFragment(innerFragment.concat(fragment.substring(i)));
	        break;
  	    }
      } 
    }
    return fragmentPair;
  }
  
  /**
   * Represents a pair of fragments and data about their relationship.
   *
   */
  public static class FragmentPair implements Comparable<FragmentPair> {

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

      public FragmentPair(String firstArg, String secondArg, String mergedFragment, int overlap) {
        this.setFirstArg(firstArg);
        this.setSecondArg(secondArg);
        this.setMergedFragment(mergedFragment);
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

	  @Override
	  public int compareTo(FragmentPair fragmentPair) {
		if(this.getOverlap() > fragmentPair.getOverlap()) {
			return 1;
		} else if(this.getOverlap() < fragmentPair.getOverlap()) {
			return -1;
		} else {
			return 0;
		}
      }
	
	  /**
	   * Two FragmentPair objects are considered equal if the pair of fragments
	   * are the same in both objects, regardless of which is defined as the "First" 
	   * or "Second" arguments. 
       * <p>
	   * ie. {@code <A,B> = <A,B> = <B,A> != <A,A>}
	   */
	  @Override
	  public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FragmentPair other = (FragmentPair) obj;
		
		if(obj instanceof FragmentPair) {
			other = (FragmentPair) obj;
		} else {
			return false;
		}
		
		if(this.getFirstArg().equals(other.getFirstArg())
		&& this.getSecondArg().equals(other.getSecondArg())) {
			return true;
		}

		if(this.getFirstArg().equals(other.getSecondArg())
		&& this.getSecondArg().equals(other.getFirstArg())) {
			return true;
		}	
		return false;
      } 
  }
}