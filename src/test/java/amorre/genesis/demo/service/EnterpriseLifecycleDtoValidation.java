package amorre.genesis.demo.service;

import amorre.genesis.demo.dto.AddressDto;
import amorre.genesis.demo.dto.EnterpriseDto;
import amorre.genesis.demo.model.Address;
import amorre.genesis.demo.repository.ContactRepository;
import amorre.genesis.demo.repository.EnterpriseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * @author Anthony Morre
 */
@RunWith(MockitoJUnitRunner.class)
public class EnterpriseLifecycleDtoValidation {

    @Mock
    EnterpriseRepository enterpriseRepository;
    @Mock
    ContactRepository contactRepository;
    @Mock
    AddressLifecycleService addressLifecycleService;

    private EnterpriseLifecycleService enterpriseLifecycleService;

    @Before
    public void init() {
        enterpriseLifecycleService = new EnterpriseLifecycleServiceImpl(enterpriseRepository, contactRepository, addressLifecycleService);
    }

    @Test
    public void testOk() {
        assertThatCode(
                () -> enterpriseLifecycleService.validateDto(validDto())).doesNotThrowAnyException();
    }

    @Test
    public void testNoVatNumber() {
        EnterpriseDto enterpriseDto = validDto();
        enterpriseDto.setVatNumber(null);
        assertThatIllegalArgumentException().isThrownBy(
                () -> enterpriseLifecycleService.validateDto(enterpriseDto)).withMessage("vat number cannot be null");
    }

    @Test
    public void testNoHeadOffice() {
        EnterpriseDto enterpriseDto = validDto();
        enterpriseDto.setHeadOffice(null);
        assertThatIllegalArgumentException().isThrownBy(
                () -> enterpriseLifecycleService.validateDto(enterpriseDto)).withMessage("head office cannot be null");
    }

    @Test
    public void testNoAddresses() {
        EnterpriseDto enterpriseDto = validDto();
        enterpriseDto.setAddresses(null);
        assertThatIllegalArgumentException().isThrownBy(
                () -> enterpriseLifecycleService.validateDto(enterpriseDto)).withMessage("there must be addresses for an enterprise");
    }

    @Test
    public void testEmptyAddresses() {
        EnterpriseDto enterpriseDto = validDto();
        enterpriseDto.setAddresses(new HashSet<>());
        assertThatIllegalArgumentException().isThrownBy(
                () -> enterpriseLifecycleService.validateDto(enterpriseDto)).withMessage("an enterprise must at least have one address (head office)");
    }

    private EnterpriseDto validDto() {
        AddressDto address1 = AddressDto.from(new Address()
                .setCity("PalletTown")
                .setStreet("AshStreet")
                .setNumber("32")
                .setCountry("PokeWorld")
                .setZipCode("GEN1"));

        EnterpriseDto enterpriseDto = new EnterpriseDto();
        enterpriseDto.setVatNumber("VAT");
        enterpriseDto.setHeadOffice(address1);
        enterpriseDto.setAddresses(new HashSet<>());
        enterpriseDto.getAddresses().add(address1);
        enterpriseDto.setContacts(new HashSet<>());
        enterpriseDto.getContacts().add("id-contact");

        return enterpriseDto;
    }
}
