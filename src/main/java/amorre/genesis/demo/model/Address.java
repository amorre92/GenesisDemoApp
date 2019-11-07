package amorre.genesis.demo.model;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

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
@Table(uniqueConstraints =
        @UniqueConstraint(columnNames = {"street", "number", "zipCode", "country"})
)
public class Address {

    @Id
    String id;

    @Column(nullable = false)
    String street;

    @Column(nullable = false)
    String number;

    @Column(nullable = false)
    String zipCode;

    @Column(nullable = false)
    String city;

    @Column(nullable = false)
    String country;

}
