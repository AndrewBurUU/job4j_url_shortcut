package ru.job4j.controller;

import lombok.*;
import org.springframework.http.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.validation.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.*;
import ru.job4j.service.*;
import ru.job4j.validate.Operation;

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
        Site site = new Site();
        site.setId(id);
        if (this.siteService.delete(site)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/password")
    public Site newPassword(@Valid @RequestBody Site site) throws InvocationTargetException, IllegalAccessException {
        String password = site.getPassword();
        var personOptional = siteService.findById(site.getId());
        if (personOptional == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var person = personOptional.get();
        person.setPassword(encoder.encode(password));
        siteService.save(person);
        return person;
    }

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@RequestBody Site siteUrl) {
        var url = siteUrl.getUrlAddress();
        boolean isNew = false;
        Site site = siteService.findByUrlAddress(url);
        if (site == null) {
            site = siteService.register(url);
            isNew = true;
        }
        return new ResponseEntity<String>(
                String.format("{registration = %b, login: %s, password: %s}",
                        isNew,
                        site.getLogin(),
                        site.getPassword()),
                HttpStatus.OK
        );
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convert(@RequestBody ShortCut shortCutUrl) {
        var url = shortCutUrl.getUrlLink();
        Optional<ShortCut> shortCut = shortCutService.findByUrlLink(url);
        if (shortCut.isEmpty()) {
            shortCut = Optional.of(shortCutService.register(url, 7));
        }
        return new ResponseEntity<String>(
                String.format("{code = %s}",
                        shortCut.get().getLinkCode()),
                HttpStatus.OK
        );
    }

    @GetMapping("/redirect/{code}")
    public ResponseEntity<ShortCut> redirect(@PathVariable String code) {
        var shortCut = shortCutService.findByLinkCode(code);
        if (shortCut.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", shortCut.get().getUrlLink());
            shortCutService.callCounterUp(shortCut.get());
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        return new ResponseEntity<ShortCut>(
                shortCut.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "LinkCode is not found. Please, check code."
                )),
                HttpStatus.NOT_FOUND
        );
    }

    @GetMapping("/statistic")
    public Collection<String> getStatistic() {
        return shortCutService.getStatistic();
    }

}
