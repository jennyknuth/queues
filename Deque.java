/******************************************************************************
 *  Compilation:  javac Deque.java
 *  Execution:    java Deque < input.txt
 *  Dependencies: StdIn.java StdOut.java
 *  Data files:   https://algs4.cs.princeton.edu/13stacks/tobe.txt
 *
 *  Stack implementation with a resizing array.
 *
 *  % more tobe.txt
 *  to be or not to - be - - that - - - is
 *
 *  % java Deque < tobe.txt
 *  to be not that or be (2 left on stack)
 *
 *  % more hyphens.txt
 *  A B C D E - F - - G - - - H
 *
 *  % java Deque < hyphens.txt with addLast only
 *  E F D G C B
 *  A H
 *
 ******************************************************************************/

import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 *  The {@code Deque} class represents a tail-in-head-out (LIFO) deque
 *  of generic items.
 *  It supports <em>addFirst</em>, <em>addLast</em>, <em>removeFirst</em>, and <em>removeLast</em> operations, along with methods
 *  for testing if the deque is empty, and iterating through
 *  the items in FIFO order.
 *  <p>
 *  This implementation uses a resizing array, which doubles the underlying array
 *  when it is full and halves the underlying array when it is one-quarter full.
 *  The <em>addFirst</em> and <em>addLast</em> operations take constant amortized time.
 *  The <em>size</em> and <em>is-empty</em> operations take
 *  constant time in the worst case.
 *  <p>
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
public class Deque<Item> implements Iterable<Item> {
    private Item[] a;         // array of items
    private int head;        // index of head element in deque
    private int tail;         // index of tail element in deque

    /**
     * Initializes an empty stack.
     */
    public Deque() {
        a = (Item[]) new Object[2]; // the ugly cast
        head = 0;
        tail = 1;
    }

    /**
     * Is this stack empty?
     * @return true if this stack is empty; false otherwise
     */
    public boolean isEmpty() {
        return (tail - head) == 1;
    }

    /**
     * Returns the number of items in the stack.
     * @return the number of items in the stack
     */
    public int size() {
        return (tail - head) - 1;
    }


    // resize the underlying array holding the elements
    // also resets head to 0 and tail to size + 1
    private void resize(int capacity) {
        assert capacity >= size(); // use asserts if you are assuming this would never happen
        int previousCapacity = a.length;

        // textbook implementation
        Item[] temp = (Item[]) new Object[capacity]; // another cast
        int headPosition = (head % previousCapacity);
        for (int i = 0; i <= size(); i++) {
            temp[i] = a[headPosition % previousCapacity];
            headPosition++;
        }
        // a = null; // is this garbage collecting?
        a = temp;
        tail = size() + 1;
        head = 0;

       // alternative implementation
       // a = java.util.Arrays.copyOf(a, capacity);
    }

    /**
     * Adds the item to the beginning like a queue.
     * @param item the item to add
     */
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("addFirst must have an argument");
        if (size() == a.length - 1) resize(2 * a.length);    // double size of array if necessary
        a[head % a.length] = item;                  // add item
        head--;
        if (head < 0) {
          head += a.length;
          tail += a.length;
        }
    }

    /**
     * Adds the item to the end like a stack.
     * @param item the item to add
     * @throws java.lang.IllegalArgumentException if argument = null
     */
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("addLast must have an argument");
        if (size() == a.length - 1) resize(2 * a.length);    // double size of array if necessary
        a[tail % a.length] = item;                   // add item
        tail++;
    }

    /**
     * Removes and returns the item most recently added.
     * @return the item most recently added
     * @throws java.util.NoSuchElementException if this stack is empty
     */
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Item item = a[(tail - 1) % a.length];
        a[(tail - 1) % a.length] = null;                   // to avoid loitering
        tail--;
        // shrink size of array if necessary
        if (size() > 0 && size() == a.length/4) resize(a.length/2);
        return item;
    }

    /**
     * Returns an iterator to this stack that iterates through the items in FIFO order.
     * @return an iterator to this stack that iterates through the items in FIFO order.
     */
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator implements Iterator<Item> {
        private int i;

        public ArrayIterator() {
            i = 0;
        }

        public boolean hasNext() {
            return i < size();
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[i++];
        }
    }


    /**
     * Unit tests the {@code Stack} data type.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        Deque<String> stack = new Deque<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) stack.addFirst(item);
            else if (!stack.isEmpty()) StdOut.println("output is: " + stack.removeLast() + " ");
        }
        StdOut.println("(" + stack.size() + " left on stack)");
        // for (String s : stack)
        //    StdOut.print(" " + s);
        Iterator<String> iterate = stack.iterator();
        while (iterate.hasNext()) {
            StdOut.print(iterate.next()+ " ");
        }
        StdOut.println();
    }
}
