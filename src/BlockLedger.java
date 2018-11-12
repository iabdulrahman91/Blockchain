/*
This class is responsible for holding list of Block records.
The reason we have Block annotated with XML is to marshal and unmarshal data to the network
The class has getter and setter methods that will be used by ledger manager mostly
 */
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class BlockLedger {


    List<BlockRecord>  BlockRecord;

    public BlockLedger(){
        this.BlockRecord = new ArrayList<BlockRecord>();
    }

    public List<BlockRecord> getBlockRecord(){
        return BlockRecord;
    }

    @XmlElement(name = "blockRecord")
    public void setBlockRecord(List<BlockRecord> list){
        this.BlockRecord = list;
    }


}



