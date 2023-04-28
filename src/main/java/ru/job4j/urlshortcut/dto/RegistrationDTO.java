package ru.job4j.urlshortcut.dto;

import lombok.*;
import javax.validation.constraints.*;

@Data
public class RegistrationDTO {

    @NotBlank(message = "Url must be not empty")
    private String urlAddress;

    private boolean registration;

    @NotBlank(message = "Login must be not empty")
    private String login;

    @NotNull(message = "Password length must be more than 5 characters.")
    @Size(min = 6)
    private String password;

}
