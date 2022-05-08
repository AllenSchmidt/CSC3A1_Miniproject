package miniproject.java;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Was running out of heap memory, so instead of display all article data
 * I am going to limit it to just a few articles
 */
public class ArticleData {
    private List<String> ListOfNames;
    private List<List<String>> ListOfCombinations;
    private String ArticleTitle;

    public ArticleData(String ArticleTitle, List<String> listOfNames, List<List<String>> listOfCombinations) {
        this.ListOfNames = removeDuplicate(listOfNames);
        this.ListOfCombinations = listOfCombinations;
        this.ArticleTitle = ArticleTitle;

        removeDuplicateCombinations();
    }

    /**
     * Return the list of Names
     * @return
     */
    public List<String> getListOfNames() {
        return this.ListOfNames;
    }

    /**
     * Set the list of Names
     * @param listOfNames
     */
    public void setListOfNames(List<String> listOfNames) {
        this.ListOfNames = listOfNames;
    }

    /**
     * Returns the list of Combinations
     * @return
     */
    public List<List<String>> getListOfCombinations() {
        return this.ListOfCombinations;
    }
    public String getArticleTitle() {
        return ArticleTitle;
    }

    /**
     * Set the list of the Combinations
     * @param listOfCombinations
     */
    public void setListOfCombinations(List<List<String>> listOfCombinations) {
        this.ListOfCombinations = listOfCombinations;
    }

    /**
     * Removes duplicates from a string through a HashSet
     * @param temp
     * @return
     */
    private List<String> removeDuplicate(List<String> temp){
        return new ArrayList<>(new HashSet<>(temp));
    }

    /**
     * This method is responsible for scanning through the list of combinations
     *  * This will be as follows: {[Sybrand, Sybrand]
     *  *                           [Sybrand, Juan]
     *  *                           [Sybrand, Adriaan]
     *  *                           [Juan, Juan]
     *  *                           [Juan, Sybrand]
     *  *                           [Juan, Adriaan]
     *  *                           [Adriaan, Adriaan]
     *  *                           [Adriaan, Sybrand]
     *  *                           [Adriaan, Juan]}
     *  Note that in the above example list
     *  [Sybrand, Juan] is the same as [Juan, Sybrand] so we dont it to be sent
     *  for display
     */
    private void removeDuplicateCombinations(){
        List<List<String>> temp = this.ListOfCombinations;

        int t = 0;
        for(int k=0; k < this.ListOfCombinations.size(); k++){
            String nameOne = this.ListOfCombinations.get(k).get(0);
            String nameTwo = this.ListOfCombinations.get(k).get(1);

            for(int i=1; i < this.ListOfCombinations.size()-1; i++){
                if((this.ListOfCombinations.get(i).get(0) == nameTwo)
                        && (this.ListOfCombinations.get(i).get(1) == nameOne)
                        || (this.ListOfCombinations.get(i).get(0) == this.ListOfCombinations.get(i).get(1))){
                    temp.remove(i);
                }
            }
        }
        setListOfCombinations(temp);
    }


}
