package ru.job4j.urlshortcut.dto;

import lombok.*;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConvertDTO {

    @NotBlank(message = "Url must be not empty")
    private String url;

    private String code;
}
