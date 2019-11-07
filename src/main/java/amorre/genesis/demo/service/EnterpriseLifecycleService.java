package amorre.genesis.demo.service;

import amorre.genesis.demo.dto.AddressDto;
import amorre.genesis.demo.dto.EnterpriseDto;
import amorre.genesis.demo.model.Enterprise;

/**
 * @author Anthony Morre
 */
public interface EnterpriseLifecycleService {

    /**
     * Method to create an enterprise
     *
     * if an address is not present, it will be added, otherwise it will be automatically updated
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
     * if an address is not present, it will be added, otherwise it will be automatically updated
     *
     * @param enterpriseDto a valid enterprise, containing :
     *                      a vat number not null
     *                      a head office address not null
     *                      addresses with at least one address (head office address)
     * @return the updated enterprise
     */
    Enterprise updateEnterprise(EnterpriseDto enterpriseDto);

    /**
     *
     * Add an address to en enterprise, and modify its head office if desired
     *
     * if an address is not present, it will be added, otherwise it will be automatically updated
     *
     * @param enterpriseId      the enterprise to modify
     * @param addressDto        the address to add
     * @param replaceHeadOffice true if needed to replace head office location
     * @return the modified Enterprise
     */
    Enterprise addAddressToEnterprise(String enterpriseId, AddressDto addressDto, boolean replaceHeadOffice);

    /**
     * Method to delete an enterprise
     *
     * @param enterpriseId the id of the enterprise to delete
     */
    void deleteEnterprise(String enterpriseId);
}
