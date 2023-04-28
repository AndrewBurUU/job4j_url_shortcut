package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.*;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.model.*;
import ru.job4j.urlshortcut.repository.*;

import javax.transaction.*;
import java.text.*;
import java.util.*;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class SpringSiteService implements UserDetailsService {

    private final SiteRepository siteRepository;
    private final SiteDTORepository siteDTORepository;
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
    public SiteDTO savePassport(SiteDTO site) {
        String password = site.getPassword();
        var siteOptional = siteDTORepository.findById(site.getId());
        if (siteOptional == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var siteRes = siteOptional.get();
        siteRes.setPassword(encoder.encode(password));
        var siteOriginal = findById(site.getId()).get();
        siteOriginal.setPassword(siteRes.getPassword());
        save(siteOriginal);
        return siteRes;
    }

    @Transactional
    public boolean delete(int id) {
        var res = siteRepository.findById(id);
        if (res.isPresent()) {
            siteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Site register(String url) {
        String password = generatePassword(8);
        Site site = new Site();
        site.setUrlAddress(url);
        site.setLogin(String.format("login%s", new SimpleDateFormat("ddMMyyhhmmss").format(new Date())));
        site.setPassword(encoder.encode(password));
        create(site);
        Site res = new Site();
        res.setUrlAddress(url);
        res.setLogin(String.format("login%s", new SimpleDateFormat("ddMMyyhhmmss").format(new Date())));
        res.setPassword(password);
        return res;
    }

    public RegistrationDTO registration(RegistrationDTO siteUrl) {
        var url = siteUrl.getUrlAddress();
        boolean isNew = false;
        Site site = findByUrlAddress(url);
        if (site == null) {
            site = register(url);
            isNew = true;
        }
        siteUrl.setUrlAddress(url);
        siteUrl.setRegistration(isNew);
        siteUrl.setLogin(site.getLogin());
        siteUrl.setPassword(site.getPassword());
        return  siteUrl;
    }

    public String generatePassword(int length) {
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
