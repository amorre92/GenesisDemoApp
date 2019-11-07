package amorre.genesis.demo.controller;

import amorre.genesis.demo.AbstractControllerTest;
import amorre.genesis.demo.dto.AddressDto;
import amorre.genesis.demo.dto.ContactDto;
import amorre.genesis.demo.model.Address;
import amorre.genesis.demo.model.EmployeeType;
import amorre.genesis.demo.model.Enterprise;
import amorre.genesis.demo.repository.AddressRepository;
import amorre.genesis.demo.repository.EnterpriseRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.UUID;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Anthony Morre
 */
public class ContactControllerTest extends AbstractControllerTest {

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    AddressRepository addressRepository;

    private Address address;
    private Enterprise enterprise;

    @Before
    public void init() {
        // create address
        address = new Address();
        address.setId(UUID.randomUUID().toString())
                .setCity("PalletTown")
                .setStreet("ProfStreet")
                .setNumber("2")
                .setCountry("PokeWorld")
                .setZipCode("GEN1");

        address = addressRepository.save(address);

        // create enterprise
        enterprise = new Enterprise();
        enterprise.setId(UUID.randomUUID().toString())
                .setHeadOffice(address)
                .setVatNumber("VAT")
                .setAddresses(Collections.singleton(address));

        enterprise = enterpriseRepository.save(enterprise);
    }


    @Test
    public void createSimpleContact() throws Exception {

        address
                .setCity("PalletTown")
                .setStreet("AshStreet")
                .setNumber("32")
                .setCountry("PokeWorld")
                .setZipCode("GEN1");

        ContactDto contactDto = new ContactDto();
        contactDto.setLastName("Ketchum");
        contactDto.setFirstName("Ash");
        contactDto.setAddress(AddressDto.from(address));
        contactDto.setEnterpriseIds(Collections.singleton(enterprise.getId()));
        contactDto.setEmployeeType(EmployeeType.INTERN);

        mockMvc().perform(request(HttpMethod.POST, "/contacts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(contactDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                ;
    }
}
