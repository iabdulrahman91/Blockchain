import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/*
This class is responsible for managing unverified blocks
it suppose to keep track of what block has been not verified, so it works as queue
for entered unverified block.
This class can do:
- receive medical entry and create a block record object.
- once block record object is made, it will be added to toDo list which will by consumed by unVerified block manager.
- it follow FIFO algorithm for giving next unverified block.
- it knows it size (how many unVerified block it has)
 */
public class BlocksQueue {
    Node node;
    private Hash hash;
    private List<BlockRecord> toDo;

    public BlocksQueue(Node node) {
        this.node = node;
        hash = new Hash();
        toDo = new ArrayList<BlockRecord>();
    }


    public void addBlock(String dob, String fName, String lName, String ssn, String provider, String diag, String gr, String treat, String note) {
        BlockRecord br = new BlockRecord();
        br.setTimeStampEntry(String.format("%1$s %2$tF.%2$tT", "", new Date()));
        String suuid = new String(UUID.randomUUID().toString());

        br.setSignedBlockID("#Suuid");
        br.setCreatingProcessID(node.getPID() + "");


        br.setBlockID(suuid);


        // fill the block
        br.setDOB(dob);
        br.setFname(fName);
        br.setLname(lName);
        br.setSSNum(ssn);
        br.setProvider(provider);
        br.setDiag(diag);
        br.setRx(gr);
        br.setTreat(treat);
        br.setNote(note);


        LedgerMngr ledgerMngr = node.getLedgerMngr();
        // do for pre
        String hashPre = (ledgerMngr.isEmpty()) ? hash.doHash("0") : (ledgerMngr.getLastBlock().getSHA256String());
        br.setPreviousHash(hashPre);
        // do for data
        String data = br.getSSNum() + br.getDOB() + br.getFname() + br.getLname() + br.getDiag() + br.getTreat() + br.getRx();
        String hashData = hash.doHash(data);
        br.setDataHash(hashData);


        // get RandSeed
        final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        for (int x = 0; x < 64; x++) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        String randSeed = builder.toString();

        // do for SHA256 String
        String hashedThreeComponents = hash.doHash((hashPre + hashData + randSeed));
        br.setSHA256String(hashedThreeComponents);


        br.setSeed("later");
        br.setTimeStampVerification("later");
        br.setVerificationProcessID("later");
        br.setSignedSHA256("later");
        br.setBlockNum("later");


        toDo.add(br);
    }


    public BlockRecord getNextUnverified() throws IndexOutOfBoundsException {
        return toDo.remove(0);
    }

    public int getSize() {
        return this.toDo.size();
    }
}
