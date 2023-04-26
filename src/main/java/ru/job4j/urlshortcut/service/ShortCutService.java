package ru.job4j.urlshortcut.service;

import ru.job4j.urlshortcut.model.*;

import java.util.*;

public interface ShortCutService {

    ShortCut save(ShortCut shortCut);

    void update(ShortCut shortCut);

    Collection<ShortCut> findAll();

    Optional<ShortCut> findByUrlLink(String url);

    Optional<ShortCut> findByLinkCode(String linkCode);

}
