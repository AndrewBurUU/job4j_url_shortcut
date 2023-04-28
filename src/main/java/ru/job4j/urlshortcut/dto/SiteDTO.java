package ru.job4j.urlshortcut.dto;

import lombok.*;
import javax.validation.constraints.*;
import ru.job4j.urlshortcut.validate.Operation;

@Data
public class SiteDTO {

    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private int id;

    @NotNull(message = "Password length must be more than 5 characters.")
    @Size(min = 6)
    private String password;
}
