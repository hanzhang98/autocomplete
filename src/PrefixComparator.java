import java.util.Comparator;

/**
 * Factor pattern for obtaining PrefixComparator objects
 * without calling new. Users simply use
 *
 *     Comparator<Term> comp = PrefixComparator.getComparator(size)
 *
 * @author owen astrachan
 * @date October 8, 2020
 */
public class PrefixComparator implements Comparator<Term> {

    private int myPrefixSize; // size of prefix

    /**
     * private constructor, called by getComparator
     * @param prefix is prefix used in compare method
     */
    private PrefixComparator(int prefix) {
        myPrefixSize = prefix;
    }


    /**
     * Factory method to return a PrefixComparator object
     * @param prefix is the size of the prefix to compare with
     * @return PrefixComparator that uses prefix
     */
    public static PrefixComparator getComparator(int prefix) {
       return new PrefixComparator(prefix);
    }


    @Override
    public int compare(Term v, Term w) {
        String v_str = v.getWord();
        String w_str = w.getWord();
        int min_char = Math.min(v_str.length(), w_str.length());
        int to_compare = Math.min(myPrefixSize, min_char);
//        if (v_str.length() < myPrefixSize || w_str.length() < myPrefixSize) {
//            if (v_str.length() <= w_str.length()) {
//                to_compare = v_str.length();
//            } else {
//                to_compare = w_str.length();
//            }
//        }

        for (int i = 0; i < to_compare; i++) {
            char v_char = v_str.charAt(i);
            char w_char = w_str.charAt(i);
            if (v_char - w_char != 0) {
                return v_char - w_char;
            }
        }
        if (to_compare == myPrefixSize) {return 0;}
        if (v_str.length() == w_str.length()) return 0;
        if (v_str.length() < w_str.length()) return -1;
        else {return 1;}
        }
    }
