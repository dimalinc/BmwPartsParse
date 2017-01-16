package Harvester;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class LinksHarvester {

    public static void main(String[] args) {
      //  ArrayList<String> harvestedLinksArrayList = harvestSubmodelLinks("https://bmwcats.com/bmw2");
      //  System.out.println(harvestedLinksArrayList);

         // ArrayList<String> harvestIspolnenieLinks = harvestIspolnenieLinks("https://bmwcats.com/bmw2");
         // System.out.println(harvestIspolnenieLinks);



    }

    public static ArrayList<String> harvestSubmodelLinks(String pageUrl) {

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

    public static ArrayList<String> harvestIspolnenieLinks(String pageUrl) {

        ArrayList<String> arrayListLinksFromPage = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect(pageUrl).get();
        } catch (IOException e) {
        }

        Elements linksFromPage = doc.select(".col-sm-3");

        for (Element element : linksFromPage) {
          //  arrayListLinksFromPage.add(element.attr("abs:href"));

            arrayListLinksFromPage.add(element.className());

        }

        return arrayListLinksFromPage;
    }


}
