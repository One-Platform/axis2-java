
package org.apache.axis2.jaxws.sample.faults;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * This class was generated by the JAXWS SI.
 * JAX-WS RI 2.0_01-b15-fcs
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "FaultyWebServiceService", targetNamespace = "http://org/test/faults", wsdlLocation = "FaultyWebService1.wsdl")
public class FaultyWebServiceService
    extends Service
{

    private final static URL FAULTYWEBSERVICESERVICE_WSDL_LOCATION;
    private static String wsdlLocation="/test/org/apache/axis2/jaxws/sample/faults/META-INF/FaultyWebService.wsdl";
    static {
        URL url = null;
        try {
        	try{
	        	String baseDir = new File(".").getCanonicalPath();
	        	wsdlLocation = new File(baseDir + wsdlLocation).getAbsolutePath();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        	File file = new File(wsdlLocation);
        	url = file.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FAULTYWEBSERVICESERVICE_WSDL_LOCATION = url;
    }

    public FaultyWebServiceService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FaultyWebServiceService() {
        super(FAULTYWEBSERVICESERVICE_WSDL_LOCATION, new QName("http://org/test/faults", "FaultyWebServiceService"));
    }

    /**
     * 
     * @return
     *     returns FaultyWebServicePortType
     */
    @WebEndpoint(name = "FaultyWebServicePort")
    public FaultyWebServicePortType getFaultyWebServicePort() {
        return (FaultyWebServicePortType)super.getPort(new QName("http://org/test/faults", "FaultyWebServicePort"), FaultyWebServicePortType.class);
    }

}
