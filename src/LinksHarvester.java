import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class LinksHarvester {

    public static void main(String[] args) {
        ArrayList<String> harvestedLinksArrayList = harvestLinks("https://bmwcats.com/bmw2");

        System.out.println(harvestedLinksArrayList);
    }

    public static ArrayList<String> harvestLinks(String pageUrl) {

        ArrayList<String> arrayListLinksFromPage = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect(pageUrl).get();
        } catch (IOException e) {
        }

        Elements linksFromPage = doc.select(".etk-serie-cell");

        // Elements linksFromPage = doc.select("a");

        for (Element element : linksFromPage) {
            arrayListLinksFromPage.add(element.attr("abs:href"));
        }

        return arrayListLinksFromPage;
    }


}
