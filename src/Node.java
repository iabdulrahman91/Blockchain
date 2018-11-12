import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import static java.lang.Thread.sleep;

/*
This is the Main class for the project.
This class is responsible for setting up and starting new node
It manage the logic for servers to start.

It can do:
scan the network to see if there is network to join, otherwise it will establish new network
Hold all needed classes such as node trakers, block queue, ledger manager.
All classes can comunicate with each other by using Getter method from Node class (This class); so it works as middleMan class.

Setters are written but is commented out for this version.

/-------------------------------------------/

Connect Four group

2. Java version used
e.g. build 1.8.0_101-b13
On MacOS

---------------------------
3. Precise command-line compilation examples / instructions:

    > javac *.java

---------------------------

After Compiling files successfully:

    > java Node 0

---------------------------
0 is node id, you can add new nodes to the network by:
---------------------------

Open separate shell windows/ terminal:

    > java Node 1

Note: you can add more node by repeating the same previous step
--------------------------------------------------------

**** Notes ****

ports that will be used
- Node 0 :
    - 6000
    - 6010
    - 6020
    - 6030
    - 6040
- next Node will use same number + 1000. so:
- Node 1 :
    - 7000
    - 7010
    - 7020
    - 7030
    - 7040

----------------------------------------------------------

To access the Web page use:
for Node 0
http://localhost:6010/
for Node 1
http://localhost:7010/
for Node 2
http://localhost:8010/


-------------------------------------------
Note: some of the classes' ideas are taken from InetServer Assignment, and blockchain example.
https://condor.depaul.edu/elliott/435/hw/programs/Blockchain/program-block.html
However, there is major modification on how classes interact and how the blockchain is managed.
-------------------------------------------
 */
public class Node {
    // fields
    private static int PID;
    private static String serverName = "localhost";
    private static int complixity = 5;

    private static NodesTracker nodesTracker;
    private static LedgerMngr ledgerMngr;
    private static BlocksQueue blocksQueue;
    private static UnverifiedBlockMngr unverifiedBlockMngr;
    private static Cli cli;
    private static MultiCaster multiCaster;


    private static NewNodeServer newNodeServer;
    private static WebServer webServer;
    private static UpdateServer updateServer;
    private static BlockReceiver blockReceiver;
    private static DoWorkServer doWorkServer;



    public Node(int pid){
        PID = pid;
        nodesTracker = new NodesTracker();
        ledgerMngr = new LedgerMngr(this);
        blocksQueue = new BlocksQueue(this);
        unverifiedBlockMngr = new UnverifiedBlockMngr(this);
        cli = new Cli(this);
        multiCaster = new MultiCaster(this);

        newNodeServer = new NewNodeServer(this);
        webServer = new WebServer(this);
        updateServer = new UpdateServer(this);
        blockReceiver = new BlockReceiver(this);
        doWorkServer = new DoWorkServer(this);




    }
    public void nodeStart(){
        new Thread(newNodeServer).start();
        new Thread(webServer).start();
        new Thread(updateServer).start();
        new Thread(blockReceiver).start();
        new Thread(doWorkServer).start();
        new Thread(unverifiedBlockMngr).start();

        new Thread(cli).start();
    }

    public void setCli(Cli c){ this.cli = c; }
    public Cli getCli(){ return this.cli; }

    public int getPID(){ return this.PID; }
    public int getComplixity(){ return this.complixity; }
    public String getServerName(){ return this.serverName; }

    public BlocksQueue getBlocksQueue(){ return this.blocksQueue; }
//    public void setBlocksQueue(BlocksQueue blocksQueue){ this.blocksQueue = blocksQueue; }

    public LedgerMngr getLedgerMngr(){ return this.ledgerMngr; }
//    public void setLedgerMngr(LedgerMngr l){ this.ledgerMngr = l; }

//    public void setNewNodeServer(NewNodeServer newNodeServer){ this.newNodeServer = newNodeServer; }
    public NewNodeServer getNewNodeServer(){ return this.newNodeServer; }

//    public void setNodesTracker(NodesTracker nodesTracker){ this.nodesTracker = nodesTracker; }
    public NodesTracker getNodesTracker(){ return this.nodesTracker; }

    public BlockReceiver getBlockReceiver(){ return this.blockReceiver; }
//    public void setBlockReceiver(BlockReceiver blockReceiver){ this.blockReceiver = blockReceiver; }

    public void multiCast(){ new Thread(multiCaster).start(); }
    public void joinNetwork() {

        System.out.print("Looking for active nodes ");
        nodesTracker.addNode(PID);
        Socket sock = null;
        int randPort = 6000;
        boolean connected = false;
        int maxPort = 50000;
        nodesTracker.addNode(PID);

        while (randPort <= maxPort && !connected) {
            randPort = (randPort == (PID * 1000) + 6000) ? (((PID * 1000) + 6000) + 1000) : randPort;
            try {
                sock = new Socket(serverName, randPort);
                PrintStream toServer = new PrintStream(sock.getOutputStream());
                toServer.println(PID);
                toServer.flush();
                connected = true;


            } catch (IOException e) {
                System.out.print(":");
                randPort = randPort + 1000;
                try {
                    sleep(20);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        if (randPort > maxPort) {
            System.out.println("\nNo node is active right now Beside me (" + PID + ") !!");
        }


    }

    // setter and getter

    public static void main(String[] args){
        int pid = (args.length < 1) ? 0 : Integer.parseInt(args[0]);
        Node node = new Node(pid);


        node.nodeStart();
        node.joinNetwork();





    }


}
