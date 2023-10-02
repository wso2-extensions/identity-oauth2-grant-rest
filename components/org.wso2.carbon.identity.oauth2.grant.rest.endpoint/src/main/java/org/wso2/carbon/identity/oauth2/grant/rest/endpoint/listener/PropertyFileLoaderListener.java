package org.wso2.carbon.identity.oauth2.grant.rest.endpoint.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.constant.Constants;
import org.wso2.carbon.identity.oauth2.grant.rest.endpoint.util.ConfigUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This model class is defined to keep the filed configured data.
 * This will be loaded when webapp is getting deployed.
 */
public class PropertyFileLoaderListener implements ServletContextListener {
    private static final Log LOG = LogFactory.getLog(PropertyFileLoaderListener.class);
    private static Properties filedDefinedProperties = new Properties();
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        LOG.info("Loading the properties.");
        if (ConfigUtil.checkPropertyFileAvailability()) {
            filedDefinedProperties = readConfigurations();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        LOG.info("Destroying the loaded properties.");
    }

    public Properties readConfigurations()  {

        InputStream inputStream = null;
        Properties properties = null;
        String configFilePath = Constants.CONFIG_FILE_PATH + Constants.CONFIG_FILE_NAME;

        try {
            properties = new Properties();
            File configFile = new File(configFilePath);

            if (LOG.isDebugEnabled()) {
                LOG.debug(String.format(" %s file has NOT been loaded from %s.", Constants.CONFIG_FILE_NAME,
                        Constants.CONFIG_FILE_PATH + Constants.CONFIG_FILE_NAME));
            }

            inputStream = new FileInputStream(configFile);
            properties.load(inputStream);
        } catch (FileNotFoundException e) {
            LOG.error("Failed to load service configurations.", e);
        } catch (IOException e) {
            LOG.error("Error while loading teh config file");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOG.error("Error while reading teh config file");
                }
            }
        }
        return properties;
    }

    public static Properties getFiledDefinedProperties() {
        return filedDefinedProperties;
    }
}
