package models;

import java.util.Date;

/**
 * Computer entity
 */
public class Computer {

    public Long id;
    public String name;
    public Date introduced;
    public Date discontinued;
    public Company company;

    public Computer() {
    }

    public Computer(String name, Date introduced, Company company) {
        this.name = name;
        this.introduced = introduced;
        this.company = company;
    }

}
