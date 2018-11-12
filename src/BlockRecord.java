
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/*
This class is responsible to represent a block records
it has XML annotation so Formatter class can marshal and marshal this class.
This class will be used as an elements of list that BlockLedger class has
This class has getters and setter to modify a block record
 */
@XmlRootElement
class BlockRecord {

    String Seed;
    String BlockNum;
    String SHA256String;
    String SignedSHA256;
    String TimeStampVerification;
    String VerificationProcessID;
    String DataHash;
    String TimeStampEntry;
    String PreviousHash;
    String SignedBlockID;
    String CreatingProcessID;
    String BlockID;
    String SSNum;
    String Fname;
    String Lname;
    String DOB;
    String Diag;
    String Treat;
    String Rx;
    String Note;
    String Provider;

    public String getProvider(){ return Provider;}

    @XmlElement
    public void setProvider(String provider){ this.Provider = provider; }

    public String getSeed() { return Seed; }

    @XmlElement
    public void setSeed(String seed) {
        this.Seed = seed;
    }


    public String getBlockNum() {
        return BlockNum;
    }

    @XmlElement
    public void setBlockNum(String num) {
        this.BlockNum = num;
    }


    public String getSHA256String() {
        return SHA256String;
    }

    @XmlElement
    public void setSHA256String(String SH) {
        this.SHA256String = SH;
    }


    public String getSignedSHA256() {
        return SignedSHA256;
    }

    @XmlElement
    public void setSignedSHA256(String sh) {
        this.SignedSHA256 = sh;
    }


    public String getTimeStampVerification() {
        return TimeStampVerification;
    }

    @XmlElement
    public void setTimeStampVerification(String time) {
        this.TimeStampVerification = time;
    }


    public String getVerificationProcessID() {
        return VerificationProcessID;
    }

    @XmlElement
    public void setVerificationProcessID(String VID) {
        this.VerificationProcessID = VID;
    }


    /*
     setters and getters by creator
     */


    public String getDataHash() {
        return DataHash;
    }

    @XmlElement
    public void setDataHash(String hash) {
        this.DataHash = hash;
    }


    public String getTimeStampEntry() {
        return TimeStampEntry;
    }

    @XmlElement
    public void setTimeStampEntry(String time) {
        this.TimeStampEntry = time;
    }


    public String getPreviousHash() {
        return PreviousHash;
    }

    @XmlElement
    public void setPreviousHash(String pre) {
        this.PreviousHash = pre;
    }


    public String getSignedBlockID() {
        return SignedBlockID;
    }

    @XmlElement
    public void setSignedBlockID(String id) {
        this.SignedBlockID = id;
    }


    public String getCreatingProcessID() {
        return CreatingProcessID;
    }

    @XmlElement
    public void setCreatingProcessID(String CP) {
        this.CreatingProcessID = CP;
    }


    public String getBlockID() {
        return BlockID;
    }

    @XmlElement
    public void setBlockID(String BID) {
        this.BlockID = BID;
    }


    public String getSSNum() {
        return SSNum;
    }

    @XmlElement
    public void setSSNum(String SS) {
        this.SSNum = SS;
    }


    public String getFname() {
        return Fname;
    }

    @XmlElement
    public void setFname(String FN) {
        this.Fname = FN;
    }


    public String getLname() {
        return Lname;
    }

    @XmlElement
    public void setLname(String LN) {
        this.Lname = LN;
    }


    public String getDOB() {
        return DOB;
    }

    @XmlElement
    public void setDOB(String DOB) {
        this.DOB = DOB;
    }


    public String getDiag() {
        return Diag;
    }

    @XmlElement
    public void setDiag(String D) {
        this.Diag = D;
    }


    public String getTreat() {
        return Treat;
    }

    @XmlElement
    public void setTreat(String D) {
        this.Treat = D;
    }


    public String getRx() {
        return Rx;
    }

    @XmlElement
    public void setRx(String D) {
        this.Rx = D;
    }


    public String getNote() {
        return Note;
    }

    @XmlElement
    public void setNote(String D) {
        this.Note = D;
    }

}