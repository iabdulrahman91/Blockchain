import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/*
This class is responsible for listen for any update on the ledger
Once an update is made by another node, the updated ledger will be sent by the multicaster of the other node,
And this node will use this update server to receive the updated ledger.
Before it forward the received ledge to the ledger manager, it will check if ledger is actually valid or not.
 */
public class UpdateServer implements Runnable {
    Node node;

    public UpdateServer(Node node){
        this.node = node;
    }

    @Override
    public void run() {
        int q_len = 6;
        Socket sock;
        try {
            ServerSocket servsock = new ServerSocket(((node.getPID() * 1000) + 6040), q_len);
            while (true) {
                sock = servsock.accept();
                new UpdateWorker(sock).start();
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    class UpdateWorker extends Thread{
        Socket socket;
        Formatter formatter;

        public UpdateWorker(Socket s){
            this.socket = s;
            this.formatter = new Formatter();
        }

        @Override
        public void run(){
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String data = in.readLine();
                String data2;
                while ((data2 = in.readLine()) != null) {
                    data = data + data2;
                }


                socket.close();
                BlockLedger blockLedger = formatter.unMarshalLedger(data);

                // to ignore empty ledger
                if(blockLedger.getBlockRecord()!=null) {
                    node.getLedgerMngr().updateLedger(blockLedger);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    }
}
