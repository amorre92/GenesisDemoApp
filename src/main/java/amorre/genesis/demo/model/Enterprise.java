package amorre.genesis.demo.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Anthony Morre
 */
@Entity
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Enterprise {


    @Id
    String id;

    @Column(nullable = false)
    String vatNumber;

    @ManyToOne
    Address headOffice;

    @OneToMany
    Set<Address> addresses;

    @JoinTable(
            name = "working_relation",
            joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "contact_id", referencedColumnName = "id", nullable = false)
    )
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    Set<Contact> contacts;

    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.addEnterprise(this);
    }
}
