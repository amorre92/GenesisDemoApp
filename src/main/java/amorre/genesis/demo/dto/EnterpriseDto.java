package amorre.genesis.demo.dto;

import amorre.genesis.demo.model.Contact;
import amorre.genesis.demo.model.Enterprise;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Anthony Morre
 */
public class EnterpriseDto {

    private String id;

    private String vatNumber;

    private AddressDto headOffice;

    private Set<AddressDto> addresses;

    private Set<String> contacts;

    public static EnterpriseDto from(Enterprise enterprise) {
        EnterpriseDto o = new EnterpriseDto();
        o.setId(enterprise.getId());
        o.setAddresses(enterprise.getAddresses().stream().map(AddressDto::from).collect(Collectors.toSet()));
        o.setHeadOffice(AddressDto.from(enterprise.getHeadOffice()));
        o.setVatNumber(enterprise.getVatNumber());
        o.setContacts(enterprise.getContacts().stream().map(Contact::getId).collect(Collectors.toSet()));
        return o;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public AddressDto getHeadOffice() {
        return headOffice;
    }

    public void setHeadOffice(AddressDto headOffice) {
        this.headOffice = headOffice;
    }

    public Set<AddressDto> getAddresses() {
        return addresses;
    }

    public void setAddresses(Set<AddressDto> addresses) {
        this.addresses = addresses;
    }

    public Set<String> getContacts() {
        return contacts;
    }

    public void setContacts(Set<String> contacts) {
        this.contacts = contacts;
    }
}
