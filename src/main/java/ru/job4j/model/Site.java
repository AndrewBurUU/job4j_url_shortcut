package ru.job4j.model;

import lombok.*;

import javax.persistence.*;

import java.util.Objects;
import javax.validation.constraints.*;

import ru.job4j.validate.*;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "site")
public class Site {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private int id;

    @NotBlank(message = "Url must be not empty")
    @Column(name = "url_address")
    private String urlAddress;

    @NotBlank(message = "Login must be not empty")
    private String login;

    @NotNull(message = "Password length must be more than 5 characters.")
    @Size(min = 6)
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public void setUrlAddress(String urlAddress) {
        this.urlAddress = urlAddress;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Site site = (Site) o;
        return Objects.equals(login, site.login)
                && Objects.equals(password, site.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, password);
    }

}