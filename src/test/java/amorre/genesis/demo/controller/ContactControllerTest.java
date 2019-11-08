package amorre.genesis.demo.controller;

import amorre.genesis.demo.AbstractControllerTest;
import amorre.genesis.demo.dto.AddressDto;
import amorre.genesis.demo.dto.ContactDto;
import amorre.genesis.demo.exception.ResourceNotFoundException;
import amorre.genesis.demo.model.Address;
import amorre.genesis.demo.model.EmployeeType;
import amorre.genesis.demo.model.Enterprise;
import amorre.genesis.demo.repository.AddressRepository;
import amorre.genesis.demo.repository.ContactRepository;
import amorre.genesis.demo.repository.EnterpriseRepository;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Autowired
    ContactRepository contactRepository;

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
    @Transactional
    public void createContact() throws Exception {

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
        contactDto.setEnterpriseIds(Collections.singleton(enterprise.getId()));
        contactDto.setEmployeeType(EmployeeType.INTERN);

        mockMvc().perform(request(HttpMethod.POST, "/contacts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(contactDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                .andExpect(jsonPath("$.firstName").value("Ash"))
                .andExpect(jsonPath("$.address.country").value("PokeWorld"))
                .andExpect(jsonPath("$.address.city").value("PalletTown"))
                .andExpect(jsonPath("$.address.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.address.street").value("AshStreet"))
                .andExpect(jsonPath("$.address.number").value("32"))
                .andExpect(jsonPath("$.enterpriseIds").isNotEmpty())
                .andExpect(jsonPath("$.enterpriseIds[0]").value(enterprise.getId()))
                .andExpect(jsonPath("$.employeeType").value("INTERN"))
                .andExpect(jsonPath("$.vatNumber").isEmpty())
                ;
    }

    @Test
    @Transactional
    public void createContact_withVat() throws Exception {

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
        contactDto.setEnterpriseIds(Collections.singleton(enterprise.getId()));
        contactDto.setEmployeeType(EmployeeType.FREELANCE);
        contactDto.setVatNumber("VATNUMBER");

        mockMvc().perform(request(HttpMethod.POST, "/contacts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(contactDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                .andExpect(jsonPath("$.firstName").value("Ash"))
                .andExpect(jsonPath("$.address.country").value("PokeWorld"))
                .andExpect(jsonPath("$.address.city").value("PalletTown"))
                .andExpect(jsonPath("$.address.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.address.street").value("AshStreet"))
                .andExpect(jsonPath("$.address.number").value("32"))
                .andExpect(jsonPath("$.enterpriseIds").isNotEmpty())
                .andExpect(jsonPath("$.enterpriseIds[0]").value(enterprise.getId()))
                .andExpect(jsonPath("$.employeeType").value("FREELANCE"))
                .andExpect(jsonPath("$.vatNumber").value("VATNUMBER"))
        ;
    }

    @Test
    @Transactional
    public void updateContactInfos() throws Exception {

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
        contactDto.setEnterpriseIds(Collections.singleton(enterprise.getId()));
        contactDto.setEmployeeType(EmployeeType.INTERN);

        MvcResult result = mockMvc().perform(request(HttpMethod.POST, "/contacts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(contactDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                .andExpect(jsonPath("$.firstName").value("Ash"))
                .andExpect(jsonPath("$.address.country").value("PokeWorld"))
                .andExpect(jsonPath("$.address.city").value("PalletTown"))
                .andExpect(jsonPath("$.address.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.address.street").value("AshStreet"))
                .andExpect(jsonPath("$.address.number").value("32"))
                .andExpect(jsonPath("$.enterpriseIds").isNotEmpty())
                .andExpect(jsonPath("$.enterpriseIds[0]").value(enterprise.getId()))
                .andExpect(jsonPath("$.employeeType").value("INTERN"))
                .andExpect(jsonPath("$.vatNumber").isEmpty())
                .andReturn()
                ;

        contactDto = objectMapper().readValue(result.getResponse().getContentAsString(), ContactDto.class);

        Address address2 = new Address();
        address2.setId(UUID.randomUUID().toString())
                .setCity("Indigo")
                .setStreet("Challenge")
                .setNumber("2")
                .setCountry("PokeWorld")
                .setZipCode("GEN1");

        address2 = addressRepository.save(address2);

        contactDto.setAddress(AddressDto.from(address2));
        contactDto.setFirstName("Misty");
        contactDto.setLastName("Water");
        contactDto.setVatNumber("PSYDUCK");
        contactDto.setEmployeeType(EmployeeType.FREELANCE);

        mockMvc().perform(request(HttpMethod.PUT, "/contacts/" + contactDto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(contactDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.lastName").value("Water"))
                .andExpect(jsonPath("$.firstName").value("Misty"))
                .andExpect(jsonPath("$.address.country").value("PokeWorld"))
                .andExpect(jsonPath("$.address.city").value("Indigo"))
                .andExpect(jsonPath("$.address.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.address.street").value("Challenge"))
                .andExpect(jsonPath("$.address.number").value("2"))
                .andExpect(jsonPath("$.enterpriseIds").isNotEmpty())
                .andExpect(jsonPath("$.enterpriseIds[0]").value(enterprise.getId()))
                .andExpect(jsonPath("$.employeeType").value("FREELANCE"))
                .andExpect(jsonPath("$.vatNumber").value("PSYDUCK"))
        ;
    }

    @Test
    @Transactional
    public void updateEnterprise_replace() throws Exception {

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
        contactDto.setEnterpriseIds(Collections.singleton(enterprise.getId()));
        contactDto.setEmployeeType(EmployeeType.INTERN);

        MvcResult result = mockMvc().perform(request(HttpMethod.POST, "/contacts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(contactDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                .andExpect(jsonPath("$.firstName").value("Ash"))
                .andExpect(jsonPath("$.address.country").value("PokeWorld"))
                .andExpect(jsonPath("$.address.city").value("PalletTown"))
                .andExpect(jsonPath("$.address.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.address.street").value("AshStreet"))
                .andExpect(jsonPath("$.address.number").value("32"))
                .andExpect(jsonPath("$.enterpriseIds").isNotEmpty())
                .andExpect(jsonPath("$.enterpriseIds[0]").value(enterprise.getId()))
                .andExpect(jsonPath("$.employeeType").value("INTERN"))
                .andExpect(jsonPath("$.vatNumber").isEmpty())
                .andReturn()
        ;

        contactDto = objectMapper().readValue(result.getResponse().getContentAsString(), ContactDto.class);

        Address address2 = new Address();
        address2.setId(UUID.randomUUID().toString())
                .setCity("Indigo")
                .setStreet("Challenge")
                .setNumber("2")
                .setCountry("PokeWorld")
                .setZipCode("GEN1");

        address2 = addressRepository.save(address2);

        // create enterprise
        Enterprise enterprise2 = new Enterprise();
        enterprise2.setId(UUID.randomUUID().toString())
                .setHeadOffice(address2)
                .setVatNumber("VAT")
                .setAddresses(Collections.singleton(address2));

        enterprise2 = enterpriseRepository.save(enterprise2);

        contactDto.setEnterpriseIds(Collections.singleton(enterprise2.getId()));

        mockMvc().perform(request(HttpMethod.PUT, "/contacts/" + contactDto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(contactDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                .andExpect(jsonPath("$.firstName").value("Ash"))
                .andExpect(jsonPath("$.address.country").value("PokeWorld"))
                .andExpect(jsonPath("$.address.city").value("PalletTown"))
                .andExpect(jsonPath("$.address.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.address.street").value("AshStreet"))
                .andExpect(jsonPath("$.address.number").value("32"))
                .andExpect(jsonPath("$.enterpriseIds").isNotEmpty())
                .andExpect(jsonPath("$.enterpriseIds[0]").value(enterprise2.getId()))
                .andExpect(jsonPath("$.employeeType").value("INTERN"))
                .andExpect(jsonPath("$.vatNumber").isEmpty())
        ;
    }

    @Test
    @Transactional
    public void updateEnterprise_add() throws Exception {

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
        contactDto.setEnterpriseIds(new HashSet<>());
        contactDto.getEnterpriseIds().add(enterprise.getId());
        contactDto.setEmployeeType(EmployeeType.INTERN);

        MvcResult result = mockMvc().perform(request(HttpMethod.POST, "/contacts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(contactDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                .andExpect(jsonPath("$.firstName").value("Ash"))
                .andExpect(jsonPath("$.address.country").value("PokeWorld"))
                .andExpect(jsonPath("$.address.city").value("PalletTown"))
                .andExpect(jsonPath("$.address.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.address.street").value("AshStreet"))
                .andExpect(jsonPath("$.address.number").value("32"))
                .andExpect(jsonPath("$.enterpriseIds").isNotEmpty())
                .andExpect(jsonPath("$.enterpriseIds[0]").value(enterprise.getId()))
                .andExpect(jsonPath("$.employeeType").value("INTERN"))
                .andExpect(jsonPath("$.vatNumber").isEmpty())
                .andReturn()
        ;

        contactDto = objectMapper().readValue(result.getResponse().getContentAsString(), ContactDto.class);

        Address address2 = new Address();
        address2.setId(UUID.randomUUID().toString())
                .setCity("Indigo")
                .setStreet("Challenge")
                .setNumber("2")
                .setCountry("PokeWorld")
                .setZipCode("GEN1");

        address2 = addressRepository.save(address2);

        // create enterprise
        Enterprise enterprise2 = new Enterprise();
        enterprise2.setId(UUID.randomUUID().toString())
                .setHeadOffice(address2)
                .setVatNumber("VAT")
                .setAddresses(Collections.singleton(address2));

        enterprise2 = enterpriseRepository.save(enterprise2);

        contactDto.getEnterpriseIds().add(enterprise2.getId());

        mockMvc().perform(request(HttpMethod.PUT, "/contacts/" + contactDto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(contactDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                .andExpect(jsonPath("$.firstName").value("Ash"))
                .andExpect(jsonPath("$.address.country").value("PokeWorld"))
                .andExpect(jsonPath("$.address.city").value("PalletTown"))
                .andExpect(jsonPath("$.address.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.address.street").value("AshStreet"))
                .andExpect(jsonPath("$.address.number").value("32"))
                .andExpect(jsonPath("$.enterpriseIds").isNotEmpty())
                .andExpect(jsonPath("$.enterpriseIds.*").value(Matchers.containsInAnyOrder(enterprise.getId(), enterprise2.getId())))
                .andExpect(jsonPath("$.employeeType").value("INTERN"))
                .andExpect(jsonPath("$.vatNumber").isEmpty())
        ;
    }

    @Test
    @Transactional
    public void deleteContact() throws Exception {

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
        contactDto.setEnterpriseIds(Collections.singleton(enterprise.getId()));
        contactDto.setEmployeeType(EmployeeType.INTERN);

        MvcResult result = mockMvc().perform(request(HttpMethod.POST, "/contacts")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(contactDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.lastName").value("Ketchum"))
                .andExpect(jsonPath("$.firstName").value("Ash"))
                .andExpect(jsonPath("$.address.country").value("PokeWorld"))
                .andExpect(jsonPath("$.address.city").value("PalletTown"))
                .andExpect(jsonPath("$.address.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.address.street").value("AshStreet"))
                .andExpect(jsonPath("$.address.number").value("32"))
                .andExpect(jsonPath("$.enterpriseIds").isNotEmpty())
                .andExpect(jsonPath("$.enterpriseIds[0]").value(enterprise.getId()))
                .andExpect(jsonPath("$.employeeType").value("INTERN"))
                .andExpect(jsonPath("$.vatNumber").isEmpty())
                .andReturn()
        ;

        contactDto = objectMapper().readValue(result.getResponse().getContentAsString(), ContactDto.class);

        mockMvc().perform(request(HttpMethod.DELETE, "/contacts/" + contactDto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().is(OK.value()));

        assertThat(contactRepository.findAll()).isEmpty();

        Enterprise enterpriseModified = enterpriseRepository.findById(enterprise.getId()).orElseThrow(ResourceNotFoundException::new);

        assertThat(enterpriseModified.getContacts()).isEmpty();
    }
}
