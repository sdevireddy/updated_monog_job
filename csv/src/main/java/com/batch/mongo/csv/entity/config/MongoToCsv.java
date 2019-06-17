package com.batch.mongo.csv.entity.config;

import java.io.FileWriter;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * Hello world!
 *
 */
@SpringBootApplication
@EnableBatchProcessing
@Configuration
@EnableAutoConfiguration
// @PropertySource(value="${external.app.properties}",
// ignoreResourceNotFound=false)
public class MongoToCsv implements CommandLineRunner {

	@Autowired
	private JobProperties jobProperties;

	@Autowired
	private ConfigurableApplicationContext context;

	@Value("${job.startdate}")
	// @DateTimeFormat(iso = ISO.DATE_TIME, pattern =
	// "yyyy-MM-dd'T'HH:mm:ss.SSS")
	@DateTimeFormat(iso = ISO.DATE_TIME, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
	// @DateTimeFormat
	private Date startDate;

	public static void main(String[] args) {
		SpringApplication.run(MongoToCsv.class, args);
	}

	public JobProperties getJobProperties() {
		return jobProperties;
	}

	public void setJobProperties(JobProperties jobProperties) {
		this.jobProperties = jobProperties;
	}

	@Override
	public void run(String... args) throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS");
		System.out.println("start values is " + dateFormat.format(startDate));
		String filePath = "C:\\DevelopmentTech\\Aru\\spring\\csv\\config.properties";
		Properties prop = new Properties();
		try (Writer inputStream = new FileWriter(filePath)) {
			prop.setProperty("job.startdate",
					dateFormat.format(getResultEndDate(1)));//.replace("\\:", ":"));
			prop.setProperty("job.enddate",
					dateFormat.format(getResultEndDate(2)));//).replace("\\:", ":"));
			prop.store(inputStream, "job  information");
		}
		System.exit(SpringApplication.exit(context));
	}

	private Date getResultEndDate(int days) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss.SSS");
		Calendar c = Calendar.getInstance();
		c.setTime(dateFormat.parse(dateFormat.format(startDate)));
		c.add(Calendar.DAY_OF_YEAR, days);
		return c.getTime();
	}

}
