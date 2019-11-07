package amorre.genesis.demo.service;

import amorre.genesis.demo.dto.AddressDto;
import amorre.genesis.demo.model.Address;

/**
 * @author Anthony Morre
 */
public interface AddressLifecycleService {

    /**
     * Method to create an address, or update if present
     *
     * @param addressDto the address
     * @return the created or updated address
     */
    Address createOrUpdate(AddressDto addressDto);
}
