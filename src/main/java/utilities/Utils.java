package utilities;

import Constants.FrameworkConstatants;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.*;
import java.util.Objects;
import java.util.Properties;

public class Utils {
    private Utils(){}

    private static RequestSpecification req;
    public static RequestSpecification requestSpecification() throws IOException {
        if(Objects.isNull(req)){
            PrintStream log =new PrintStream(new FileOutputStream("logging.txt"));

            req=new RequestSpecBuilder().setBaseUri(getPropertyKeyValue("baseUrl"))
                    .addQueryParam("key", "qaclick123")
                    .addFilter(RequestLoggingFilter.logRequestTo(log))
                    .addFilter(ResponseLoggingFilter.logResponseTo(log))
                    .setContentType(ContentType.JSON).build();
            return req;
        }
        return null;
    }

    private static String getPropertyKeyValue(String key) throws IOException {
        Properties prop =new Properties();
        FileInputStream files =new FileInputStream(FrameworkConstatants.getRESOURCEPATH()+"global.properties");
        prop.load(files);
        return  prop.getProperty(key);
    }

    public static String getJsonPath(Response response, String key){
        return new JsonPath(response.asString()).get(key).toString();
    }
}
