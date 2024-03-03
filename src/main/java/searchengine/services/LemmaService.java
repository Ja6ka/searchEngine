package searchengine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import searchengine.model.Lemma;
import searchengine.model.Site;
import searchengine.repositories.LemmaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LemmaService {
    private final LemmaRepository lemmaRepository;

    @Autowired
    public LemmaService(LemmaRepository lemmaRepository) {
        this.lemmaRepository = lemmaRepository;
    }

    public void saveLemmaToDB(int frequency, String lemma, Site site) {
        if (lemmaRepository.existsByLemma(lemma)) {
            lemmaRepository.getByLemma(lemma).setFrequency(lemmaRepository.getByLemma(lemma).getFrequency() + frequency);
        } else {
            Lemma newLemma = new Lemma();
            newLemma.setFrequency(frequency);
            newLemma.setLemma(lemma);
            newLemma.setSite(site);
            lemmaRepository.save(newLemma);
        }
    }

    public Optional<Lemma> findLemma(long id) {
        return lemmaRepository.findById(id);
    }

    public Lemma findByLemmaAndSiteId(String lemma, int siteID) {
        return lemmaRepository.findByLemmaAndSiteId(lemma, siteID);
    }

    public List<Lemma> findLemmaByName(String lemma) {
        return lemmaRepository.findByLemma(lemma);
    }
}
