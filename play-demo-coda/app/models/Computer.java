package models;

import java.util.*;

import javax.persistence.*;

import play.db.ebean.*;
import play.data.format.*;
import play.data.validation.*;

import com.avaje.ebean.*;

/**
 * Computer entity managed by Ebean
 */
@Entity 
@SuppressWarnings("serial")
public class Computer extends Model {

    @Id
    public Long id;
    
    @Constraints.Required
    public String name;
    
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date introduced;
    
    @Formats.DateTime(pattern="yyyy-MM-dd")
    public Date discontinued;
    
    @ManyToOne
    public Company company;
    
    public Computer() {
    }
    
    public Computer(String name, Date introduced, Company company) {
        this.name = name;
        this.introduced = introduced;
        this.company = company;
    }
    
    public static Finder<Long,Computer> find =
            new Finder<Long,Computer>(Long.class, Computer.class); 

    public static Page<Computer> page(int page, int pageSize,
            String sortBy,String order, String filter) {
        return find.where()
                .ilike("name", "%" + filter + "%")
                .orderBy(sortBy + " " + order)
                .fetch("company")
                .findPagingList(pageSize)
                .getPage(page);
    }
    
}

