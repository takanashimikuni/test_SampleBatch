package jp.or.ja_kyousai.batch.samples.tasklet;

import jp.or.ja_kyousai.batch.mapper.MembersMapper;
import jp.or.ja_kyousai.batch.util.ApplicationProperties;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Configuration
@EnableBatchProcessing
@EnableAutoConfiguration
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    private final Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                        logger.info("Step1の開始");

                        sqlSessionFactory().getConfiguration().addMapper(MembersMapper.class);
                        MembersMapper mapper = sqlSession().getMapper(MembersMapper.class);

                        if (applicationProperties().isDataClearBeforProcess()) {
                            mapper.truncate();
                        }

                        File file = new File(applicationProperties().getInputFileName());
                        BufferedReader br = new BufferedReader(new FileReader(file));

                        String line = br.readLine();
                        // 1行ずつCSVファイルを読み込む
                        while (line != null) {
                            String[] columns = line.split(",", 0); // 行をカンマ区切りで配列に変換

                            mapper.insert(columns[0], columns[1], columns[2]);
                            line = br.readLine();
                        }
                        br.close();

                        logger.info("Step1の終了");

                        return null;
                    }
                })
                .build();
    }

    /*
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step01")
                .tasklet(new Tasklet() {
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

                        logger.info("Step2の開始");

                        ApplicationContext context = new ClassPathXmlApplicationContext("/application-context.xml");
                        SqlSession sqlSession = (SqlSession)context.getBean("sqlSession");
                        MembersMapper mapper = sqlSession.getMapper(MembersMapper.class);
                        mapper.delete("66");
                        mapper.insert("66","333333","443333333");

                        logger.info("Step2の終了");

                        return null;
                    }
                })
                .build();
    }
    */

    @Bean
    public Job job(Step step1) throws Exception {
        return jobBuilderFactory.get("job1" + System.currentTimeMillis())
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(applicationProperties().getDriverClassName());
        dataSource.setUrl(applicationProperties().getUrl());
        dataSource.setUsername(applicationProperties().getUser());
        dataSource.setPassword(applicationProperties().getPassword());
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        return factoryBean.getObject();
    }

    @Bean
    public SqlSession sqlSession() throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory());
    }

    @Bean
    public ApplicationProperties applicationProperties() {
        return new ApplicationProperties();
    }
}