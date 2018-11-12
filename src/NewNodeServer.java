import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/*
This class is responsible for receiving join network request.
It is async server that keep listen for any join network request.
it is helpful to have one network.
It is also responsible for notifying other active nodes about the new node.

// get new node id
    // if !exist
        // add
        // tell everyone about the new node
    // else ignore
 */

public class NewNodeServer implements Runnable {
    Node node;

    public NewNodeServer(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        int q_len = 6;
        Socket sock;
        try {
            ServerSocket servsock = new ServerSocket(((node.getPID() * 1000) + 6000), q_len);
            while (true) {
                sock = servsock.accept();
                new NewNodeWorker(sock).start();
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    class NewNodeWorker extends Thread {
        Socket sock;

        NewNodeWorker(Socket s) {
            sock = s;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                String data = in.readLine();
                int id = Integer.parseInt(data);
                sock.close();
                if (!node.getNodesTracker().getActiveNodes().contains(id)) {
                    node.getNodesTracker().addNode(id);
                    Socket sock = null;
                    PrintStream toServer = null;
                    try {
                        sock = new Socket(node.getServerName(), ((id * 1000) + 6000));
                        toServer = new PrintStream(sock.getOutputStream());
                        toServer.println(node.getPID());
                        toServer.flush();
                        sock.close();
                    } catch (IOException e) {
                        System.out.println("node is not active anymore");
                    }

                    List<Integer> nodes = new ArrayList<>(node.getNodesTracker().getActiveNodes());
                    for (int receiver : nodes) {
                        if (receiver != node.getPID()) {
                            try {
                                sock = new Socket(node.getServerName(), ((receiver * 1000) + 6000));
                                toServer = new PrintStream(sock.getOutputStream());
                                toServer.println(id);
                                toServer.flush();
                                sock.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                    node.multiCast();
                }
            } catch (IOException x) {
                System.out.println("error in reading the new node msg");
            } catch (ConcurrentModificationException e) {
                System.out.println("error about Concurrent.");
            }
        }
    }
}
