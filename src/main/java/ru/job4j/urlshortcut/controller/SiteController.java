package ru.job4j.urlshortcut.controller;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.validation.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.model.*;
import ru.job4j.urlshortcut.service.*;
import ru.job4j.urlshortcut.validate.Operation;

import java.lang.reflect.*;
import java.util.*;

import javax.validation.*;

@RestController
@RequestMapping("/site")
@AllArgsConstructor
public class SiteController {

    private final SpringSiteService siteService;
    private final SpringShortCutService shortCutService;
    private BCryptPasswordEncoder encoder;

    @GetMapping("/")
    public List<Site> findAll() {
        return this.siteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Site> findById(@PathVariable int id) {
        var site = this.siteService.findById(id);
        return new ResponseEntity<Site>(
                site.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Account is not found. Please, check requisites."
                )),
                site.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    @Validated(Operation.OnCreate.class)
    public ResponseEntity<Site> create(@Valid @RequestBody Site site) {
        var password = site.getPassword();
        site.setPassword(encoder.encode(password));
        return new ResponseEntity<Site>(
                this.siteService.create(site),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@Valid @RequestBody Site site) {
        var password = site.getPassword();
        site.setPassword(encoder.encode(password));
        if (this.siteService.save(site)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (this.siteService.delete(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/password")
    public SiteDTO newPassword(@Valid @RequestBody SiteDTO site) throws InvocationTargetException, IllegalAccessException {
        return siteService.savePassport(site);
    }

    @PostMapping("/registration")
    public ResponseEntity<RegistrationDTO> registration(@RequestBody RegistrationDTO siteUrl) {
        return new ResponseEntity<RegistrationDTO>(
                siteService.registration(siteUrl),
                HttpStatus.OK
        );
    }

    @PostMapping("/convert")
    public ResponseEntity<ConvertDTO> convert(@RequestBody ConvertDTO shortCutUrl) {
        return new ResponseEntity<ConvertDTO>(
                shortCutService.convert(shortCutUrl),
                HttpStatus.OK
        );
    }

    @GetMapping("/redirect/{code}")
    public ResponseEntity<ShortCut> redirect(@PathVariable String code) {
        return shortCutService.redirect(code);
    }

    @GetMapping("/statistic")
    public Collection<StatisticDTO> getStatistic() {
        return shortCutService.getStatistic();
    }

}
