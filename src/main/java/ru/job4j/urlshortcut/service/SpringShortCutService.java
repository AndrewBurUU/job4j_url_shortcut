package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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

    public int callCounterInc(int id) {
        return shortCutRepository.incrementCallCounter(id);
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

    public Optional<ShortCut> redirect(String code) {
        var shortCut = findByLinkCode(code);
        if (shortCut.isPresent()) {
            callCounterInc(shortCut.get().getId());
        }
        return shortCut;
    }

    /**
     @Transactional
     public void callCounterUp(ShortCut shortCut) {
     shortCut.setCallCounter(shortCut.getCallCounter() + 1);
     this.update(shortCut);
     }
     */

}
