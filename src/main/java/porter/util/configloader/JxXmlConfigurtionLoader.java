package porter.util.configloader;

import io.vertx.core.json.JsonObject;
import org.w3c.dom.*;
import porter.verticle.JxVerticleDeclaration;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by parker on 2015/10/14.
 */
public class JxXmlConfigurtionLoader implements JiConfigurtionLoader {

    private String _xmlFileName;
    public JxXmlConfigurtionLoader(String xmlFileName) {
        _xmlFileName = xmlFileName;
    }

    /**
     * Read verticles configuration from xml file.
     * @return A list of verticle declaration.
     */
    @Override
    public List<JxVerticleDeclaration> getVerticleDeclarations() {
        List<JxVerticleDeclaration> verticleDeclerations = new ArrayList<JxVerticleDeclaration>();
        Document doc = loadXmlDocument(_xmlFileName);
        Element rootElement = doc.getDocumentElement();

        Element verticlesTag = (Element) rootElement.getElementsByTagName("verticles").item(0);
        // return Empty list if tag verticles not found.
        if (!validateElementNode(verticlesTag, "verticles")) {
            return verticleDeclerations;
        }

        NodeList nodes = ((Element) verticlesTag).getElementsByTagName("verticle");
        // Travel for getting all verticles.
        for (int i = 0; i < nodes.getLength(); ++i) {
            Element verticleElement = (Element) nodes.item(i);
            JxVerticleDeclaration verticleDefine = new JxVerticleDeclaration();

            // Get class name of verticle.
            verticleDefine.setClassName(verticleElement.getAttribute("class"));

            // Get number of verticle instance.
            String number = verticleElement.getAttribute("number");
            if (number.isEmpty())
                verticleDefine.setNumber(1);
            else
                verticleDefine.setNumber(Integer.parseInt(number));

            // Get configuration of this verticle. Such as server ip, port, etc.
            Element configTag = (Element) verticleElement.getElementsByTagName("config").item(0);
            JsonObject configJson = getTagAttributes(configTag);
            verticleDefine.setConfiguration(configJson);
            verticleDeclerations.add(verticleDefine);
        }
        return verticleDeclerations;
    }

    private Document loadXmlDocument(String filename) {
        try {
            InputStream iStream = this.getClass().getResourceAsStream(filename);
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder buider = builderFactory.newDocumentBuilder();
            Document doc = buider.parse(iStream);
            return doc;
        } catch (Exception e) {
            System.err.println("Oh oh ... Load configuration error!");
            System.err.println(filename);
            e.printStackTrace();
            return null;
        }
    }

    private JsonObject getTagAttributes(Element configTag) {
        JsonObject configJson = new JsonObject();
        if (validateElementNode(configTag, "config")) {
            NamedNodeMap allConfiguration = configTag.getAttributes();
            for (int j = 0; j < allConfiguration.getLength(); ++j) {
                Node configItem = allConfiguration.item(j);
                configJson.put(configItem.getNodeName(), configItem.getNodeValue());
            }
            return configJson;
        } else return null;
    }

    private boolean validateElementNode(Element element, String elementName) {
        if (element == null || element.getNodeType() != Node.ELEMENT_NODE) {
            String errMsg;
            if (elementName != null) {
                errMsg = String.format("[ERROR]\"config.xml\" error: tag <%s> not found!",
                        elementName);
            } else {
                errMsg = "[ERROR]\"config.xml\" error: tag not found!";
            }
            System.err.println(errMsg);
            return false;
        } else return true;
    }
}
