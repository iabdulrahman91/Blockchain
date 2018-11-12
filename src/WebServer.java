import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;

/*
This class is responsible to run as web server to add GUI functionality.
It can be used for the following purposes:
- adding new medical entry
- displaying ledger (list of verified blocks)
- search the ledger for specific searching criteria
- display active nodes on the blockchain network

it can do:
- handle POST request from browser for adding new block
- handle GET request from browser to viewing the ledger
- manage the flow of webpages the should be displayed to the user.
 */
public class WebServer implements Runnable{
    Node node;
    public WebServer(Node node) {
        this.node = node;
    }


    @Override
    public void run() {
        int q_len = 6;
        Socket sock;
        System.out.println("\nStarting WebServer for PID: " + node.getPID() + "" +
                " http://localhost:" + ((node.getPID() * 1000) + 6010)+"/");
        try {
            ServerSocket servsock = new ServerSocket(((node.getPID() * 1000) + 6010), q_len);
            while (true) {
                sock = servsock.accept();

                BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                OutputStream out = new BufferedOutputStream(sock.getOutputStream());
                PrintStream pout = new PrintStream(out);

                HTMLGenerator generator = new HTMLGenerator();
                String request = in.readLine();
                if (request == null)
                    continue;

                String misc;
                while (true) {
                    misc = in.readLine();
                    if (misc == null || misc.length() == 0)
                        break;
                }


                if (!request.startsWith("GET") || request.length() < 14
                        || !(request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1"))) {

                    if (request.startsWith("POST")
                            || !(request.endsWith("HTTP/1.0") || request.endsWith("HTTP/1.1"))) {

                        if (request.indexOf("newEntry") > 0) {

                            String msg;
                            msg = in.readLine();
                            String fName = msg.substring(msg.indexOf("=") + 1);
                            msg = in.readLine();
                            String lName = msg.substring(msg.indexOf("=") + 1);
                            msg = in.readLine();
                            String ssn = msg.substring(msg.indexOf("=") + 1);
                            msg = in.readLine();
                            String provider = msg.substring(msg.indexOf("=") + 1);
                            msg = in.readLine();
                            String dob = msg.substring(msg.indexOf("=") + 1);
                            msg = in.readLine();
                            String diag = msg.substring(msg.indexOf("=") + 1);
                            msg = in.readLine();
                            String treat = msg.substring(msg.indexOf("=") + 1);
                            msg = in.readLine();
                            String rx = msg.substring(msg.indexOf("=") + 1);
                            msg = in.readLine();
                            String note = msg.substring(msg.indexOf("=") + 1);

                            node.getBlocksQueue().addBlock(dob, fName, lName, ssn, provider, diag, rx, treat, note);
                            pout.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + "text/html" + "\r\n"
                                    + "Date: " + new Date() + "\r\n" + "Server: WebServer 1.0\r\n\r\n");
                            pout.print(generator.getConformationPage());
                        }


                    } else {

                        pout.print("HTTP/1.0 " + "400" + " " + "Bad Request" + "\r\n" + "\r\n"
                                + "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" + "<TITLE>" + "400" + " " + "Bad Request"
                                + "</TITLE>\r\n" + "</HEAD><BODY>\r\n" + "<H1>" + "Bad Request" + "</H1>\r\n" + "Your browser sent a request that " +
                                "this server could not understand." + "<P>\r\n"
                                + "<HR><ADDRESS>FileServer 1.0 at " + sock.getLocalAddress().getHostName() + " Port "
                                + sock.getLocalPort() + "</ADDRESS>\r\n" + "</BODY></HTML>\r\n");
                    }
                } else { //  GET request

                    if (request.indexOf("/home") > 0) {
                        pout.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + "text/html" + "\r\n"
                                + "Date: " + new Date() + "\r\n" + "Server: WebServer 1.0\r\n\r\n");
                        pout.print(generator.getHomePage());
                    } else if (request.indexOf("/search") > 0) { //
                        pout.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + "text/html" + "\r\n"
                                + "Date: " + new Date() + "\r\n" + "Server: WebServer 1.0\r\n\r\n");
                        pout.print(generator.getSearchForm());
                    } else if (request.indexOf("/newBlock") > 0) { //
                        pout.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + "text/html" + "\r\n"
                                + "Date: " + new Date() + "\r\n" + "Server: WebServer 1.0\r\n\r\n");
                        pout.print(generator.getInputForm());


                    } else if (request.indexOf("/byName") > 0) { //

                        String fname = request.substring((request.indexOf("firstName=") + "firstName=".length()), request.indexOf("&lastName="));
                        String lname = request.substring((request.indexOf("lastName=") + "lastName=".length()), request.indexOf("HTTP/"));

                        List<BlockRecord> result = node.getLedgerMngr().searchByName(fname, lname);
                        pout.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + "text/html" + "\r\n"
                                + "Date: " + new Date() + "\r\n" + "Server: WebServer 1.0\r\n\r\n");

                        pout.print(generator.getResultPage(result));

                    } else if (request.indexOf("/byProvider") > 0) { //
                        String provider = request.substring((request.indexOf("provider=") + "provider=".length()), request.indexOf("HTTP/"));
                        List<BlockRecord> result = node.getLedgerMngr().searchByProvider(provider);

                        pout.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + "text/html" + "\r\n"
                                + "Date: " + new Date() + "\r\n" + "Server: WebServer 1.0\r\n\r\n");

                        pout.print(generator.getResultPage(result));

                    } else if (request.indexOf("/byDate") > 0) { //

                        String from = request.substring((request.indexOf("from=") + "from=".length()), request.indexOf("&to="));
                        String to = request.substring((request.indexOf("&to=") + "&to=".length()), request.indexOf(" HTTP/"));
                        //2018-11-06.14:04:51


                        List<BlockRecord> result = node.getLedgerMngr().searchByDate(from, to);


                        pout.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + "text/html" + "\r\n"
                                + "Date: " + new Date() + "\r\n" + "Server: WebServer 1.0\r\n\r\n");
                        pout.print(generator.getResultPage(result));


                    } else if (request.indexOf("/viewLedger") > 0) { //


                        pout.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + "text/html" + "\r\n"
                                + "Date: " + new Date() + "\r\n" + "Server: WebServer 1.0\r\n\r\n");
                        pout.print(generator.getResultPage(node.getLedgerMngr().getBlockLedger().getBlockRecord()));


                    } else {
                        pout.print("HTTP/1.0 200 OK\r\n" + "Content-Type: " + "text/html" + "\r\n"
                                + "Date: " + new Date() + "\r\n" + "Server: WebServer 1.0\r\n\r\n");
                        pout.print(generator.getHomePage());
                    }

                }
                out.flush();

                if (sock != null)
                    sock.close();
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }
}