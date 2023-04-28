package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.*;
import ru.job4j.urlshortcut.model.*;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.repository.ShortCutRepository;

import javax.transaction.*;
import java.util.*;

@Service
@AllArgsConstructor
public class SpringShortCutService implements ShortCutService {

    private final ShortCutRepository shortCutRepository;

    @Override
    @Transactional
    public ShortCut save(ShortCut shortCut) {
        return shortCutRepository.save(shortCut);
    }

    @Override
    @Transactional
    public void update(ShortCut shortCut) {
        shortCutRepository.save(shortCut);
    }

    @Override
    public Collection<ShortCut> findAll() {
        return shortCutRepository.findAll();
    }

    @Override
    public Optional<ShortCut> findByLinkCode(String linkCode) {
        return shortCutRepository.findByLinkCode(linkCode);
    }

    @Override
    public Optional<ShortCut> findByUrlLink(String url) {
        return shortCutRepository.findByUrlLink(url);
    }

    @Transactional
    public ShortCut register(String url, int n) {
        String code = UUID.randomUUID().toString();
        code = code.substring(0, n);
        ShortCut shortCut = new ShortCut();
        shortCut.setUrlLink(url);
        shortCut.setLinkCode(code);
        return save(shortCut);
    }

    public Collection<StatisticDTO> getStatistic() {
        Collection<ShortCut> shortCuts = this.findAll();
        Collection<StatisticDTO> res = new ArrayList<>();
        for (ShortCut shortCut: shortCuts) {
            res.add(new StatisticDTO(shortCut.getUrlLink(), shortCut.getCallCounter()));
        }
        return res;
    }

    @Transactional
    public void callCounterUp(ShortCut shortCut) {
        shortCut.setCallCounter(shortCut.getCallCounter() + 1);
        this.update(shortCut);
    }

    public ConvertDTO convert(ConvertDTO convertDTO) {
        var url = convertDTO.getUrl();
        Optional<ShortCut> shortCut = findByUrlLink(url);
        if (shortCut.isEmpty()) {
            shortCut = Optional.of(register(url, 7));
        }
        convertDTO.setCode(shortCut.get().getLinkCode());
        return convertDTO;
    }

    public ResponseEntity<ShortCut> redirect(String code) {
        var shortCut = findByLinkCode(code);
        if (shortCut.isPresent()) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", shortCut.get().getUrlLink());
            callCounterUp(shortCut.get());
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        }
        return new ResponseEntity<ShortCut>(
                shortCut.orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "LinkCode is not found. Please, check code."
                )),
                HttpStatus.NOT_FOUND
        );
    }

}
