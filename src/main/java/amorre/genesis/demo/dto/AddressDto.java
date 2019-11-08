package amorre.genesis.demo.dto;

import amorre.genesis.demo.model.Address;

/**
 * @author Anthony Morre
 */
public class AddressDto {

    private String id;
    private String number;
    private String street;
    private String zipCode;
    private String country;
    private String city;

    public static AddressDto from(Address address) {
        AddressDto o = new AddressDto();
        o.setId(address.getId());
        o.setNumber(address.getNumber());
        o.setStreet(address.getStreet());
        o.setZipCode(address.getZipCode());
        o.setCountry(address.getCountry());
        o.setCity(address.getCity());
        return o;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
