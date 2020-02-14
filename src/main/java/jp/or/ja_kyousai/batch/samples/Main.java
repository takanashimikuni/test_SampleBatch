package jp.or.ja_kyousai.batch.samples;

import jp.or.ja_kyousai.batch.samples.tasklet.BatchConfiguration;
import org.springframework.batch.core.launch.support.CommandLineJobRunner;
import org.springframework.boot.SpringApplication;

public class Main {
    public static void main(String [] args) {
        //ApplicationContext context = new ClassPathXmlApplicationContext("application-context.xml");

        new CommandLineJobRunner();
        System.exit(SpringApplication.exit(SpringApplication.run(
                BatchConfiguration.class, args)));
    }
}
