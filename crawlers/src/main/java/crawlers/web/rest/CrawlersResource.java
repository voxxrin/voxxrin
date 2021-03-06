package crawlers.web.rest;

import crawlers.AbstractHttpCrawler;
import restx.annotations.GET;
import restx.annotations.RestxResource;
import restx.factory.Component;
import restx.security.PermitAll;

import java.util.Set;

@RestxResource
@Component
public class CrawlersResource {

    private final Set<AbstractHttpCrawler> crawlers;

    public CrawlersResource(Set<AbstractHttpCrawler> crawlers) {
        this.crawlers = crawlers;
    }

    @GET("/info")
    @PermitAll
    public Iterable<AbstractHttpCrawler> getAllRegisteredCrawlers() {
        return crawlers;
    }
}
