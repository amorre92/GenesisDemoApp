package amorre.genesis.demo.dto;

import amorre.genesis.demo.model.Contact;
import amorre.genesis.demo.model.EmployeeType;
import amorre.genesis.demo.model.Enterprise;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Anthony Morre
 */
public class ContactDto {

    private String id;
    private AddressDto address;
    private String firstName;
    private String lastName;
    private EmployeeType employeeType;
    private String vatNumber;
    private Set<String> enterpriseIds;

    public static ContactDto from(Contact contact) {
        ContactDto o = new ContactDto();
        o.setId(contact.getId());
        o.setAddress(AddressDto.from(contact.getAddress()));
        o.setFirstName(contact.getFirstName());
        o.setLastName(contact.getLastName());
        o.setEmployeeType(contact.getEmployeeType());
        o.setVatNumber(contact.getVatNumber());
        o.setEnterpriseIds(contact.getEnterprises().stream().map(Enterprise::getId).collect(Collectors.toSet()));
        return o;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AddressDto getAddress() {
        return address;
    }

    public void setAddress(AddressDto address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public EmployeeType getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(EmployeeType employeeType) {
        this.employeeType = employeeType;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Set<String> getEnterpriseIds() {
        return enterpriseIds;
    }

    public void setEnterpriseIds(Set<String> enterpriseIds) {
        this.enterpriseIds = enterpriseIds;
    }
}
