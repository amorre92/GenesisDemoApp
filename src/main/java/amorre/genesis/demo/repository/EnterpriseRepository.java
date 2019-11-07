package amorre.genesis.demo.repository;

import amorre.genesis.demo.model.Enterprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Anthony Morre
 */
@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, String> {
}
