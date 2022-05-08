package miniproject.java.lists;

import static java.util.Objects.checkIndex;

public class ArrayList<T> implements List<T>{

    private int CAPACITY;
    private int size = 0;
    private T[] data;

    public ArrayList() {
        this.CAPACITY = 10000;
        data = (T[]) new Object[this.CAPACITY];
    }

    public ArrayList(int capacity){
        this.CAPACITY = capacity;
        data = (T[]) new Object[this.CAPACITY];
    }

    @Override
    public int size() { return size; }

    @Override
    public boolean isEmpty() { return size == 0; }

    @Override
    public T get(int i) throws IndexOutOfBoundsException {
        checkIndex(i, this.size);
        return data[i];
    }

    @Override
    public T set(int i, T t) throws IndexOutOfBoundsException {
        checkIndex(i, this.size);
        T temp = data[i];
        data[i] = t;
        return temp;
    }

    @Override
    public T remove(int i) throws IndexOutOfBoundsException {
        checkIndex(i, this.size);
        T temp =  data[i];
        for(int k=i; k < this.size-1; k++){ data[k] = data[k+1];}
        data[this.size -1] = null;
        this.size--;
        return null;
    }

    @Override
    public void add(int i, T t) throws IndexOutOfBoundsException {
        checkIndex(i, this.size+1);
        if(this.size == data.length){ CAPACITY = CAPACITY*2; }
        for(int k=i; k< this.size -1; k++){
            data[k+1] = data[k];
        }
        data[i] = t;
        size++;
    }
}
