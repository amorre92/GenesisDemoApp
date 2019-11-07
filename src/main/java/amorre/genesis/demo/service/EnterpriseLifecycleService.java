package amorre.genesis.demo.service;

import amorre.genesis.demo.dto.EnterpriseDto;
import amorre.genesis.demo.model.Enterprise;

/**
 * @author Anthony Morre
 */
public interface EnterpriseLifecycleService {

    /**
     * Method to create an enterprise
     *
     * @param enterpriseDto a valid enterprise, containing :
     *                      a vat number not null
     *                      a head office address not null
     *                      addresses with at least one address (head office address)
     * @return the created enterprise
     */
    Enterprise createEnterprise(EnterpriseDto enterpriseDto);

    /**
     * Method to update an enterprise
     *
     * @param enterpriseDto a valid enterprise, containing :
     *                      a vat number not null
     *                      a head office address not null
     *                      addresses with at least one address (head office address)
     * @return the updated enterprise
     */
    Enterprise updateEnterprise(EnterpriseDto enterpriseDto);

    /**
     * Method to delete an enterprise
     *
     * @param enterpriseId the id of the enterprise to delete
     */
    void deleteEnterprise(String enterpriseId);
}
