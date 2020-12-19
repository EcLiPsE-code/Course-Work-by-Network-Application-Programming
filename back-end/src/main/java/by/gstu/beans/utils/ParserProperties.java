package by.gstu.beans.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ParserProperties {
    private static final Logger logger = LogManager.getLogger();

    private FileInputStream fileInputStream;
    private final Properties property = new Properties();

    public ParserProperties(){
        try{
            fileInputStream = new FileInputStream("src/main/resources/config.properties");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public String getProperty(String key){
        try{
            property.load(fileInputStream);
            return property.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
