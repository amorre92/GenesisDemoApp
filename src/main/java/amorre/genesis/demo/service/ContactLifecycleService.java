package amorre.genesis.demo.service;

import amorre.genesis.demo.dto.ContactDto;
import amorre.genesis.demo.model.Contact;

/**
 * @author Anthony Morre
 */
public interface ContactLifecycleService {

    /**
     * Method to create a contact
     *
     * @param contactDto a valid contact, containing :
     *                   address notNull
     *                   firstName notNull
     *                   lastName notNull
     *                   at least one enterprise
     *                   a vat number if freelance
     * @return the created contact
     */
    Contact createContact(ContactDto contactDto);

    /**
     * Method to update a contact
     *
     * @param contactDto a valid contact, containing :
     *                   address notNull
     *                   firstName notNull
     *                   lastName notNull
     *                   at least one enterprise
     *                   a vat number if freelance
     * @return the updated contact
     */
    Contact updateContact(ContactDto contactDto);

    /**
     * Method to delete a contact
     *
     * @param contactId the id of the contact to delete
     */
    void deleteContact(String contactId);

    /**
     * Validates a dto
     *
     * @param contactDto dto
     */
    void validateDto(ContactDto contactDto);
}
