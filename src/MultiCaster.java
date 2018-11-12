import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.List;

/*
This class is responsible for multiCasting ledger updates.
This class will be used when:
- new node join the network, to it give the new node updated copy of the ledger (to solve ledger synchronization issue).
- an new verified block got added to the ledger.

During the multiCasing process, nodes that left the network will be removed from the active nodes list.
 */
public class MultiCaster extends Thread {
    Node node;
    Formatter formatter;

    public MultiCaster(Node node) {
        this.node = node;
        this.formatter = new Formatter();
    }

    @Override
    public void run() {
        List<Integer> activeNodes = node.getNodesTracker().getActiveNodes();
        try {
            String ledgerString = formatter.marshalLedger(node.getLedgerMngr().getBlockLedger());
            for (int node : activeNodes) {
                try {
                    Socket sock = new Socket(this.node.getServerName(), ((node * 1000) + 6040));
                    PrintStream toServer = new PrintStream(sock.getOutputStream());
                    toServer.println(ledgerString);
                    toServer.flush();
                } catch (IOException e) {
                    this.node.getNodesTracker().removeNode(node);
                }
            }
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
