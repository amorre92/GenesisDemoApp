package amorre.genesis.demo.repository;

import amorre.genesis.demo.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Anthony Morre
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, String> {

    Optional<Address> findByCityAndStreetAndNumberAndZipCodeAndCountry(String city, String street, String number, String zipCode, String country);
}
