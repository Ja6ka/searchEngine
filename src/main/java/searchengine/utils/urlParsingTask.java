package searchengine.utils;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import searchengine.services.SiteIndexingService;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveTask;

public class urlParsingTask extends RecursiveTask<TreeSet> {

    private final ConcurrentHashMap<String, String> pageMap;
    private final TreeSet<String> set;
    private final String siteURL;
    @Getter
    @Setter
    private static volatile boolean stopping = false;

    @Autowired
    public urlParsingTask(String siteURL, ConcurrentHashMap<String, String> pageMap, TreeSet<String> set) {
        this.siteURL = siteURL;
        this.pageMap = pageMap;
        this.set = set;
    }

    @Override
    protected TreeSet<String> compute() {
        try {
            System.out.println(siteURL);
            Thread.sleep(1500);
            String USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
            Document doc = Jsoup.connect(siteURL).ignoreContentType(true).ignoreHttpErrors(true)
                    .userAgent(USER_AGENT)
                    .referrer("http://www.google.com")
                    .get();
            pageMap.put(siteURL, doc.text());
            Elements links = doc.select("a[href*=/]");
            Set<urlParsingTask> urlParsingTasks = new HashSet<>();
            if (set.add(siteURL) && SiteIndexingService.isIndexing()) {
                for (Element element : links) {
                    String newLink = element.attr("abs:href");
                    if (newLink.endsWith("/")) {
                        newLink = newLink.substring(0, newLink.length() - 2);
                    }
                    if (!newLink.startsWith(siteURL) || newLink.contains("#")) {
                        continue;
                    }
                    Document newDoc = Jsoup.connect(newLink).ignoreContentType(true).ignoreHttpErrors(true)
                            .userAgent(USER_AGENT)
                            .referrer("http://www.google.com").get();
                    pageMap.put(newLink, newDoc.text());
                    urlParsingTask newUrlParsingTask = new urlParsingTask(newLink, pageMap, set);
                    if (!urlParsingTasks.contains(newUrlParsingTask)) {
                        newUrlParsingTask.fork();
                        urlParsingTasks.add(newUrlParsingTask);
                    }
                }
            }
            for (urlParsingTask urlParsingTask : urlParsingTasks) {
                urlParsingTask.join();
            }
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return set;
    }
}