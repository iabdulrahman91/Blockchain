import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/*
This class is responsible to do the work and verify the block that receive from all nodes
This class accept multiple request so it async server.
This class is the one solving the puzzle by guessing a random seed.

 */
public class DoWorkServer implements Runnable {
    private static Node node;

    public DoWorkServer(Node node) {
        this.node = node;
    }


    @Override
    public void run() {
        int q_len = 6;
        Socket sock;
//            System.out.println("Starting verification server for PID: " + PID + " port: " + ((PID * 1000) + 6030));
        try {
            ServerSocket servsock = new ServerSocket(((node.getPID() * 1000) + 6030), q_len);
            while (true) {
                sock = servsock.accept();
                new DoWorkWorker(sock).start();
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    // verify block and send it back to creator with id
    static class DoWorkWorker extends Thread {
        Socket sock;
        Formatter formatter;
        Hash hash;
        DoWorkWorker(Socket s) {
            this.sock = s;
            this.formatter = new Formatter();
            this.hash = new Hash();
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
                sock.close();



                BlockRecord unVerifiedBlock = formatter.unMarshalRecord(data);

                System.out.println("Verifying Block ...");

                String SHA256 = unVerifiedBlock.getSHA256String();
                String pre = unVerifiedBlock.getPreviousHash();
                String hashData = unVerifiedBlock.getDataHash();

                //
                int numOfChar = 64 - node.getComplixity();
                String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                String result = "";
                String randSeed = "";

                do {
                    randSeed = "";
                    for (int x = 0; x < 64; x++) {
                        int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
                        randSeed = randSeed + (ALPHA_NUMERIC_STRING.charAt(character));
                    }

                    result = hash.doHash((pre + hashData + randSeed));

                } while (!SHA256.substring(numOfChar).equals(result.substring(numOfChar)));

                unVerifiedBlock.setSeed(randSeed);
                unVerifiedBlock.setTimeStampVerification(String.format("%1$s %2$tF.%2$tT", "", new Date()));
                unVerifiedBlock.setVerificationProcessID(node.getPID() + "");
                unVerifiedBlock.setSignedSHA256("#" + result);


                String fullBlock = formatter.marshalRecord(unVerifiedBlock);

                // send back

                int pid = Integer.parseInt(unVerifiedBlock.getCreatingProcessID());

                try {
                    sock = new Socket(node.getServerName(), ((pid * 1000) + 6020));
                    PrintStream toServer = new PrintStream(sock.getOutputStream());
                    toServer.println(fullBlock);
                    toServer.flush();
                    sock.close();
                    System.out.println("Sent back to: "+pid);
                } catch (IOException x) {
                    // if there is an error, print it
                    node.getNodesTracker().removeNode(pid);
                }


            }  catch (IOException e) {
                e.printStackTrace();
            } catch (JAXBException e) {
                e.printStackTrace();
            }

        }
    }

}
