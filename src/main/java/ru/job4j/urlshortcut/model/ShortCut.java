package ru.job4j.urlshortcut.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import ru.job4j.urlshortcut.validate.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "shortcut")
public class ShortCut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private int id;

    @NotBlank(message = "Url must be not empty")
    @Column(name = "url_link")
    private String urlLink;

    @NotBlank(message = "Code must be not empty")
    @Column(name = "link_code")
    private String linkCode;

    @Column(name = "call_counter")
    private int callCounter;

}
