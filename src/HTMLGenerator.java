import java.util.List;

/*
This class is responsible to generate html strings that is used by the WebServer.
By using the html generator class, we can have dynamic html pages.
The html string is hard coded within multiple method that will be invoked when needed.
We used external CSS styles from W3.
 */
public class HTMLGenerator {
    public HTMLGenerator(){

    }
    public String getInputForm(){
        String res = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Title</title>\n" +
                "<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"w3-container w3-blue\">\n" +
                "  <h2>New Entry</h2>\n" +
                "</div>\n" +
                "<div class=\"w3-container\">\n" +
                "  <form class=\"w3-container\" method=\"POST\" action=\"newEntry\" enctype=\"text/plain\">\n" +

                "      <label>First Name:<br>\n" +
                "      <input class=\"w3-input\" type=\"text\" name=\"firstName\" value=\"x\"><br>\n" +

                "      <label>Last Name:<br>\n" +
                "      <input class=\"w3-input\" type=\"text\" name=\"lastName\" value=\"0\"><br>\n" +

                "      <label>SSN:<br>\n" +
                "      <input class=\"w3-input\" type=\"number\" name=\"ssn\" value=\"0\"><br>\n" +

                "      <label>Provider:<br>\n" +
                "      <input class=\"w3-input\" type=\"text\" name=\"provider\" value=\"0\"><br>\n" +

                "      <label>DOB (year.month.day):<br>\n" +
                "      <input class=\"w3-input\" type=\"text\" name=\"dob\" value=\"0\"><br>\n" +

                "      <label>Diag:<br>\n" +
                "      <input class=\"w3-input\" type=\"text\" name=\"diag\" value=\"0\"><br>\n" +

                "      <label>Treat:<br>\n" +
                "      <input class=\"w3-input\" type=\"text\" name=\"treat\" value=\"0\"><br>\n" +

                "      <label>Rx:<br>\n" +
                "      <input class=\"w3-input\" type=\"text\" name=\"rx\" value=\"0\"><br>\n" +

                "      <label>Note (if this is a correction Please enter BlockID):<br>\n" +
                "      <input class=\"w3-input\" type=\"text\" name=\"note\" value=\"0\"><br>\n" +
                "    <br>\n" +
                "  <input class=\"w3-btn w3-blue\" type=\"submit\" value=\"Submit\">\n" +
                "</form>\n" +
                "</div>" +
                "</body>\n" +
                "</html>";

        return res;

    }

    public String getConformationPage(){

        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>ConnectFour</title>\n" +
                "<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" +
                "</head>\n" +
                "<body>\n" +
                "<div class=\"w3-container w3-green\"> <h1> Thank you, your entry will be added to the Ledger after it get verified </h1>\n" +
                "<h2> Please wait, and refresh the ledger to see your entry </h2></div>\n" +

                "<form method=\"GET\" action=\"home\">\n" +
                "<input class=\"w3-btn w3-blue\" type=\"submit\" value=\"Home Page\">\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>";
    }

    public String getHomePage(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>ConnectFour</title>\n" +
                "<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" +

                "</head>\n" +
                "<body>\n" +
                "<div class=\"w3-container w3-green\"> <h1> Welcome to Connect Four </h1>\n" +
                "<form class=\"w3-container\" method=\"GET\" action=\"newBlock\">\n" +
                "<input class=\"w3-btn w3-blue\" type=\"submit\" value=\"New Block\">\n" +
                "</form>\n" +
                "<form class=\"w3-container\" method=\"GET\" action=\"viewLedger\">\n" +
                "    <input class=\"w3-btn w3-blue\" type=\"submit\" value=\"View Ledger\">\n" +
                "</form>\n" +
                "<form class=\"w3-container\" method=\"GET\" action=\"search\">\n" +
                "    <input class=\"w3-btn w3-blue\" type=\"submit\" value=\"Search\">\n" +
                "</form>\n" +
                "</div></body>\n" +
                "</html>";
    }

    public String getSearchForm(){
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>ConnectFour</title>\n" +
                "<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" +

                "</head>\n" +
                "<body>\n" +

                "<div class=\"w3-card-4\">"+

                "<br><div class=\"w3-container w3-blue\">\n" +
                "<h4> Search by Name </h4>\n" +
                "</div>\n" +

                "<form class=\"w3-container\" method=\"GET\" action=\"byName\">\n" +
                "    <label>first and last names:<br>\n" +
                "    <input class=\"w3-input\" type=\"text\" name=\"firstName\" value=\"Fname\" required><br>\n" +
                "    <input class=\"w3-input\" type=\"text\" name=\"lastName\" value=\"Lname\" required><br>\n" +
                "<input class=\"w3-btn w3-green\" type=\"submit\" value=\"Search\">\n" +
                "</form>" +
                "<br>\n" +

                "<br></div>"+
                "<div class=\"w3-card-4\">"+

                "<br><div class=\"w3-container w3-blue\">\n" +
                "<h4> Search by Provider </h4>\n" +
                "</div>\n" +

                "<form class=\"w3-container\" method=\"GET\" action=\"byProvider\">\n" +
                "    <label>Provider<br>\n" +
                "    <input class=\"w3-input\" type=\"text\" name=\"provider\" value=\"provider\" required><br>\n" +
                "    <input class=\"w3-btn w3-green\" type=\"submit\" value=\"Search\">\n" +
                "</form><br>\n" +


                "<br></div>"+
                "<div class=\"w3-card-4\">"+


                "<br><div class=\"w3-container w3-blue\">\n" +
                "<h4> Search by Date </h4>\n" +
                "</div>\n" +

                "<form class=\"w3-container\" method=\"GET\" action=\"byDate\">\n" +
                "    <label>From: e.g. YEAR-MONTH-DAY<br>\n" +
                "    <input class=\"w3-input\" type=\"text\" name=\"from\" value=\"2018-11-04\" required><br>\n" +
                "    <label>To: e.g. YEAR-MONTH-DAY<br>\n" +
                "    <input class=\"w3-input\" type=\"text\" name=\"to\" value=\"2018-11-06\" required><br>\n" +
                "    <input class=\"w3-btn w3-green\" type=\"submit\" value=\"Search\">\n" +
                "</form>\n" +
                "<br></div>"+


                "</body>\n" +
                "</html>";
    }

    public String getResultPage(List<BlockRecord> result){
        String beginning = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>ConnectFour</title>\n" +
                "<link rel=\"stylesheet\" href=\"https://www.w3schools.com/w3css/4/w3.css\">\n" +

                "</head>\n" +
                "<body>\n" +
                "<div>\n" +
                "    <h2> result </h2><br>\n" +
                "    <hr>";

        String body = "";


        String end = "</div>\n" +
                "<div>\n" +
                "    <form method=\"GET\" action=\"home\">\n" +
                "        <input type=\"submit\" value=\"Home Page\">\n" +
                "    </form>\n" +
                "\n" +
                "</div>\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        if(result!=null && result.size()>0) {
            body = body + "<ul class=\"w3-ul w3-hoverable\">\n";

            for (BlockRecord b : result) {
                body = body + "<li class=\"w3-blue\">" + b.getBlockID()+"</tr>";
                body = body + "<li>" + b.getFname()+"</li>";
                body = body + "<li>" + b.getLname()+"</li>";
                body = body + "<li>" + b.getDOB()+"</li>";
                body = body + "<li>" + b.getProvider()+"</li>";
                body = body + "<li>" + b.getDiag()+"</li>";
                body = body + "<li>" + b.getTreat()+"</li>";
                body = body + "<li>" + b.getTimeStampEntry()+"</li>";

            }
            body = body + "\n</ul>";

        } else {
            body = body +"Nothing to show";
        }

        return beginning+body+end;

    }
}
