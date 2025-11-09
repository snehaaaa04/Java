// Nimbus - Online Student Management System - Single-file project scaffold
// Tree-like layout: copy files into a Maven project structure (or use the README below)

/*
pom.xml
----------------------------------------
*/

<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.nimbus</groupId>
    <artifactId>student-management</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- Spring Core + Context + Tx -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>5.3.30</version>
        </dependency>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-tx</artifactId>
            <version>5.3.30</version>
        </dependency>

        <!-- Hibernate -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>5.6.15.Final</version>
        </dependency>

        <!-- H2 for easy testing (swap to MySQL by changing datasource & dialect) -->
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <version>2.1.214</version>
            <scope>runtime</scope>
        </dependency>

        <!-- MySQL (optional) -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.33</version>
        </dependency>

        <!-- JPA annotations -->
        <dependency>
            <groupId>jakarta.persistence</groupId>
            <artifactId>jakarta.persistence-api</artifactId>
            <version>2.2.3</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.36</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-simple</artifactId>
            <version>1.7.36</version>
        </dependency>

        <!-- JUnit (optional, for tests) -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.9.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.10.1</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>


/*
src/main/resources/hibernate.cfg.xml (alternative: use java config shown later)
----------------------------------------
*/

<!--
<?xml version='1.0' encoding='utf-8'?>
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        <property name="hibernate.dialect">org.hibernate.dialect.H2Dialect</property>
        <property name="hibernate.show_sql">true</property>
        <property name="hibernate.hbm2ddl.auto">update</property>
        <property name="hibernate.format_sql">true</property>

        <property name="connection.driver_class">org.h2.Driver</property>
        <property name="connection.url">jdbc:h2:~/nimbusdb;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false</property>
        <property name="connection.username">sa</property>
        <property name="connection.password"></property>

        <!-- mapping classes optional when using annotated classes + SessionFactory via LocalSessionFactoryBean -->
    </session-factory>
</hibernate-configuration>
-->


/*
Java package: com.nimbus.model
----------------------------------------
*/

// Student.java
package com.nimbus.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id")
    private Course course;

    // current balance for fee operations
    private Double balance = 0.0;

    public Student() {}

    public Student(String name, Course course, Double balance) {
        this.name = name;
        this.course = course;
        this.balance = balance == null ? 0.0 : balance;
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", name='" + name + '\'' + ", course=" + (course!=null?course.getName():"None") + ", balance=" + balance + '}';
    }
}


// Course.java
package com.nimbus.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(name = "course_name", nullable = false)
    private String name;

    private Integer duration; // in weeks or months

    public Course() {}

    public Course(String name, Integer duration) { this.name = name; this.duration = duration; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Course)) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }

    @Override
    public String toString() {
        return "Course{" + "id=" + id + ", name='" + name + '\'' + ", duration=" + duration + '}';
    }
}


// Payment.java
package com.nimbus.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    private Double amount;
    private LocalDateTime date;

    public Payment() {}
    public Payment(Student student, Double amount) { this.student = student; this.amount = amount; this.date = LocalDateTime.now(); }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}


/*
Java package: com.nimbus.dao
----------------------------------------
*/

// StudentDAO.java
package com.nimbus.dao;

import com.nimbus.model.Student;
import java.util.List;

public interface StudentDAO {
    Long save(Student student);
    void update(Student student);
    void delete(Student student);
    Student findById(Long id);
    List<Student> findAll();
}


// StudentDAOImpl.java
package com.nimbus.dao;

import com.nimbus.model.Student;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class StudentDAOImpl implements StudentDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() { return sessionFactory.getCurrentSession(); }

    @Override
    public Long save(Student student) {
        return (Long) currentSession().save(student);
    }

    @Override
    public void update(Student student) {
        currentSession().update(student);
    }

    @Override
    public void delete(Student student) {
        currentSession().delete(student);
    }

    @Override
    public Student findById(Long id) {
        return currentSession().get(Student.class, id);
    }

    @Override
    public List<Student> findAll() {
        return currentSession().createQuery("from Student", Student.class).list();
    }
}


// CourseDAO.java
package com.nimbus.dao;

import com.nimbus.model.Course;
import java.util.List;

public interface CourseDAO {
    Long save(Course course);
    Course findById(Long id);
    List<Course> findAll();
}


// CourseDAOImpl.java
package com.nimbus.dao;

import com.nimbus.model.Course;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class CourseDAOImpl implements CourseDAO {

    @Autowired
    private SessionFactory sessionFactory;

    private Session currentSession() { return sessionFactory.getCurrentSession(); }

    @Override
    public Long save(Course course) { return (Long) currentSession().save(course); }

    @Override
    public Course findById(Long id) { return currentSession().get(Course.class, id); }

    @Override
    public List<Course> findAll() { return currentSession().createQuery("from Course", Course.class).list(); }
}


// PaymentDAO.java (basic)
package com.nimbus.dao;

import com.nimbus.model.Payment;

public interface PaymentDAO {
    Long save(Payment payment);
}


// PaymentDAOImpl.java
package com.nimbus.dao;

import com.nimbus.model.Payment;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentDAOImpl implements PaymentDAO {
    @Autowired
    private SessionFactory sessionFactory;
    private Session currentSession() { return sessionFactory.getCurrentSession(); }

    @Override
    public Long save(Payment payment) { return (Long) currentSession().save(payment); }
}


/*
Java package: com.nimbus.service
----------------------------------------
*/

// FeeService.java
package com.nimbus.service;

public interface FeeService {
    void payFee(Long studentId, Double amount) throws Exception;
    void refundFee(Long studentId, Double amount) throws Exception;
}


// FeeServiceImpl.java
package com.nimbus.service;

import com.nimbus.dao.StudentDAO;
import com.nimbus.dao.PaymentDAO;
import com.nimbus.model.Student;
import com.nimbus.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeeServiceImpl implements FeeService {

    @Autowired
    private StudentDAO studentDAO;

    @Autowired
    private PaymentDAO paymentDAO;

    // Payment: decrease student's balance by amount and record payment. Atomic operation.
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payFee(Long studentId, Double amount) throws Exception {
        Student s = studentDAO.findById(studentId);
        if (s == null) throw new Exception("Student not found: " + studentId);
        if (amount == null || amount <= 0) throw new Exception("Invalid amount");

        // deduct
        double newBal = s.getBalance() - amount;
        s.setBalance(newBal);
        studentDAO.update(s);

        // record payment
        Payment p = new Payment(s, amount);
        paymentDAO.save(p);

        // simulate possible error to demonstrate rollback (comment out in production)
        // if (amount > 10000) throw new RuntimeException("Simulated failure");
    }

    // Refund: increase student's balance and record as negative payment or separate refund table
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refundFee(Long studentId, Double amount) throws Exception {
        Student s = studentDAO.findById(studentId);
        if (s == null) throw new Exception("Student not found: " + studentId);
        if (amount == null || amount <= 0) throw new Exception("Invalid amount");

        double newBal = s.getBalance() + amount;
        s.setBalance(newBal);
        studentDAO.update(s);

        // record refund as negative payment entry
        Payment p = new Payment(s, -Math.abs(amount));
        paymentDAO.save(p);
    }
}


/*
Java package: com.nimbus.config
----------------------------------------
*/

// AppConfig.java (Java-based Spring configuration)
package com.nimbus.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = "com.nimbus")
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        // Using H2 for simplicity. For MySQL use: com.mysql.cj.jdbc.Driver and jdbc:mysql://...
        ds.setDriverClassName("org.h2.Driver");
        ds.setUrl("jdbc:h2:~/nimbusdb;DB_CLOSE_DELAY=-1;DATABASE_TO_UPPER=false");
        ds.setUsername("sa");
        ds.setPassword("");
        return ds;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean lsfb = new LocalSessionFactoryBean();
        lsfb.setDataSource(dataSource());
        lsfb.setPackagesToScan("com.nimbus.model");
        Properties props = new Properties();
        props.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        props.put("hibernate.show_sql", "true");
        props.put("hibernate.hbm2ddl.auto", "update");
        lsfb.setHibernateProperties(props);
        return lsfb;
    }

    @Bean
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        HibernateTransactionManager txm = new HibernateTransactionManager();
        txm.setSessionFactory(sessionFactory);
        return txm;
    }
}


/*
Main class / Console controller
----------------------------------------
*/

// MainApp.java
package com.nimbus;

import com.nimbus.config.AppConfig;
import com.nimbus.dao.CourseDAO;
import com.nimbus.dao.StudentDAO;
import com.nimbus.model.Course;
import com.nimbus.model.Student;
import com.nimbus.service.FeeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class)) {
            StudentDAO studentDAO = ctx.getBean(StudentDAO.class);
            CourseDAO courseDAO = ctx.getBean(CourseDAO.class);
            FeeService feeService = ctx.getBean(FeeService.class);

            Scanner sc = new Scanner(System.in);
            boolean running = true;
            while (running) {
                System.out.println("\n=== Nimbus - Online Student Management ===");
                System.out.println("1. Add Course");
                System.out.println("2. Add Student");
                System.out.println("3. List Students");
                System.out.println("4. Pay Fee");
                System.out.println("5. Refund Fee");
                System.out.println("6. Update Student (name/course)");
                System.out.println("7. Delete Student");
                System.out.println("0. Exit");
                System.out.print("Choose: ");
                int ch = Integer.parseInt(sc.nextLine().trim());
                switch (ch) {
                    case 1 -> {
                        System.out.print("Course name: ");
                        String cname = sc.nextLine();
                        System.out.print("Duration: ");
                        int dur = Integer.parseInt(sc.nextLine());
                        Course c = new Course(cname, dur);
                        courseDAO.save(c);
                        System.out.println("Course saved: " + c);
                    }
                    case 2 -> {
                        System.out.print("Student name: ");
                        String sname = sc.nextLine();
                        List<Course> courses = courseDAO.findAll();
                        if (courses.isEmpty()) { System.out.println("No courses exist. Add a course first."); break; }
                        System.out.println("Choose course id from list: ");
                        courses.forEach(System.out::println);
                        Long cid = Long.parseLong(sc.nextLine());
                        Course chosen = courseDAO.findById(cid);
                        if (chosen == null) { System.out.println("Invalid course id"); break; }
                        System.out.print("Initial balance (0 if none): ");
                        double bal = Double.parseDouble(sc.nextLine());
                        Student s = new Student(sname, chosen, bal);
                        studentDAO.save(s);
                        System.out.println("Student added: " + s);
                    }
                    case 3 -> {
                        List<Student> students = studentDAO.findAll();
                        students.forEach(System.out::println);
                    }
                    case 4 -> {
                        System.out.print("Student id: ");
                        Long sid = Long.parseLong(sc.nextLine());
                        System.out.print("Amount to pay: ");
                        Double amt = Double.parseDouble(sc.nextLine());
                        try { feeService.payFee(sid, amt); System.out.println("Payment successful"); }
                        catch (Exception ex) { System.out.println("Payment failed: " + ex.getMessage()); }
                    }
                    case 5 -> {
                        System.out.print("Student id: ");
                        Long sid = Long.parseLong(sc.nextLine());
                        System.out.print("Refund amount: ");
                        Double amt = Double.parseDouble(sc.nextLine());
                        try { feeService.refundFee(sid, amt); System.out.println("Refund successful"); }
                        catch (Exception ex) { System.out.println("Refund failed: " + ex.getMessage()); }
                    }
                    case 6 -> {
                        System.out.print("Student id to update: ");
                        Long sid = Long.parseLong(sc.nextLine());
                        Student s = studentDAO.findById(sid);
                        if (s == null) { System.out.println("Not found"); break; }
                        System.out.print("New name (enter to skip): ");
                        String nn = sc.nextLine(); if (!nn.isBlank()) s.setName(nn);
                        System.out.print("Change course? (y/n): ");
                        if (sc.nextLine().equalsIgnoreCase("y")) {
                            List<Course> cs = courseDAO.findAll(); cs.forEach(System.out::println);
                            System.out.print("Course id: "); Long ncid = Long.parseLong(sc.nextLine());
                            s.setCourse(courseDAO.findById(ncid));
                        }
                        studentDAO.update(s);
                        System.out.println("Updated: " + s);
                    }
                    case 7 -> {
                        System.out.print("Student id to delete: ");
                        Long sid = Long.parseLong(sc.nextLine());
                        Student s = studentDAO.findById(sid);
                        if (s == null) { System.out.println("Not found"); break; }
                        studentDAO.delete(s);
                        System.out.println("Deleted: " + sid);
                    }
                    case 0 -> running = false;
                    default -> System.out.println("Invalid option");
                }
            }
            sc.close();
        }
    }
}


/*
SQL schema (MySQL) - place in README or run once
----------------------------------------
CREATE TABLE courses (
  course_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  course_name VARCHAR(255) NOT NULL,
  duration INT
);

CREATE TABLE students (
  student_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  course_id BIGINT,
  balance DOUBLE,
  FOREIGN KEY (course_id) REFERENCES courses(course_id)
);

CREATE TABLE payments (
  payment_id BIGINT AUTO_INCREMENT PRIMARY KEY,
  student_id BIGINT,
  amount DOUBLE,
  date DATETIME,
  FOREIGN KEY (student_id) REFERENCES students(student_id)
);


/*
README (Usage & Notes)
----------------------------------------
1. Import this scaffold as a Maven project in IntelliJ or Eclipse.
2. The provided AppConfig uses H2 embedded DB for convenience. To switch to MySQL:
   - Change dataSource() in AppConfig: set driver to com.mysql.cj.jdbc.Driver and URL to jdbc:mysql://localhost:3306/nimbus
   - Add MySQL credentials.
   - Set hibernate.dialect to org.hibernate.dialect.MySQL8Dialect
   - Optionally run the provided SQL schema in your MySQL server.
3. Run MainApp as a Java application. The console menu is interactive.
4. Transaction Management: FeeServiceImpl uses @Transactional to ensure atomicity. Any exception will rollback both student update and payment insert.
5. Logging: slf4j-simple prints to console. For production use Logback or Log4j2.
6. To add web UI: replace MainApp with Spring Boot + Spring MVC controllers and JSP/Thymeleaf or REST + frontend.


/*
Extra notes / testing tips
----------------------------------------
- For quick testing, pre-populate some courses in a JUnit test or in MainApp startup.
- Use Postman only if you add REST controllers.
- Validation: add javax.validation annotations on model fields and use Spring's Validator or @Valid in controllers.

End of scaffolding file.
