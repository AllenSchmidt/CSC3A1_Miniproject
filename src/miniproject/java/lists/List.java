package miniproject.java.lists;

public interface List<T>{
    int size();
    boolean isEmpty();
    T get(int i) throws IndexOutOfBoundsException;
    T set(int i, T t) throws IndexOutOfBoundsException;
    T remove(int i) throws IndexOutOfBoundsException;
    void add(int i, T t) throws IndexOutOfBoundsException;
}
