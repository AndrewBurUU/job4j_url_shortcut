package ru.job4j.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.model.Site;

import java.util.*;

public interface SiteRepository extends CrudRepository<Site, Integer> {

    List<Site> findAll();

    Site findByLogin(String login);

    Site findByUrlAddress(String urlAddress);
}
