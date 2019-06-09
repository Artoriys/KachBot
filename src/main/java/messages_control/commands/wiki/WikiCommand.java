package messages_control.commands.wiki;
//TODO Making exceptions more useful

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiCommand {
    private StringBuilder urlBuilder = new StringBuilder("https://");
    private String[] parsedStrings = new String[2];

    public String getLinkToWikiByMessage(String wordToSearch) {
        buildURL(checkForSpaces(wordToSearch));
        try {
            parseURLresponse();
        } catch (Exception e) {
            e.printStackTrace();
            return "I can't find anything :(";
        }
        return buildReturnLinkAndDesc();
    }

    private void buildURL(String wordToSearch) {
        Pattern ENWikiPattern = Pattern.compile("(\\w*)");
        Matcher ENWikiMather = ENWikiPattern.matcher(wordToSearch);

        Pattern RUWikiPattern = Pattern.compile("([А-Яа-я0-9ё_ ]*)");
        Matcher RUWikiMather = RUWikiPattern.matcher(wordToSearch);

        if (ENWikiMather.matches()) {
            urlBuilder.append("en.wikipedia.org/w/api.php?action=opensearch&format=xml&search=")
                    .append(wordToSearch)
                    .append("&limit=1&utf8=1");
        }
        if (RUWikiMather.matches()) {
            urlBuilder.append("ru.wikipedia.org/w/api.php?action=opensearch&format=xml&search=")
                    .append(wordToSearch)
                    .append("&limit=1&utf8=1");
        }
    }

    private void parseURLresponse() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(urlBuilder.toString());
        document.getDocumentElement().normalize();
        NodeList urlNode = document.getDocumentElement().getElementsByTagName("Url");
        NodeList descNode = document.getDocumentElement().getElementsByTagName("Description");

        parsedStrings[0] = descNode.item(0).getTextContent();
        parsedStrings[1] = urlNode.item(0).getTextContent();
    }

    private String buildReturnLinkAndDesc() {
        return parsedStrings[0] + "\n" + parsedStrings[1];
    }

    private String checkForSpaces(String message) {
        if (message.contains(" ")) return message.replace(" ", "_");
        return message;
    }


}
