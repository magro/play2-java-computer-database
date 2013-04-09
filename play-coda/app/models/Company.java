package models;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Company entity
 */
public class Company {

    public Long id;
    public String name;

    public Company(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    private static final List<Company> ALL = Arrays.asList(new Company(1l, "Mac"), new Company(2l, "Lenovo"));

    public static Map<String, String> options() {
        Map<String, String> options = new LinkedHashMap<String, String>();
        for (Company c : ALL) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }

}
