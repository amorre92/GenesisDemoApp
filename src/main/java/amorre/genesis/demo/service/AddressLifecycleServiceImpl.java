package amorre.genesis.demo.service;

import amorre.genesis.demo.dto.AddressDto;
import amorre.genesis.demo.exception.ResourceNotFoundException;
import amorre.genesis.demo.model.Address;
import amorre.genesis.demo.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author Anthony Morre
 */
@Service
public class AddressLifecycleServiceImpl implements AddressLifecycleService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressLifecycleServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Address createOrUpdate(AddressDto addressDto) {

        Address address;
        if (addressDto.getId() != null) {
            address = addressRepository
                    .findByCityAndStreetAndNumberAndZipCodeAndCountry(addressDto.getCountry(), addressDto.getStreet(), addressDto.getNumber(), addressDto.getZipCode(), addressDto.getCountry())
                    .orElseGet(() -> new Address().setId(UUID.randomUUID().toString()));
        } else {
            address = addressRepository.findById(addressDto.getId()).orElseThrow(() -> new ResourceNotFoundException("cannot find address with id {0}"));
        }

        return addressRepository.save(address);
    }
}
