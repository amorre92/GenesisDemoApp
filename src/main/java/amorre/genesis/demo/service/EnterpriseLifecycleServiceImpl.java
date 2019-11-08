package amorre.genesis.demo.service;

import amorre.genesis.demo.dto.AddressDto;
import amorre.genesis.demo.dto.EnterpriseDto;
import amorre.genesis.demo.exception.ResourceNotFoundException;
import amorre.genesis.demo.model.Address;
import amorre.genesis.demo.model.Enterprise;
import amorre.genesis.demo.repository.ContactRepository;
import amorre.genesis.demo.repository.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Anthony Morre
 */
@Service
public class EnterpriseLifecycleServiceImpl implements EnterpriseLifecycleService {

    private final EnterpriseRepository enterpriseRepository;
    private final ContactRepository contactRepository;
    private final AddressLifecycleService addressLifecycleService;

    @Autowired
    public EnterpriseLifecycleServiceImpl(EnterpriseRepository enterpriseRepository, ContactRepository contactRepository,
                                          AddressLifecycleService addressLifecycleService) {
        this.enterpriseRepository = enterpriseRepository;
        this.contactRepository = contactRepository;
        this.addressLifecycleService = addressLifecycleService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enterprise createEnterprise(EnterpriseDto enterpriseDto) {
        validateDto(enterpriseDto);

        Enterprise enterprise = new Enterprise();
        enterprise.setId(UUID.randomUUID().toString());
        setFieldsFromDto(enterprise, enterpriseDto);

        return enterpriseRepository.save(enterprise);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enterprise updateEnterprise(EnterpriseDto enterpriseDto) {
        validateDto(enterpriseDto);

        Enterprise enterprise = enterpriseRepository.findById(enterpriseDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("cannot find enterprise with id {0}", enterpriseDto.getId()));
        setFieldsFromDto(enterprise, enterpriseDto);

        return enterpriseRepository.save(enterprise);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Enterprise addAddressToEnterprise(String enterpriseId, AddressDto addressDto, boolean replaceHeadOffice) {
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId).orElseThrow(() -> new ResourceNotFoundException("cannot find enterprise with id {0}", enterpriseId));

        Address address = addressLifecycleService.createOrUpdate(addressDto);

        enterprise.getAddresses().add(address);
        if (replaceHeadOffice) enterprise.setHeadOffice(address);

        return enterpriseRepository.save(enterprise);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteEnterprise(String enterpriseId) {
        enterpriseRepository.deleteById(enterpriseId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateDto(EnterpriseDto enterpriseDto) {
        Assert.notNull(enterpriseDto.getVatNumber(), "vat number cannot be null");
        Assert.notNull(enterpriseDto.getHeadOffice(), "head office cannot be null");
        Assert.notNull(enterpriseDto.getAddresses(), "there must be addresses for an enterprise");
        Assert.notEmpty(enterpriseDto.getAddresses(), "an enterprise must at least have one address (head office)");
    }

    private void setFieldsFromDto(Enterprise enterprise, EnterpriseDto enterpriseDto) {
        enterprise
                .setAddresses(updateOrCreateAddresses(enterpriseDto.getAddresses()))
                .setHeadOffice(addressLifecycleService.createOrUpdate(enterpriseDto.getHeadOffice()))
                .setVatNumber(enterpriseDto.getVatNumber());

        // remove not used contacts
        enterprise.getContacts()
                .stream()
                .filter(c -> !enterpriseDto.getContacts().contains(c.getId()))
                .forEach(enterprise::removeContact);

        if (enterpriseDto.getContacts() != null && enterpriseDto.getContacts().size() >= 1) {
            contactRepository.findAllById(enterpriseDto.getContacts())
                    .stream()
                    .filter(c -> enterpriseDto.getContacts().contains(c.getId()) && !enterprise.getContacts().contains(c))
                    .forEach(enterprise::addContact);
        }
    }

    private Set<Address> updateOrCreateAddresses(Set<AddressDto> addressesDtos) {
        return addressesDtos.stream()
                .map(addressLifecycleService::createOrUpdate)
                .collect(Collectors.toSet());

    }
}
