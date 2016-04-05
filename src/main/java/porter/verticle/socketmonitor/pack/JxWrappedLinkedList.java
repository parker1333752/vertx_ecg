package porter.verticle.socketmonitor.pack;

import java.util.LinkedList;

/**
 * Created by parker on 2015/11/11.
 */
public class JxWrappedLinkedList<T> {
    private LinkedList<T> list;

    public JxWrappedLinkedList(LinkedList<T> list){
        this.list = list;
    }

    public int size(){
        return list.size();
    }

    public T getAndRemoveFirst(){
        T temp = list.getFirst();
        list.removeFirst();
        return temp;
    }
}
