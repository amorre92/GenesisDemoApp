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
    Set<Address> addresses = new HashSet<>();

    @JoinTable(
            name = "working_relation",
            joinColumns = @JoinColumn(name = "enterprise_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "contact_id", referencedColumnName = "id", nullable = false)
    )
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    Set<Contact> contacts = new HashSet<>();

    public void addContact(Contact contact) {
        this.contacts.add(contact);
        contact.getEnterprises().add(this);
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
        contact.getEnterprises().remove(this);
    }

    public void removeAllContacts() {
        for (Contact c : this.contacts) {
            c.getEnterprises().remove(this);
        }
        contacts.clear();
    }
}
