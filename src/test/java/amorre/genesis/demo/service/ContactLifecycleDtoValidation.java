package amorre.genesis.demo.service;

import amorre.genesis.demo.dto.AddressDto;
import amorre.genesis.demo.dto.ContactDto;
import amorre.genesis.demo.model.Address;
import amorre.genesis.demo.model.EmployeeType;
import amorre.genesis.demo.repository.ContactRepository;
import amorre.genesis.demo.repository.EnterpriseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Anthony Morre
 */
@RunWith(MockitoJUnitRunner.class)
public class ContactLifecycleDtoValidation {

    @Mock
    EnterpriseRepository enterpriseRepository;
    @Mock
    ContactRepository contactRepository;
    @Mock
    AddressLifecycleService addressLifecycleService;

    private ContactLifecycleService contactLifecycleService;

    @Before
    public void init() {
        contactLifecycleService = new ContactLifecycleServiceImpl(contactRepository, enterpriseRepository, addressLifecycleService);
    }

    @Test
    public void testOk() {
        assertThatCode(
                () -> contactLifecycleService.createContact(validDto())).doesNotThrowAnyException();
    }

    @Test
    public void testNoLastName() {
        ContactDto contactDto = validDto();
        contactDto.setLastName(null);
        assertThatIllegalArgumentException().isThrownBy(
                () -> contactLifecycleService.createContact(contactDto)).withMessage("lastName cannot be null");
    }

    @Test
    public void testNoFirstName() {
        ContactDto contactDto = validDto();
        contactDto.setFirstName(null);
        assertThatIllegalArgumentException().isThrownBy(
                () -> contactLifecycleService.createContact(contactDto)).withMessage("firstName cannot be null");
    }

    @Test
    public void testNoAddress() {
        ContactDto contactDto = validDto();
        contactDto.setAddress(null);
        assertThatIllegalArgumentException().isThrownBy(
                () -> contactLifecycleService.createContact(contactDto)).withMessage("address cannot be null");
    }

    @Test
    public void testNoEnterprises() {
        ContactDto contactDto = validDto();
        contactDto.setEnterpriseIds(null);
        assertThatIllegalArgumentException().isThrownBy(
                () -> contactLifecycleService.createContact(contactDto)).withMessage("a contact must have a link to enterprise(s)");
    }

    @Test
    public void testNoEnterprises_notNull() {
        ContactDto contactDto = validDto();
        contactDto.setEnterpriseIds(new HashSet<>());
        assertThatIllegalArgumentException().isThrownBy(
                () -> contactLifecycleService.createContact(contactDto)).withMessage("a contact must have at least one enterprise");
    }

    @Test
    public void testNoVatNumber() {
        ContactDto contactDto = validDto();
        contactDto.setEmployeeType(EmployeeType.FREELANCE);
        assertThatIllegalArgumentException().isThrownBy(
                () -> contactLifecycleService.createContact(contactDto)).withMessage("vat number cannot be null for a freelance contact");
    }

    private ContactDto validDto() {
        Address address1 = new Address()
                .setCity("PalletTown")
                .setStreet("AshStreet")
                .setNumber("32")
                .setCountry("PokeWorld")
                .setZipCode("GEN1");

        ContactDto contactDto = new ContactDto();
        contactDto.setLastName("Ketchum");
        contactDto.setFirstName("Ash");
        contactDto.setAddress(AddressDto.from(address1));
        contactDto.setEnterpriseIds(Collections.singleton("enterpriseId"));
        contactDto.setEmployeeType(EmployeeType.INTERN);
        return contactDto;
    }
}
