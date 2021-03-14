import java.util.*;

public class HashListAutocomplete implements Autocompletor {
    private static final int MAX_PREFIX = 10;
    private Map<String, List<Term>> myMap = new HashMap<>();
    private int mySize;

    /**
     * Constructs the HashListAutocomplete using the initialize method
     * @param terms array of Strings for words in each Term
     * @param weights corresponding weight for each word in each Term
     * @throws NullPointerException if one of the arguments is null
     * @throws IllegalArgumentException if the there are different number of terms and weights
     */
    public HashListAutocomplete(String[] terms, double[] weights){
        if (terms == null || weights == null) {
            throw new NullPointerException("One or more arguments null");
        }
        if (terms.length != weights.length) {
            throw new IllegalArgumentException("terms and weights are not the same length");
        }
        initialize(terms,weights);
    }

    /**
     * Returns a list of top matches using the myMap initialized, based on weights
     * @param prefix word the user wants to type
     * @param k number of letters the term should compare
     * @return a list of terms that have the highest weightage given the prefix provided
     */
    @Override
    public List<Term> topMatches(String prefix, int k) {
        List<Term> all = myMap.get(prefix);
        if (all == null) return new ArrayList<>();
        return all.subList(0,Math.min(k,all.size()));
    }

    /**
     * Initialize a HashMap to store all possible prefixes up to 10 characters long and the terms associated with each prefix
     * @param terms array of Strings for words in each Term
     * @param weights corresponding weight for each word in each Term
     */
    @Override
    public void initialize(String[] terms, double[] weights) {
        myMap = new HashMap<>();
        for(int k = 0; k < terms.length; k++){
            Term t = new Term(terms[k], weights[k]);
            for(int i = 0; i <= Math.min(MAX_PREFIX, terms[k].length()); i++){
                String prefix = terms[k].substring(0, i);
                if(!myMap.containsKey(prefix)){
                    myMap.put(prefix, new ArrayList<>());
                }
                myMap.get(prefix).add(t);
            }
        }

        Comparator comp = Comparator.comparing(Term::getWeight).reversed();
        for(String key: myMap.keySet()){
            List<Term> list = myMap.get(key);
            Collections.sort(list, comp);
        }
    }

    /**
     * Calculates the memory storage in bytes used by the autocompletor
     * @return the memory storage based on the number of keys and terms
     */
    @Override
    public int sizeInBytes() {
        if (mySize == 0) {
            for(String prefix : myMap.keySet()) {
                mySize += prefix.length()*BYTES_PER_CHAR;
                for(Term t :myMap.get(prefix)){
                    mySize += BYTES_PER_DOUBLE +BYTES_PER_CHAR*t.getWord().length();
                }
            }
        }
        return mySize;
    }
}
