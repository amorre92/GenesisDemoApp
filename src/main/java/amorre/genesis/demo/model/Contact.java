package amorre.genesis.demo.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Anthony Morre
 */
@Entity
@Accessors(chain = true)
@FieldDefaults(level= AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Contact {

    @Id
    String id;

    @ManyToOne
    Address address;

    @Column(nullable = false)
    String firstName;

    @Column(nullable = false)
    String lastName;

    @Enumerated(EnumType.STRING)
    EmployeeType employeeType;

    String vatNumber;

    @ManyToMany(mappedBy = "contacts", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    Set<Enterprise> enterprises = new HashSet<>();

    public void addEnterprise(Enterprise enterprise) {
        this.enterprises.add(enterprise);
        enterprise.getContacts().add(this);
    }

}
