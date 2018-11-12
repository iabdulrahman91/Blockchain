import java.util.ArrayList;
import java.util.List;

/*
This class is responsible for tracking what node is active and who is not.

In the future:
improve this class so it become multi-thread safe.
This class consider critical section for casting changes.

 */
public class NodesTracker {
    private List<Integer> activeNodes;

    public  NodesTracker(){
        this.activeNodes = new ArrayList<Integer>();
    }

    public  void addNode(int i){
        if(!activeNodes.contains(i)){ activeNodes.add(i); }
    }
    public   void removeNode(int i){
        if(activeNodes.contains(i)){ activeNodes.remove(i);}
    }
    public  List<Integer> getActiveNodes(){
        return activeNodes;
    }
}
