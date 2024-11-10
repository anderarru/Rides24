package businesslogic;

import configuration.ConfigXML;
import dataAccess.DataAccess;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.logging.Logger;

public class BLFactory {
    private static BLFacade instance = null;  // Singleton BLFacade


    private BLFactory() {}  //pribatua zuzenean ez instantziatzeko

  
    public static BLFacade getBLFacade(boolean isLocal) {
        if (instance == null) { 
            try {
            	ConfigXML c = ConfigXML.getInstance();
                if (isLocal) {
           
                    DataAccess da = new DataAccess();
                    instance = new BLFacadeImplementation(da);  //  local
                } else {
                    String serviceName = "http://" + c.getBusinessLogicNode() + ":" + c.getBusinessLogicPort() + "/ws/"
                            + c.getBusinessLogicName() + "?wsdl";
                    URL url = new URL(serviceName);
                    QName qname = new QName("http://businesslogic/", "BLFacadeImplementationService");
                    Service service = Service.create(url, qname);
                    instance = service.getPort(BLFacade.class);  // remote
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;  
    }
}
