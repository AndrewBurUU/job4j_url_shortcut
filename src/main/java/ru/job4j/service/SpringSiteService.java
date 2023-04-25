package ru.job4j.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.stereotype.Service;
import ru.job4j.repository.SiteRepository;
import ru.job4j.model.Site;

import javax.transaction.*;
import java.text.*;
import java.util.*;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class SpringSiteService implements UserDetailsService {

    private final SiteRepository siteRepository;
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Site site = siteRepository.findByLogin(username);
        if (site == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(site.getLogin(), site.getPassword(), emptyList());
    }

    public List<Site> findAll() {
        return siteRepository.findAll();
    }

    public Optional<Site> findById(int id) {
        return siteRepository.findById(id);
    }

    public Site findByLogin(String login) {
        return siteRepository.findByLogin(login);
    }

    public Site findByUrlAddress(String urlAddress) {
        return siteRepository.findByUrlAddress(urlAddress);
    }

    @Transactional
    public Site create(Site site) {
        return siteRepository.save(site);
    }

    @Transactional
    public boolean save(Site site) {
        var res = siteRepository.findById(site.getId());
        if (res.isPresent()) {
            siteRepository.save(site);
            return true;
        }
        return false;
    }

    @Transactional
    public boolean delete(Site site) {
        var res = siteRepository.findById(site.getId());
        if (res.isPresent()) {
            siteRepository.delete(site);
            return true;
        }
        return false;
    }

    @Transactional
    public Site register(String url) {
        Site site = new Site();
        site.setUrlAddress(url);
        site.setLogin(encoder.encode(
                String.format("login%s", new SimpleDateFormat("ddMMyyhhmmss").format(new Date()))));
        site.setPassword(encoder.encode(generatePassword(8)));
        return create(site);
    }

    public static String generatePassword(int length) {
        String upperCase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowerCase = "abcdefghijklmnopqrstuvwxyz";
        String numbers = "0123456789";
        String symbols = "!@#$%^&*_=+-/";

        String allChars = upperCase + lowerCase + numbers + symbols;

        Random random = new Random();
        char[] password = new char[length];

        for (int i = 0; i < length; i++) {
            password[i] = allChars.charAt(random.nextInt(allChars.length()));
        }
        return new String(password);
    }
}
