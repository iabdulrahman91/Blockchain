import javax.xml.bind.*;
import java.io.*;

/*
Formatter class is responsible for marshalling and unmarshalling data
this class is used to ensure constancy on reading and writing from/to network connection
it uses XML as external data format
It can do:
- return xml representation for block record
- return xml representation for block ledger
- return BlockLedger object by reading XML file
- return BlockRecord object by reading XML file
 */
public class Formatter {

    public BlockLedger unMarshalLedger(String s) throws JAXBException {
        String text = s.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
        BlockLedger blockLedger = null;
        StringReader reader = null;

        JAXBContext jaxbContext = JAXBContext.newInstance(BlockLedger.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        reader = new StringReader(text);
        blockLedger = (BlockLedger) jaxbUnmarshaller.unmarshal(reader);


        return blockLedger;
    }

    public BlockRecord unMarshalRecord(String s) throws JAXBException {
        String text = s.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>", "");
        BlockRecord blockRecord = null;
        StringReader reader = null;

        JAXBContext jaxbContext = JAXBContext.newInstance(BlockRecord.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        reader = new StringReader(text);
        blockRecord = (BlockRecord) jaxbUnmarshaller.unmarshal(reader);

        return blockRecord;
    }

    public String marshalLedger(BlockLedger blockLedger) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(BlockLedger.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(blockLedger, sw);
        String fullLedger = sw.toString();
        return (fullLedger);


    }

    public String marshalRecord(BlockRecord blockRecord) throws JAXBException {

        JAXBContext jaxbContext = JAXBContext.newInstance(BlockRecord.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(blockRecord, sw);
        String fullBlock = sw.toString();
        return fullBlock;

    }
}
