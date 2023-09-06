package crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashSet;

public class YandexCrawler {
    public static final String agent = "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 YaBrowser/23.5.1.725 (corp) Yowser/2.5 Safari/537.36";
    public static final String YANDEX_CSS_QUERY = "a[href]:not(.HeaderDesktopNavigation-Tab):not(.a11y-fast-nav__link):not(.HeaderDesktopLogo):not(.HeaderDesktopActions-Item):not(.HeaderDesktopLink):not(.SerpFooter-Link):not(.Thumb):not(.Related-Button):not(.UniSearchHeader-Title):not(.LinkMore-Link)";
    private final HashSet links;
    public static final int MILLIS = 6000;
    private static final int MAX_DEPTH = 2;
    public YandexCrawler() {
        this.links = new HashSet<>();
    }

    public void getPageLinks(String myURL, int depth) {
        if (!links.contains(myURL) && (depth < MAX_DEPTH)) {
            System.out.println(">> Depth: " + depth + " [" + myURL + "]");
            try {
                links.add(myURL);

                Document document = Jsoup.connect(myURL)
                        .userAgent(agent)
                        .ignoreContentType(true)
                        .timeout(MILLIS)
                        .get();
                Elements linksOnPage = document.select(YANDEX_CSS_QUERY);

                depth++;
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"), depth);
                }
            } catch (IOException e) {
                System.err.println("For '" + myURL + "': " + e.getMessage());
            }
        }
    }
}
