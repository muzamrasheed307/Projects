package base;

import io.restassured.response.Response;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class baseTest {

    public static Response Get_1;
    public static Response Post_1;
    public static Response GetAfterPost_1;
    public static Response Put_1;
    public static Response Delete_1;
    public static int IDnewCountry;
    public static String ValidPermissionToken;
    public Logger logger;

   
    //Azremr
    public static String version_azuremr;
    public static String BaseURI_azuremr;
    public static String clinet_id_azuremr;
    public static String client_secret_azuremr;
    public static String grant_type_azuremr;
    public static String api_domain_azuremr;
    public static String postURLforToken_azuremr;





    public void setuptestbase() throws IOException {

        // reading logger properties
        logger = Logger.getLogger("Api"); //add loggers
        PropertyConfigurator.configure("log4j.properties"); //add log with configurations set in log4j.property
        logger.setLevel(Level.DEBUG);
        //logger.info("__________________________________________");
        //
        //Reading data properties
        //read  a file
        Properties configure = new Properties();
        InputStream TestFile = new FileInputStream("azuremr.properties");
        configure.load(TestFile);
       
        //Azuremr
       // RestAssured.useRelaxedHTTPSValidation();
        version_azuremr= configure.getProperty("version_azuremr");
        BaseURI_azuremr=configure.getProperty("BaseURI_azuremr");
        clinet_id_azuremr=configure.getProperty("client_ID_azuremr");
        client_secret_azuremr=configure.getProperty("client_secret_azuremr");
        grant_type_azuremr=configure.getProperty("grant_type_azuremr");
        api_domain_azuremr=configure.getProperty("api_domain_azuremr");
        postURLforToken_azuremr=configure.getProperty("post_azuremr");


    }
}
