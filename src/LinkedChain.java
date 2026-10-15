/**
 * A generic, unordered collection built from a singly linked chain of nodes.
 * Duplicates are allowed and null is never stored. The chain only checks
 * equality (equals()), it never orders or compares its elements, and it
 * contains no game logic.
 *
 * @param <T>
 *            the type of element stored
 */
public class LinkedChain<T>
{
    private Node<T> head;
    private int numberOfEntries;

    /**
     * Creates an empty chain. O(1).
     */
    public LinkedChain()
    {
        head = null;
        numberOfEntries = 0;
    }


    /**
     * Adds an item at the front of the chain. O(1).
     *
     * @param item
     *            the item to add
     * @throws IllegalArgumentException
     *             if item is null
     */
    public void add(T item)
    {
        if (item == null)
        {
            throw new IllegalArgumentException("Cannot add null to the chain");
        }

        head = new Node<T>(item, head);
        numberOfEntries++;
    }


    /**
     * Removes the first instance of item, if present. O(n).
     *
     * @param item
     *            the item to remove
     * @return true if an item was removed, false if it was not found
     */
    public boolean remove(T item)
    {
        Node<T> previous = null;
        Node<T> current = head;

        while (current != null)
        {
            if (current.data.equals(item))
            {
                if (previous == null)
                {
                    head = current.next;
                }
                else
                {
                    previous.next = current.next;
                }
                numberOfEntries--;
                return true;
            }
            previous = current;
            current = current.next;
        }
        return false;
    }


    /**
     * Tells whether the chain holds the item. O(n).
     *
     * @param item
     *            the item to look for
     * @return true if found, false otherwise
     */
    public boolean contains(T item)
    {
        return count(item) > 0;
    }


    /**
     * Counts how many times the item appears. O(n).
     *
     * @param item
     *            the item to count
     * @return the number of occurrences, 0 if none
     */
    public int count(T item)
    {
        int total = 0;
        Node<T> current = head;

        while (current != null)
        {
            if (current.data.equals(item))
            {
                total++;
            }
            current = current.next;
        }
        return total;
    }


    /**
     * Gets the item at an index, where 0 is the front. O(n).
     *
     * @param index
     *            the position of the item
     * @return the item at that position
     * @throws IndexOutOfBoundsException
     *             if the index is invalid
     */
    public T get(int index)
    {
        if (index < 0 || index >= numberOfEntries)
        {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }

        Node<T> current = head;
        for (int i = 0; i < index; i++)
        {
            current = current.next;
        }
        return current.data;
    }


    /**
     * Returns the number of elements. O(1).
     *
     * @return the size of the chain
     */
    public int size()
    {
        return numberOfEntries;
    }


    /**
     * Tells whether the chain is empty. O(1).
     *
     * @return true if the chain has no elements
     */
    public boolean isEmpty()
    {
        return numberOfEntries == 0;
    }

    // One element and a reference to the next node
    private static class Node<T>
    {
        private T data;
        private Node<T> next;

        private Node(T data, Node<T> next)
        {
            this.data = data;
            this.next = next;
        }
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        Node<T> current = head; 

        while (current != null) {
            result.append(current.data);
            if (current.next != null) {
                result.append(" -> ");
            }
            current = current.next;
        }
        
        result.append("]");
        return result.toString();
    }
}
