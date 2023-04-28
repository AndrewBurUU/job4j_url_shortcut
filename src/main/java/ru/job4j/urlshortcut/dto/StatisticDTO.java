package ru.job4j.urlshortcut.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class StatisticDTO {

    private String url;

    private int total;
}
