package ru.job4j.urlshortcut.repository;

import ru.job4j.urlshortcut.dto.*;

import java.util.*;

public interface SiteDTORepository extends SiteRepository {

    Optional<SiteDTO> findById(int id);
}
