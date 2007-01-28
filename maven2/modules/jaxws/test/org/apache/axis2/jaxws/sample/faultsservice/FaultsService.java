
package org.apache.axis2.jaxws.sample.faultsservice;

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
@WebServiceClient(name = "FaultsService", targetNamespace = "http://org/test/polymorphicfaults", wsdlLocation = "FaultsService.wsdl")
public class FaultsService
    extends Service
{

    private final static URL FAULTSSERVICE_WSDL_LOCATION;
    private static String wsdlLocation="/test/org/apache/axis2/jaxws/sample/faultsservice/META-INF/FaultsService.wsdl";
    static {
        URL url = null;
        try {
            try{
                String baseDir = new File(System.getProperty("basedir")).getCanonicalPath();
                wsdlLocation = new File(baseDir + wsdlLocation).getAbsolutePath();
            }catch(Exception e){
                e.printStackTrace();
            }
            File file = new File(wsdlLocation);
            url = file.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        FAULTSSERVICE_WSDL_LOCATION = url;
    }

    public FaultsService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public FaultsService() {
        super(FAULTSSERVICE_WSDL_LOCATION, new QName("http://org/test/polymorphicfaults", "FaultsService"));
    }

    /**
     * 
     * @return
     *     returns FaultsServicePortType
     */
    @WebEndpoint(name = "FaultsPort")
    public FaultsServicePortType getFaultsPort() {
        return (FaultsServicePortType)super.getPort(new QName("http://org/test/polymorphicfaults", "FaultsPort"), FaultsServicePortType.class);
    }

}
