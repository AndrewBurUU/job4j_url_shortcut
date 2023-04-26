package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.model.*;
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

    public Collection<String> getStatistic() {
        Collection<ShortCut> shortCuts = this.findAll();
        Collection<String> res = new ArrayList<>();
        for (ShortCut shortCut: shortCuts) {
            res.add(String.format("{url : %s, total : %s}", shortCut.getUrlLink(), shortCut.getCallCounter()));
        }
        return res;
    }

    @Transactional
    public void callCounterUp(ShortCut shortCut) {
        shortCut.setCallCounter(shortCut.getCallCounter() + 1);
        this.update(shortCut);
    }
}
