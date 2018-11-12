import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
This class is responsible for receiving a block after it gets verified
This class can:
- receive verified blocks from other nodes and itself' node
- expect what Block it might receive
- wait for the majority of nodes to verify a block before a verified block got added to the ledger.
- find the winning node that verified a block first.
- ignore blocks that receive after a winner is determined.
- notify the ledger manager about the blocks it received, and tell it what block should be added to the ledger.
 */
public class BlockReceiver implements Runnable {
    private static Node node;
    private static Map<String,List<BlockRecord>> expected;

    public BlockReceiver(Node node) {
        this.node = node;
        this.expected = new HashMap<String, List<BlockRecord>>();

    }


    public void addExpected(String blockID){
        this.expected.put(blockID, new ArrayList<BlockRecord>());
    }

    public static void majorityApproved(){
        int activeNodeSize = node.getNodesTracker().getActiveNodes().size();
        int half = (activeNodeSize%2==0)? (activeNodeSize/2) : (activeNodeSize/2)+1;
        for (String blockID : expected.keySet()){
            if(expected.get(blockID).size()>=half){
                List<BlockRecord> approved = expected.remove(blockID);
                checkWinner(approved);

            }
        }
    }
    public static void checkWinner(List<BlockRecord> blockRecords){
        BlockRecord winner = blockRecords.get(0);
        Date winnerDate = null;
        try {
             winnerDate = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").parse(winner.getTimeStampVerification());
             for (BlockRecord block : blockRecords){
                 Date blockDate = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").parse(block.getTimeStampVerification());
                 if(blockDate.before(winnerDate)) {winner = block;}
             }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        node.getLedgerMngr().addBlockRecord(winner);
        System.out.println("Block ID " + winner.getBlockID() + " got Verified");
        System.out.println("The winner: "+winner.getVerificationProcessID());
    }



    @Override
    public void run() {
        int q_len = 6;
        Socket sock;
        try {
            ServerSocket servsock = new ServerSocket(((node.getPID() * 1000) + 6020), q_len);
            while (true) {
                majorityApproved();
                sock = servsock.accept();
                new BlockReceiverWorker(sock).start();

            }

        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
    // organize verified block
    static class BlockReceiverWorker extends Thread {
        Socket sock;
        Formatter formatter;

        BlockReceiverWorker(Socket s) {
            this.sock = s;
            this.formatter = new Formatter();
        }

        @Override
        public void run() {

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                String data = in.readLine();
                String data2;
                while ((data2 = in.readLine()) != null) {
                    data = data + data2;
                }

                BlockRecord blockReceived = formatter.unMarshalRecord(data);
                if(expected.containsKey(blockReceived.getBlockID())){
                    expected.get(blockReceived.getBlockID()).add(blockReceived);
                    majorityApproved();
                } else {
                }
                sock.close();
            } catch (IOException x) {
                x.printStackTrace();
            } catch (JAXBException e) {
                e.printStackTrace();
            }

        }
    }
}