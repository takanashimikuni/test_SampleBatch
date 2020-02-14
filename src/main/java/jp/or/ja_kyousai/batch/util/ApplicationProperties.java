package jp.or.ja_kyousai.batch.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties
public class ApplicationProperties {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.user}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driverClassName}")
    private String driverClassName;

    @Value("${dataClearBeforProcess}")
    private Boolean dataClearBeforProcess;

    @Value("${inputFileName}")
    private String inputFileName;

    public String getUrl() {
        return url;
    }


    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }


    public boolean isDataClearBeforProcess() {
        return dataClearBeforProcess;
    }

    public String getInputFileName() {
        return inputFileName;
    }
}
