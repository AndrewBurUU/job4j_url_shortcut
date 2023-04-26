package ru.job4j.service;

import ru.job4j.model.*;

import java.util.*;

public interface ShortCutService {

    ShortCut save(ShortCut shortCut);

    void update(ShortCut shortCut);

    Collection<ShortCut> findAll();

    Optional<ShortCut> findById(int id);

    Optional<ShortCut> findByUrlLink(String url);

    Optional<ShortCut> findByLinkCode(String linkCode);

}
