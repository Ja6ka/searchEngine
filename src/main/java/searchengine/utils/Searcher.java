package searchengine.utils;

import org.springframework.data.domain.Limit;
import searchengine.model.*;
import searchengine.model.search.Data;
import searchengine.services.SearchIndexService;
import searchengine.services.LemmaService;
import searchengine.services.PageIndexingService;
import searchengine.services.SiteIndexingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class Searcher {

    private SearchIndexService indexService;
    private PageIndexingService pageService;
    private SiteIndexingService siteService;
    private LemmaService lemmaService;
    private Lemmatizer lemmatizer;

    @Autowired
    public Searcher(SearchIndexService indexService, PageIndexingService pageService, SiteIndexingService siteService,
                    LemmaService lemmaService) throws IOException {
        this.indexService = indexService;
        this.pageService = pageService;
        this.siteService = siteService;
        this.lemmaService = lemmaService;
        this.lemmatizer = Lemmatizer.getInstance();
    }

    private int lemmaInInputCount;

    public ArrayList<Data> searchStringToDataArray(String searchInput, String siteURL) {
        ArrayList<Lemma> sortedArray = inputToLemmasSortedArray(searchInput, siteURL);
        ArrayList<Data> resultList = new ArrayList<>();
        if (sortedArray.isEmpty()) {
            System.out.println("Искомые слова не найдены!");
        } else {
            List<SearchIndex> leastFrequentLemmaIndexes = new ArrayList<>();
            HashSet<Integer> sitesSet = new HashSet<>();
            for (Lemma sortedLemma : sortedArray) {
                sitesSet.add(sortedLemma.getSite().getId());
            }
            sitesSet.forEach(siteID -> {
                List<Lemma> filteredList = sortedArray.stream().filter(lemma -> lemma.getSite().getId() == siteID).collect(Collectors.toList());
                Collections.sort(filteredList);

                if (filteredList.size() == lemmaInInputCount) {
                    List<SearchIndex> tempLemmaIndexes = findIndexesForSearchOutput(filteredList);
                    leastFrequentLemmaIndexes.addAll(tempLemmaIndexes);
                }

            });
            resultList = lemmaIndexesToData(leastFrequentLemmaIndexes, sortedArray);
        }
        return resultList;
    }

    private ArrayList<Data> lemmaIndexesToData(List<SearchIndex> leastFrequentLemmaIndexes, ArrayList<Lemma> sortedArray) {
        ArrayList<Data> resultList = new ArrayList<>();
        leastFrequentLemmaIndexes.forEach(lfl -> {
            Page page = pageService.findPage(lfl.getPageId()).get();
            Site site = siteService.findById(page.getSite().getId()).get();
            Lemma lemma = lemmaService.findLemma(lfl.getLemmaId()).get();
            Data data = new Data(site.getUrl(),
                    site.getName(),
                    page.getPath().substring(site.getUrl().length()),
                    page.getContent().substring(0, 51), //на этой строке передается название ссылки в поисковой выдаче
                    findPageSnippet(page.getContent(), lemma.getLemma()),
                    0);
            sortedArray.forEach(l -> {
                if (indexService.checkIfIndexExists(lfl.getPageId(), l.getId())) {
                    data.setRelevance(data.getRelevance() + indexService.findByPageIdAndLemmaId(lfl.getPageId(), l.getId()).getSearchIndexRank());
                }
            });
            resultList.add(data);
        });
        Collections.sort(resultList);
        return resultList;
    }

    private List<SearchIndex> findIndexesForSearchOutput(List<Lemma> lemmaList) {
        Lemma leastFrequentLemma = lemmaList.get(0);
        System.out.println(lemmaList);
        System.out.println(leastFrequentLemma);
        List<SearchIndex> leastFrequentLemmaIndexes = indexService.findAllByLemmaId(leastFrequentLemma.getId(), Limit.of(500));
        List<SearchIndex> leastFrequentLemmaIndexesToDeleteFrom = new ArrayList<>(leastFrequentLemmaIndexes);
        System.out.println(leastFrequentLemmaIndexes);
        for (int i = 1; i < lemmaList.size(); i++) {
            if (!leastFrequentLemmaIndexesToDeleteFrom.isEmpty()) {
                int finalI = i;
                leastFrequentLemmaIndexes.forEach(lfl -> {
                    if (!indexService.checkIfIndexExists(lfl.getPageId(), lemmaList.get(finalI).getId()))
                    {
                        leastFrequentLemmaIndexesToDeleteFrom.remove(lfl);
                        System.out.println(leastFrequentLemmaIndexesToDeleteFrom.size());
                    }
                });
            }
            leastFrequentLemmaIndexes = new ArrayList<>(leastFrequentLemmaIndexesToDeleteFrom);
        }
        return leastFrequentLemmaIndexesToDeleteFrom;
    }

    public ArrayList<Lemma> inputToLemmasSortedArray(String input, String siteURL) {
        ArrayList<String> list = new ArrayList<>();
        Long totalPagesCount = pageService.countPages();
        try {
            list = lemmatizer.getBasicFormsFromString(input);
            lemmaInInputCount = list.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Lemma> lemmasSortedList = new ArrayList<>();
        list.forEach(e -> {
            Lemma lemma;
            List<Lemma> listFromDB = new ArrayList<>();
            if (!siteURL.isEmpty()) {
                Site site = siteService.findByName(siteURL);
                lemma = lemmaService.findByLemmaAndSiteId(e, site.getId());
                listFromDB.add(lemma);
            } else {
                listFromDB = lemmaService.findLemmaByName(e);
            }
            listFromDB.forEach(lemmaFromList -> {
                if (lemmaFromList != null) {
                    if (lemmaFromList.getFrequency() != 0)
                        lemmasSortedList.add(lemmaFromList);
                }
            });
        });
        Collections.sort(lemmasSortedList);
        return lemmasSortedList;
    }

    public String findPageTitle(String page) {
        int startTitle = page.indexOf("<title>");
        int endTitle = page.indexOf("</title>");
        if (startTitle == -1 || endTitle == -1) {
            return "";
        } else return page.substring(startTitle + 7, endTitle);
    }

    public String findPageSnippet(String pageContent, String leastFrequentLemma) {
        String plainText = lemmatizer.replaceAuxiliarySymbols(pageContent);
        String out = "!!!error";

        for (int i = 0; i < 5; i++) {
            if (leastFrequentLemma.length() > i) {
                String leastFrequentLemmaWithoutSuffix = leastFrequentLemma.substring(0, leastFrequentLemma.length() - i);
                int beginIndex = plainText.indexOf(leastFrequentLemmaWithoutSuffix);
                if (beginIndex != -1) {
                    int beginSnippet = beginIndex > 100 ? beginIndex - 100 : 0;
                    int endSnippet = Math.min(beginIndex + 100, plainText.length());
                    out = plainText.substring(beginSnippet, endSnippet).replaceAll(leastFrequentLemmaWithoutSuffix, "<b>" + leastFrequentLemmaWithoutSuffix + "</b>");
                    break;
                }
            }
        }
        return out;
    }
}