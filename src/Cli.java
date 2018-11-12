import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
/*
This class is responsible to run Command line interface for the user.
It can be used for the following purposes:
- adding new medical entry
- displaying ledger (list of verified blocks)
- search the ledger for specific searching criteria
- display active nodes on the blockchain network
 */
public class Cli implements Runnable {
    Node node;

    public Cli(Node node) {
        this.node = node;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                System.out.println("Options:\n '1' - To enter data" +
                        "\n '2' - To Show ledger" +
                        "\n '3' - To search by Name" +
                        "\n '4' - To search by Provider" +
                        "\n '5' - To search by Date" +
                        "\n '6' - To Show active Nodes");
                String line = scanner.nextLine();
                try {
                    int input = Integer.parseInt(line);
                    if (input == 1) {
                        Scanner msgScan = new Scanner(System.in);


                        // 1- DOB
                        System.out.println("DOB year.month.day (no Space plz): ");
                        String dob = msgScan.next();

                        // 2- FName
                        System.out.println("Fname (no Space plz): ");
                        String fName = msgScan.next();

                        // 3- LName
                        System.out.println("Lname (no Space plz): ");
                        String lName = msgScan.next();

                        // 4- SSN
                        System.out.println("SSN (no Space plz): ");
                        String ssn = msgScan.next();

                        // 4- SSN
                        System.out.println("Provider: ");
                        String provider = msgScan.next();

                        // 5- GDiag
                        System.out.println("GDiag (no Space plz): ");
                        String diag = msgScan.next();

                        // 6-GRx
                        System.out.println("GRx (no Space plz): ");
                        String gr = msgScan.next();

                        // 7- GTreat
                        System.out.println("GTreat (no Space plz): ");
                        String treat = msgScan.next();

                        // 8 - Note
                        System.out.println("if this Correction, please enter BlockID (else _): ");
                        String note = msgScan.next();


                        node.getBlocksQueue().addBlock(dob, fName, lName, ssn, provider, diag, gr, treat, note);

                    } else if (input == 2) {
                        if (node.getLedgerMngr().isEmpty()) {
                            System.out.println("Ledger is empty");
                        } else {
                            for (BlockRecord b : node.getLedgerMngr().getBlockLedger().getBlockRecord()) {
                                System.out.println("BlockID: " + b.getBlockID());
                                System.out.println("Fname  :" + b.getFname());
                                System.out.println("Lname  :" + b.getLname());
                                System.out.println("Provider  :" + b.getProvider());
                                System.out.println("DOB    :" + b.getDOB());
                                System.out.println("SSN    :" + b.getSSNum());
                                System.out.println("Diag   :" + b.getDiag());
                                System.out.println("RX     :" + b.getRx());
                                System.out.println("Treat  :" + b.getTreat());
                            }
                        }
                    } else if (input == 3) {
                        if (node.getLedgerMngr().isEmpty()) {
                            System.out.println("Ledger is empty");
                        } else {
                            Scanner msgScan = new Scanner(System.in);

                            System.out.println("Enter first name: ");
                            String fname = msgScan.next();
                            System.out.println("Enter last name: ");
                            String lname = msgScan.next();
                            List<BlockRecord> result = node.getLedgerMngr().searchByName(fname, lname);
                            for (BlockRecord b : result) {
                                System.out.println("BlockID: " + b.getBlockID());
                                System.out.println("Fname  :" + b.getFname());
                                System.out.println("Lname  :" + b.getLname());
                                System.out.println("Provider  :" + b.getProvider());
                                System.out.println("DOB    :" + b.getDOB());
                                System.out.println("SSN    :" + b.getSSNum());
                                System.out.println("Diag   :" + b.getDiag());
                                System.out.println("RX     :" + b.getRx());
                                System.out.println("Treat  :" + b.getTreat());
                            }

                        }

                    } else if (input == 4) {
                        if (node.getLedgerMngr().isEmpty()) {
                            System.out.println("Ledger is empty");
                        } else {
                            Scanner msgScan = new Scanner(System.in);

                            System.out.println("Enter provider: ");
                            String provider = msgScan.next();

                            List<BlockRecord> result = node.getLedgerMngr().searchByProvider(provider);
                            for (BlockRecord b : result) {
                                System.out.println("BlockID: " + b.getBlockID());
                                System.out.println("Fname  :" + b.getFname());
                                System.out.println("Lname  :" + b.getLname());
                                System.out.println("Provider  :" + b.getProvider());
                                System.out.println("DOB    :" + b.getDOB());
                                System.out.println("SSN    :" + b.getSSNum());
                                System.out.println("Diag   :" + b.getDiag());
                                System.out.println("RX     :" + b.getRx());
                                System.out.println("Treat  :" + b.getTreat());
                            }

                        }

                    } else if (input == 5) {
                        if (node.getLedgerMngr().isEmpty()) {
                            System.out.println("Ledger is empty");
                        } else {
                            Scanner msgScan = new Scanner(System.in);

                            System.out.println("Enter dates e.g. 1991-12-24 ");
                            System.out.println("Enter from Date: ");
                            String from = msgScan.next();
                            System.out.println("Enter to Date: ");
                            String to = msgScan.next();

                            List<BlockRecord> result = node.getLedgerMngr().searchByDate(from, to);

                            for (BlockRecord b : result) {
                                System.out.println("BlockID: " + b.getBlockID());
                                System.out.println("Fname  :" + b.getFname());
                                System.out.println("Lname  :" + b.getLname());
                                System.out.println("Provider  :" + b.getProvider());
                                System.out.println("DOB    :" + b.getDOB());
                                System.out.println("SSN    :" + b.getSSNum());
                                System.out.println("Diag   :" + b.getDiag());
                                System.out.println("RX     :" + b.getRx());
                                System.out.println("Treat  :" + b.getTreat());
                            }

                        }

                    } else if (input == 6) {
                        List<Integer> act = node.getNodesTracker().getActiveNodes();
                        System.out.println(act.toString());
                    }
                } catch (Exception e) {
                    System.out.println("Invalid Entry");
                    System.out.println(node.getBlocksQueue().getSize());
                }

            }
        } catch (IllegalStateException | NoSuchElementException e) {
            // System.in has been closed
            System.out.println("System.in was closed; exiting");
        }


    }
}