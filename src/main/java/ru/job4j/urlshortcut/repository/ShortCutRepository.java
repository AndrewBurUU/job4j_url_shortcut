package ru.job4j.urlshortcut.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.*;
import ru.job4j.urlshortcut.model.*;

import javax.transaction.*;
import java.util.*;

public interface ShortCutRepository extends CrudRepository<ShortCut, Integer> {

    List<ShortCut> findAll();

    Optional<ShortCut> findByUrlLink(String urlLink);

    Optional<ShortCut> findByLinkCode(String linkCode);

    @Transactional
    @Modifying
    @Query("UPDATE ShortCut s SET s.callCounter = s.callCounter + 1 WHERE s.id = :id")
    int incrementCallCounter(@Param("id") int id);
}
