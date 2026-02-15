/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        // Your code goes here
        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        CharData newFirst = new CharData(chr);
        this.first = new Node(newFirst, this.first);
        this.size++;
        
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
        Node current = first;
        String listString = "(";
        while (current != null) {
            CharData d = current.cp;
            listString += ("(" + d.chr + " " + d.count + " " + d.p + " " + d.cp + ")");
            current = current.next;
        }
        return listString;
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
    Node current = first; 
    int index = 0;

    while (current != null) {
        // Use the equals method defined in CharData.java
        if (current.cp.equals(chr)) { 
            return index;
        }
        current = current.next; // Move to the next Node
        index++;
    }
    return -1; // Character not found
}

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        int index = indexOf(chr);
        if (index == -1) {
            addFirst(chr);
        } else {
            get(index).count++;
        }
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        int index = indexOf(chr);
        if (index == 0) {
            this.first = this.first.next;
            this.size--;
        } else if (index >= 1) {
            Node current = first;
            for (int i = 0; i < index-1; i++) {
                current = current.next;
            }

            current.next = null;
            this.size--;

            return true;
        } 
        

        return false;
    }

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
   public CharData get(int index) {
    if (index < 0 || index >= size) {
        throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    Node current = first;
    for (int i = 0; i < index; i++) {
        current = current.next; 
    }

    return current.cp;
}

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public ListIterator listIterator(int index) {
	    // If the list is empty, there is nothing to iterate   
	    if (size == 0) return null;
	    // Gets the element in position index of this list
	    Node current = first;
	    int i = 0;
        while (i < index) {
            current = current.next;
            i++;
        }
        // Returns an iterator that starts in that element
	    return new ListIterator(current);
    }


// public static void main(String[] args) {
//     // 1. Initialize the list
//     List myList = new List();
//     System.out.println("Testing addFirst and getSize:");

//     // 2. Test addFirst
//     myList.addFirst('a');
//     myList.addFirst('b');
//     myList.addFirst('c');
//     // Expected order in list: (c) -> (b) -> (a)
//     System.out.println("Size should be 3: " + myList.getSize());

//     // 3. Test getFirst
//     System.out.println("First element should be c: " + myList.getFirst());

//     // 4. Test indexOf
//     System.out.println("Index of 'b' should be 1: " + myList.indexOf('b'));
//     System.out.println("Index of 'z' should be -1: " + myList.indexOf('z'));

//     // 5. Test get(index)
//     try {
//         System.out.println("Element at index 2 should be a: " + myList.get(2));
//         // This should throw an exception
//         myList.get(10); 
//     } catch (IndexOutOfBoundsException e) {
//         System.out.println("Successfully caught out of bounds error.");
//     }

//     // 6. Test remove
//     System.out.println("Removing 'b'...");
//     boolean removed = myList.remove('b');
//     System.out.println("Removed? " + removed + " | New size should be 2: " + myList.getSize());

//     // 7. Test iterator (The walk to the end)
//     System.out.println("Iterating from index 0:");
//     ListIterator it = myList.listIterator(0);
//     while (it != null && it.hasNext()) {
//         System.out.println("Node data: " + it.next());
//     }
// }
}