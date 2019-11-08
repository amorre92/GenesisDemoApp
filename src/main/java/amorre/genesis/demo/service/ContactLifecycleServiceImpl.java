package amorre.genesis.demo.service;

import amorre.genesis.demo.dto.ContactDto;
import amorre.genesis.demo.exception.ResourceNotFoundException;
import amorre.genesis.demo.model.Contact;
import amorre.genesis.demo.model.EmployeeType;
import amorre.genesis.demo.repository.ContactRepository;
import amorre.genesis.demo.repository.EnterpriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * @author Anthony Morre
 */
@Service
public class ContactLifecycleServiceImpl implements ContactLifecycleService {

    private final ContactRepository contactRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final AddressLifecycleService addressLifecycleService;

    @Autowired
    public ContactLifecycleServiceImpl(ContactRepository contactRepository, EnterpriseRepository enterpriseRepository,
                                       AddressLifecycleService addressLifecycleService) {
        this.contactRepository = contactRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.addressLifecycleService = addressLifecycleService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact createContact(ContactDto contactDto) {

        validateDto(contactDto);

        Contact contact = new Contact();
        contact.setId(UUID.randomUUID().toString());
        setFieldsFromDto(contact, contactDto);

        return contactRepository.save(contact);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact updateContact(ContactDto contactDto) {

        validateDto(contactDto);

        Contact contact = contactRepository.findById(contactDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("cannot find contact with id {0}", contactDto.getId()));
        setFieldsFromDto(contact, contactDto);

        return contactRepository.save(contact);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteContact(String contactId) {
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new ResourceNotFoundException("cannot find contact with id {0}", contactId));

        contact.removeAllEnterprises();
        contactRepository.save(contact);
        contactRepository.delete(contact);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validateDto(ContactDto contactDto) {
        Assert.notNull(contactDto.getAddress(), "address cannot be null");
        Assert.notNull(contactDto.getFirstName(), "firstName cannot be null");
        Assert.notNull(contactDto.getLastName(), "lastName cannot be null");
        Assert.notNull(contactDto.getEnterpriseIds(), "a contact must have a link to enterprise(s)");
        Assert.isTrue(contactDto.getEnterpriseIds().size() >= 1, "a contact must have at least one enterprise");

        if (EmployeeType.FREELANCE.equals(contactDto.getEmployeeType()))
            Assert.notNull(contactDto.getVatNumber(), "vat number cannot be null for a freelance contact");
    }

    private void setFieldsFromDto(Contact contact, ContactDto contactDto) {
        contact
                .setAddress(addressLifecycleService.createOrUpdate(contactDto.getAddress()))
                .setEmployeeType(contactDto.getEmployeeType())
                .setVatNumber(contactDto.getVatNumber())
                .setFirstName(contactDto.getFirstName())
                .setLastName(contactDto.getLastName());


        // remove unused enterprises
        contact.getEnterprises()
                .stream()
                .filter(e -> !contactDto.getEnterpriseIds().contains(e.getId()))
                .forEach(contact::removeEnterprise);

        if (contactDto.getEnterpriseIds() != null && contactDto.getEnterpriseIds().size() >= 1) {

            // add new ones
            enterpriseRepository.findAllById(contactDto.getEnterpriseIds())
                    .stream()
                    .filter(e -> contactDto.getEnterpriseIds().contains(e.getId()) && !contact.getEnterprises().contains(e))
                    .forEach(contact::addEnterprise);
        }
    }
}
