import javax.xml.bind.JAXBException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/*
This class is responsible to sending work request from all node in the network.
So it will get next unverified block from the BlockQueue and ask all nodes to verify it;
it will send the unverified block to all active node by asking
the NodesTrack who is active.

---
This class needs:
- Formatter
- NodesTracker
- BlockQueue


 */
public class UnverifiedBlockMngr implements Runnable {
    Node node;
    Formatter formatter;

    public UnverifiedBlockMngr(Node node) {
        this.node = node;
        this.formatter = new Formatter();
    }

    @Override
    public void run() {
        // start BlockReceiver
        BlockRecord toSend = null;
        while (true) {
            List<Integer> toRemove = new ArrayList<Integer>();
            try {
                toSend = node.getBlocksQueue().getNextUnverified();
                String toSendString = formatter.marshalRecord(toSend);
                Socket sock;
                PrintStream toServer;

                node.getBlockReceiver().addExpected(toSend.getBlockID());
                System.out.println("Block ID : " + toSend.getBlockID());
                for (int i : node.getNodesTracker().getActiveNodes()) {
                    try {
                        sock = new Socket(node.getServerName(), ((i * 1000) + 6030));
                        toServer = new PrintStream(sock.getOutputStream());
                        toServer.println(toSendString);
                        toServer.flush();
                        sock.close();
                        System.out.println(" Sent for verification to Node: " + i);
                    } catch (Exception e) {
                        toRemove.add(i);
                    }
                }

            } catch (IndexOutOfBoundsException e) {
                try {
                    sleep(1000);
                }catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (JAXBException e) {
                e.printStackTrace();
            }
            for(int i : toRemove){
                node.getNodesTracker().removeNode(i);
            }

        }
    }
}