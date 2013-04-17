package models;

import java.util.Date;

public class PurchasableComputer extends Computer {

    private static final long serialVersionUID = 1L;

    public Long id;
    public String name;
    public Date introduced;
    public Date discontinued;
    public Company company;

    public final int price;
    public int availability;

    public PurchasableComputer(Long id, String name, Date introduced, Date discontinued, Company company, int price,
            int availability) {
        this.id = id;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
        this.company = company;
        this.price = price;
        this.availability = availability;
    }

    public static PurchasableComputer create(Computer computer, int price, int availability) {
        return new PurchasableComputer(computer.id, computer.name, computer.introduced, computer.discontinued,
                computer.company, price, availability);
    }

}
