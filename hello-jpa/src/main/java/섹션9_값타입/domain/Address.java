package 섹션9_값타입.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
// 임베디드 타입은 불변 객체로 만들자
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // 기본 생성자 필수
    public Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(zipcode, address.zipcode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, street, zipcode);
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getStreet() {
        return street;
    }

    // 불변
    private void setCity(String city) {
        this.city = city;
    }

    // 불변
    private void setStreet(String street) {
        this.street = street;
    }

    // 불변
    private void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
}
