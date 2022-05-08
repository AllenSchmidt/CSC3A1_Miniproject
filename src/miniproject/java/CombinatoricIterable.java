package miniproject.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * This class applies a concept in combinatorics, combinations
 * This is built off of the idea of combinations
 * So the number of combinations, i.e. selections where the order is not taken into account of r objects
 * out of n objects is
 *                      C(n, r) = P(n, r)/r! = n!/(n-r)!r!
 * In this implementation, we have n be the names of the people and r the size of each group * Names are combined in groups of twos
 * So with applying this to an example with a given set of names: Sybrand, Juan, Adriaan grouped in groups
 * of two.
 * This will be as follows: {[Sybrand, Sybrand]
 *                           [Sybrand, Juan]
 *                           [Sybrand, Adriaan]
 *                           [Juan, Juan]
 *                           [Juan, Sybrand]
 *                           [Juan, Adriaan]
 *                           [Adriaan, Adriaan]
 *                           [Adriaan, Sybrand]
 *                           [Adriaan, Juan]}
 * @param <T>
 */
public class CombinatoricIterable<T> implements Iterable<List<T>>{

    private List<T> list;
    //Size represents the choices we need to make per group in the combination
    private int size, numberofElements;

    /**
     * Constructor method
     * @param list The input list that the combinations will be made from
     * @param size The size of the choices per group
     */
    public CombinatoricIterable(List<T> list, int size){
        this.size = size;
        this.list = list;
        numberofElements = (int) Math.pow(list.size(), size);
    }

    @Override
    public Iterator<List<T>> iterator() {
        return new Iterator<List<T>>() {

            //Keep track of the elements
            private int count = 0;
            //Indexes of the elements that we work with
            private final int index[] = new int[size];

            @Override
            public boolean hasNext() {
                return count < numberofElements;
            }

            /**
             * Increase the choices of the selections of the elements by one
             */
            private void increase(){
                int i = this.index.length - 1;
                while(i >= 0){
                    if(this.index[i] < list.size() -1){
                        this.index[i]++;
                        return;
                    }
                    this.index[i] = 0;
                    i--;
                }
            }

            @Override
            public List<T> next() {
                if(!hasNext()){
                    throw new NoSuchElementException("There are no more elements");
                }
                List<T> combinatoric = new ArrayList<>(size);
                for(int i = 0; i<size; i++){
                    combinatoric.add(list.get(index[i]));
                }
                increase();
                count++;
                return combinatoric;
            }
        };
    }
}
