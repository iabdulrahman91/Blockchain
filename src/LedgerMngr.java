import javax.xml.bind.JAXBException;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/*
This class is responsible for managing any interaction with the ledger
it can do:
- read ledger from local disk when node starts
- write ledger on disk after any updates
- return last block has been added to the ledger if needed (e.g. when we need the previous hash for creating new block)
- search ledger for specify criterias
- add block to ledger after is get verified. (BlockReciever determine the winner and forward it to this class).
- return blockLedger if needed
- update ledger when it receive new ledger from other nodes (longest ledger win)

In the future:
- ability to return copy of ledger that contain correct block only (since no block can be removed).
- ability to handle forked ledgers.

 */
public class LedgerMngr {
    private static BlockLedger blockLedger;
    private Formatter formatter;
    private Node node;

    public LedgerMngr(Node node) {
        this.node = node;
        blockLedger = new BlockLedger();
        this.formatter = new Formatter();
        this.readFromLocal();
    }

    public void readFromLocal() {

        String legerFromFile = "";
        try {
            File file = new File("BlockchainLedger.xml");
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                legerFromFile = legerFromFile + sc.nextLine();
            }
            sc.close();
            List<BlockRecord> fromFile = formatter.unMarshalLedger(legerFromFile).getBlockRecord();
            blockLedger.setBlockRecord(fromFile);
        } catch (FileNotFoundException e) {
            writeToLocal();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public void writeToLocal() {
        try {
            FileWriter fw = new FileWriter("BlockchainLedger.xml", false);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw);
            out.println(formatter.marshalLedger(blockLedger));
            out.close();

        } catch (IOException e) {
            System.out.println("error in writing to file");
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    public BlockRecord getLastBlock() {
        return blockLedger.getBlockRecord().get(blockLedger.getBlockRecord().size() - 1);
    }

    public Boolean isEmpty() {

        return blockLedger.getBlockRecord().isEmpty();
    }


    public BlockLedger getBlockLedger() {
        return blockLedger;
    }

    public void addBlockRecord(BlockRecord br) {
        Hash hash = new Hash();
        if (isEmpty()) {
            blockLedger.getBlockRecord().add(br);
            writeToLocal();
            node.multiCast();
        } else {
            String data = br.getSSNum() + br.getDOB() + br.getFname() + br.getLname() + br.getDiag() + br.getTreat() + br.getRx();
            String result = hash.doHash((getLastBlock().getSHA256String() + hash.doHash(data) + br.getSeed()));
            int numOfChar = 64 - node.getComplixity();

            if (br.getSHA256String().substring(numOfChar).equals(result.substring(numOfChar))) {
                br.setBlockNum(blockLedger.getBlockRecord().size() + "");
                blockLedger.getBlockRecord().add(br);
                writeToLocal();
                node.multiCast();

            } else {
                System.out.println("inValid Block");
            }

        }
    }

    // search might go into Ledger class
    public static List<BlockRecord> searchByProvider(String prvider) {
        List<BlockRecord> result = new ArrayList<BlockRecord>();
        for (BlockRecord b : blockLedger.getBlockRecord()) {

            if (prvider.indexOf(b.getProvider()) >= 0) {
                result.add(b);
            }
        }
        return result;
    }

    public static List<BlockRecord> searchByDate(String from, String to) {
        List<BlockRecord> result = new ArrayList<BlockRecord>();
        //2018-11-05.14:48:07
        from = from + ".00:00:00";
        to = to + ".23:59:59";
        Date fromDate = null;
        Date toDate = null;
        try {
            fromDate = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").parse(from);
            toDate = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").parse(to);


            for (BlockRecord b : blockLedger.getBlockRecord()) {
                Date recordDate = null;

                recordDate = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss").parse(b.getTimeStampEntry());

                if (recordDate.after(fromDate) && recordDate.before(toDate)) {
                    result.add(b);
                }
            }

        } catch (Exception e) {
            System.out.println(from);
            System.out.println(to);
            System.out.println("error in date method");
        }


        return result;
    }

    public static List<BlockRecord> searchByName(String fname, String lname) {
        List<BlockRecord> result = new ArrayList<BlockRecord>();
        for (BlockRecord b : blockLedger.getBlockRecord()) {
            if (fname.indexOf(b.getFname()) >= 0 && lname.indexOf(b.getLname()) >= 0) {
                result.add(b);
            }
        }
        return result;
    }

    public void updateLedger(BlockLedger newBlockLedger) {

        // if the recieved ledger is longer than the one Node has
        if (newBlockLedger.getBlockRecord().size() >= getBlockLedger().getBlockRecord().size()) {
            System.out.println("Longer");
            getBlockLedger().setBlockRecord(newBlockLedger.getBlockRecord());
            writeToLocal();
        }

    }
}
