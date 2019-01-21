package tw.noah.spring.boot.client;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;


/**
 * 範例參考：https://docs.oracle.com/middleware/1212/toplink/TLJAX/simple_values.htm#TLJAX282
 */
@Log4j2
@Component
public class XmlExample {

  public void run() {

//    String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Record><a>this is a standard tag.</a><Field name=\"apa01\" value=\"FC119010042:3020\">a</Field><Field name=\"message\" value=\"Failure\">b</Field></Record>";

    String data ="<200,<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body><fjs1:CreateFeeBaseBusinessDataResponse xmlns:fjs1=\"http://www.dsc.com.tw/tiptop/TIPTOPServiceGateWay\"><fjs1:response>&lt;Response&gt;\n"
        + "  &lt;Execution&gt;\n" + "    &lt;Status code=\"0\" sqlcode=\"0\" description=\"apa11: \"/&gt;\n" + "  &lt;/Execution&gt;\n"
        + "  &lt;ResponseContent&gt;\n" + "    &lt;Parameter&gt;\n" + "      &lt;Record&gt;\n"
        + "        &lt;Field name=\"apa01\" value=\"FC119010042:3020\"/&gt;\n" + "        &lt;Field name=\"message\" value=\"Failure\"/&gt;\n"
        + "      &lt;/Record&gt;\n" + "    &lt;/Parameter&gt;\n" + "    &lt;Document/&gt;\n" + "  &lt;/ResponseContent&gt;\n"
        + "&lt;/Response&gt;</fjs1:response></fjs1:CreateFeeBaseBusinessDataResponse></SOAP-ENV:Body></SOAP-ENV:Envelope>,{Server=[Apache-Coyote/1.1], X-Powered-By=[Servlet 2.5; JBoss-5.0/JBossWeb-2.1], asyncServiceInvoke=[false], Date=[Thu, 17 Jan 2019 09:17:19 GMT], Content-Type=[text/xml;charset=UTF-8], Content-Length=[774]}> ";
    data = data.substring(data.indexOf("&lt;Record&gt;"),data.indexOf("&lt;/Record&gt;") + "&lt;/Record&gt;".length());
    StringBuffer sbXml = new StringBuffer();
    Arrays.stream(data.split("\n")).forEach(v->{
      String v2 = v + "\n";
      v = v2.replaceFirst("&lt;","<").replaceFirst("&gt;\n",">").trim();
      sbXml.append(v).append("\n");
    });
    data = sbXml.toString();
    log.info(data);
    InputStream xml = new ByteArrayInputStream(data.getBytes());

    JAXBContext jaxbContext = null;
    Unmarshaller unmarshaller = null;
    Model m = null;
    try {
      jaxbContext = JAXBContext.newInstance(Model.class);
      unmarshaller = jaxbContext.createUnmarshaller();
      m = (Model) unmarshaller.unmarshal(xml);
    } catch (JAXBException e) {
      log.error(e, e);
      throw new RuntimeException(e);
    }
    log.info(m);

    log.info("----------------");

  }


  @Data
  @XmlRootElement(name = "Record")
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Model {

    @XmlElement(name="a")
    private String a;

    @XmlElement(name="Field")
    private List<Record> records;

  }

  @Data
  @XmlAccessorType(XmlAccessType.FIELD)
  public static class Record{

    @XmlAttribute(name="name")
    private String name;

    @XmlAttribute(name="value")
    private String value;
  }

}
