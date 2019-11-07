package amorre.genesis.demo.repository;

import amorre.genesis.demo.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Anthony Morre
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
}
