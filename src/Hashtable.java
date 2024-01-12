import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


class HashNode<K, V>{
    K key;
    V value;
    HashNode<K, V> next;

    public HashNode(K key, V value){
        this.key = key;
        this.value = value;
        next = null;


    }
}

public class Hashtable<K, V> {

    private List<HashNode<K, V>> bucketArray;
    private final int capacity;
    private int size;

    public Hashtable( int capacity){
        this.bucketArray = new ArrayList<>();
        this.capacity = capacity;


        for (int i = 0; i < capacity; i++){
            bucketArray.add(null);
        }
    }


    public int size() {
        return size;
    }
    public boolean isEmpty(){
        return size() == 0;
    }

    public int hash(K key){
        return Math.abs(Objects.hashCode(key) % capacity);
    }

    public V add (K key, V value){
        this.size++;
        V oldvalue = null;

        int position = hash(key);
        HashNode<K, V> currentNode = bucketArray.get(position), previousNode = null;

        while(currentNode != null && currentNode.key!=key) {
            previousNode = currentNode;
            currentNode = currentNode.next;
        }//finds previous node so we can link the new node

        if(currentNode!=null){
            oldvalue = currentNode.value;
            currentNode.value = value;
        }
        else if(previousNode!=null){
            previousNode.next = new HashNode<>(key, value);//adds the new node to the end of the list
        }
        else{
            bucketArray.set(position, new HashNode<>(key, value)); //if previous node doesn't exist make it the first in the chain
        }
        return oldvalue;

        //find the correct node with the key
//        while (currentNode != null && !Objects.equals(currentNode.key, key)){
//            //previousNode = currentNode;
//            currentNode = currentNode.next;
//        }


//        if (currentNode.key.equals(key)){
//            currentNode.value = value;
//            return;
//        }
//        while (currentNode != null){
//            previousNode = currentNode;
//            currentNode = currentNode.next;
//            }
//            if ( previousNode != null){
//                previousNode.next = new HashNode<>(key, value);
//            }
//            else {
//                bucketArray.add(position, new HashNode<>(key, value));
//            }
//
    }
    public V get(K key)
    {
        int position = hash(key);

        HashNode<K, V> head = bucketArray.get(position);

        // Search key in chain
        while (head != null && !head.key.equals(key)){
            head = head.next;
        }

        if (head != null){
            return head.value;
        }
        // If key not found
        return null;
    }

    public void remove(K key){
        this.size--;
        int position = hash(key);
        HashNode<K,V> currentNode = bucketArray.get(position), previousNode = null;
        while (currentNode != null && currentNode.key != key){
            previousNode = currentNode;
            currentNode = currentNode.next;
        }
                // Found the node to remove
            if (previousNode != null) {
                previousNode.next = currentNode!=null ? currentNode.next : null;
                    // If it's the first node in the list, update the bucketArray
                }
            else
                bucketArray.remove(position);
                // If it's not the first node, just remove it from the list


     }

    @Override
    public String toString() {
        String str = "";
        for (HashNode<K,V> entry : bucketArray){
            if(entry!=null)
                str += entry.key + " => " + entry.value + "\n";
        }
        return str;
    }

}
