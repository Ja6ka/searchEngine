package searchengine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import searchengine.services.SiteIndexingService;

@Controller
public class DefaultController {

    private final SiteIndexingService siteIndexingService;

    @Autowired
    public DefaultController(SiteIndexingService siteIndexingService) {
        this.siteIndexingService = siteIndexingService;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}