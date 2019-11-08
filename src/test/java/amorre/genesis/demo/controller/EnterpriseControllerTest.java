package amorre.genesis.demo.controller;

import amorre.genesis.demo.AbstractControllerTest;
import amorre.genesis.demo.dto.AddressDto;
import amorre.genesis.demo.dto.EnterpriseDto;
import amorre.genesis.demo.model.Address;
import amorre.genesis.demo.model.Contact;
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
public class EnterpriseControllerTest extends AbstractControllerTest {

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    ContactRepository contactRepository;

    private Contact contact;

    @Before
    public void init() {
        Address address = new Address();
        address.setId(UUID.randomUUID().toString())
                .setCity("Indigo")
                .setStreet("ProfStreet")
                .setNumber("2")
                .setCountry("PokeWorld")
                .setZipCode("GEN1");
        address = addressRepository.save(address);

        // create enterprise
        Enterprise enterprise = new Enterprise();
        enterprise.setId(UUID.randomUUID().toString())
                .setHeadOffice(address)
                .setVatNumber("VAT")
                .setAddresses(Collections.singleton(address));

        enterprise = enterpriseRepository.save(enterprise);


        contact = new Contact();
        contact.setId(UUID.randomUUID().toString())
                .setLastName("Ketchum")
                .setFirstName("Ash")
                .setEmployeeType(EmployeeType.INTERN)
                .setAddress(address)
                .getEnterprises().add(enterprise);

        contact = contactRepository.save(contact);
    }

    @Test
    @Transactional
    public void createEnterprise() throws Exception {
        AddressDto address1 = AddressDto.from(
                new Address()
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
        enterpriseDto.getContacts().add(contact.getId());

        mockMvc().perform(request(HttpMethod.POST, "/enterprises")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(enterpriseDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.vatNumber").value("VAT"))
                .andExpect(jsonPath("$.headOffice.country").value("PokeWorld"))
                .andExpect(jsonPath("$.headOffice.city").value("PalletTown"))
                .andExpect(jsonPath("$.headOffice.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.headOffice.street").value("AshStreet"))
                .andExpect(jsonPath("$.headOffice.number").value("32"))
                .andExpect(jsonPath("$.addresses").isNotEmpty())
                .andExpect(jsonPath("$.addresses[*].city").value(Matchers.containsInAnyOrder("PalletTown")))
                .andExpect(jsonPath("$.contacts").isNotEmpty())
                .andExpect(jsonPath("$.contacts[*]").value(Matchers.containsInAnyOrder(contact.getId())));

        assertThat(contact.getEnterprises()).hasSize(2);

    }

    @Test
    @Transactional
    public void updateEnterprise() throws Exception {
        AddressDto address1 = AddressDto.from(
                new Address()
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
        enterpriseDto.getContacts().add(contact.getId());

        MvcResult result = mockMvc().perform(request(HttpMethod.POST, "/enterprises")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(enterpriseDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.vatNumber").value("VAT"))
                .andExpect(jsonPath("$.headOffice.country").value("PokeWorld"))
                .andExpect(jsonPath("$.headOffice.city").value("PalletTown"))
                .andExpect(jsonPath("$.headOffice.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.headOffice.street").value("AshStreet"))
                .andExpect(jsonPath("$.headOffice.number").value("32"))
                .andExpect(jsonPath("$.addresses").isNotEmpty())
                .andExpect(jsonPath("$.addresses[*].city").value(Matchers.containsInAnyOrder("PalletTown")))
                .andExpect(jsonPath("$.contacts").isNotEmpty())
                .andExpect(jsonPath("$.contacts[*]").value(Matchers.containsInAnyOrder(contact.getId())))
                .andReturn();

        enterpriseDto = objectMapper().readValue(result.getResponse().getContentAsString(), EnterpriseDto.class);

        assertThat(contact.getEnterprises()).hasSize(2);

        AddressDto address2 = AddressDto.from(
                new Address()
                        .setCity("Indigo")
                        .setStreet("Challenge")
                        .setNumber("2")
                        .setCountry("PokeWorld")
                        .setZipCode("GEN1"));

        enterpriseDto.setContacts(new HashSet<>());
        enterpriseDto.setVatNumber("VAT2");
        enterpriseDto.setHeadOffice(address2);
        enterpriseDto.getAddresses().add(address2);

        mockMvc().perform(request(HttpMethod.PUT, "/enterprises/" + enterpriseDto.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(enterpriseDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.vatNumber").value("VAT2"))
                .andExpect(jsonPath("$.headOffice.country").value("PokeWorld"))
                .andExpect(jsonPath("$.headOffice.city").value("Indigo"))
                .andExpect(jsonPath("$.headOffice.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.headOffice.street").value("Challenge"))
                .andExpect(jsonPath("$.headOffice.number").value("2"))
                .andExpect(jsonPath("$.addresses").isNotEmpty())
                .andExpect(jsonPath("$.addresses[*].city").value(Matchers.containsInAnyOrder("PalletTown", "Indigo")))
                .andExpect(jsonPath("$.contacts").isEmpty())
                ;

        assertThat(contact.getEnterprises()).hasSize(1);
    }

    @Test
    @Transactional
    public void updateEnterprise_addAddress() throws Exception {
        AddressDto address1 = AddressDto.from(
                new Address()
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
        enterpriseDto.getContacts().add(contact.getId());

        MvcResult result = mockMvc().perform(request(HttpMethod.POST, "/enterprises")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(enterpriseDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.vatNumber").value("VAT"))
                .andExpect(jsonPath("$.headOffice.country").value("PokeWorld"))
                .andExpect(jsonPath("$.headOffice.city").value("PalletTown"))
                .andExpect(jsonPath("$.headOffice.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.headOffice.street").value("AshStreet"))
                .andExpect(jsonPath("$.headOffice.number").value("32"))
                .andExpect(jsonPath("$.addresses").isNotEmpty())
                .andExpect(jsonPath("$.addresses[*].city").value(Matchers.containsInAnyOrder("PalletTown")))
                .andExpect(jsonPath("$.contacts").isNotEmpty())
                .andExpect(jsonPath("$.contacts[*]").value(Matchers.containsInAnyOrder(contact.getId())))
                .andReturn();

        enterpriseDto = objectMapper().readValue(result.getResponse().getContentAsString(), EnterpriseDto.class);

        assertThat(contact.getEnterprises()).hasSize(2);

        AddressDto address2 = AddressDto.from(
                new Address()
                        .setCity("Indigo")
                        .setStreet("Challenge")
                        .setNumber("2")
                        .setCountry("PokeWorld")
                        .setZipCode("GEN1"));

        mockMvc().perform(request(HttpMethod.PUT, "/enterprises/" + enterpriseDto.getId() + "/addresses")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(address2)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.vatNumber").value("VAT"))
                .andExpect(jsonPath("$.headOffice.country").value("PokeWorld"))
                .andExpect(jsonPath("$.headOffice.city").value("PalletTown"))
                .andExpect(jsonPath("$.headOffice.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.headOffice.street").value("AshStreet"))
                .andExpect(jsonPath("$.headOffice.number").value("32"))
                .andExpect(jsonPath("$.addresses").isNotEmpty())
                .andExpect(jsonPath("$.addresses[*].city").value(Matchers.containsInAnyOrder("PalletTown", "Indigo")))
                .andExpect(jsonPath("$.contacts").isNotEmpty())
                .andExpect(jsonPath("$.contacts[*]").value(Matchers.containsInAnyOrder(contact.getId())))
        ;

        assertThat(contact.getEnterprises()).hasSize(2);
    }

    @Test
    @Transactional
    public void updateEnterprise_addAddress_replaceHeadOffice() throws Exception {
        AddressDto address1 = AddressDto.from(
                new Address()
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
        enterpriseDto.getContacts().add(contact.getId());

        MvcResult result = mockMvc().perform(request(HttpMethod.POST, "/enterprises")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(enterpriseDto)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.vatNumber").value("VAT"))
                .andExpect(jsonPath("$.headOffice.country").value("PokeWorld"))
                .andExpect(jsonPath("$.headOffice.city").value("PalletTown"))
                .andExpect(jsonPath("$.headOffice.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.headOffice.street").value("AshStreet"))
                .andExpect(jsonPath("$.headOffice.number").value("32"))
                .andExpect(jsonPath("$.addresses").isNotEmpty())
                .andExpect(jsonPath("$.addresses[*].city").value(Matchers.containsInAnyOrder("PalletTown")))
                .andExpect(jsonPath("$.contacts").isNotEmpty())
                .andExpect(jsonPath("$.contacts[*]").value(Matchers.containsInAnyOrder(contact.getId())))
                .andReturn();

        enterpriseDto = objectMapper().readValue(result.getResponse().getContentAsString(), EnterpriseDto.class);

        assertThat(contact.getEnterprises()).hasSize(2);

        AddressDto address2 = AddressDto.from(
                new Address()
                        .setCity("Indigo")
                        .setStreet("Challenge")
                        .setNumber("2")
                        .setCountry("PokeWorld")
                        .setZipCode("GEN1"));

        mockMvc().perform(request(HttpMethod.PUT, "/enterprises/" + enterpriseDto.getId() + "/addresses").param("replaceHeadOffice", "true")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper().writeValueAsString(address2)))
                .andDo(print())
                .andExpect(status().is(OK.value()))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.vatNumber").value("VAT"))
                .andExpect(jsonPath("$.headOffice.country").value("PokeWorld"))
                .andExpect(jsonPath("$.headOffice.city").value("Indigo"))
                .andExpect(jsonPath("$.headOffice.zipCode").value("GEN1"))
                .andExpect(jsonPath("$.headOffice.street").value("Challenge"))
                .andExpect(jsonPath("$.headOffice.number").value("2"))
                .andExpect(jsonPath("$.addresses").isNotEmpty())
                .andExpect(jsonPath("$.addresses[*].city").value(Matchers.containsInAnyOrder("PalletTown", "Indigo")))
                .andExpect(jsonPath("$.contacts").isNotEmpty())
                .andExpect(jsonPath("$.contacts[*]").value(Matchers.containsInAnyOrder(contact.getId())))
        ;

        assertThat(contact.getEnterprises()).hasSize(2);
    }
}
