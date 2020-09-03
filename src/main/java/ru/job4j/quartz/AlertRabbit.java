package ru.job4j.quartz;

import org.quartz.*;

import static org.quartz.JobBuilder.*;

import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import java.util.Date;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class AlertRabbit {
    private static final Logger LOG = LoggerFactory.getLogger(AlertRabbit.class.getName());

    public static void main(String[] args) {
        try {
            Connection connection = createConnection();
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("database", connection);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(getTime())
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();
        } catch (Exception e) {
            LOG.error(e.toString());
        }
    }

    private static Connection createConnection() {
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("driver-class-name"));
            return DriverManager.getConnection(
                    config.getProperty("url"),
                    config.getProperty("username"),
                    config.getProperty("password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            Connection connection = (Connection) context.getJobDetail().getJobDataMap().get("database");
            String insert = "insert into jobs.rabbit(created) values (?)";
            try (final PreparedStatement statement = connection.prepareStatement(insert)) {
                statement.setDate(1, new java.sql.Date(new Date().getTime()));
                statement.executeUpdate();
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private static int getTime() {
        try (InputStream in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties config = new Properties();
            config.load(in);
            return Integer.parseInt(config.getProperty("rabbit.interval"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException();
    }
}
