package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.HashSet;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Configuration configuration = new Configuration();
        //Initialize the configuration object
        //with the configuration file data
        configuration.configure("hibernate.cfg.xml");
        // Get the SessionFactory object from configuration.
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        Session session=sessionFactory.openSession();
        Transaction tx =session.beginTransaction();


        Student student = new Student();

        Set<Subject> subjectSet = new HashSet<>();
        subjectSet.add(new Subject("math"));
        subjectSet.add(new Subject("Science"));


        //Setting the object properties.
        student.setFirstName("Vivek");
        student.setLastName("Solenki");
        student.setClassName("MCA final");
        student.setRollNo("MCA/07/70");
        student.setAge(27);
        student.setSubjects(subjectSet);
        session.save(student);
        tx.commit();
        session.close();


    }
}