package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.*;

import java.util.*;

public interface ShortCutRepository extends CrudRepository<ShortCut, Integer> {

    List<ShortCut> findAll();

    Optional<ShortCut> findByUrlLink(String urlLink);

    Optional<ShortCut> findByLinkCode(String linkCode);
}
