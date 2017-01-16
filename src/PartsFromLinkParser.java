import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class PartsFromLinkParser {

    final static String regex = ".tr"/*+"[0-9]{2}"*/;

    final static String parseDir = "d:\\Java\\1_BMW_parse\\";
    static ArrayList<String> categoriesUrlsList = new ArrayList<String>();
    static ArrayList<String> underCategoriesUrlsList = new ArrayList<String>();

    static Csv.Writer partsWriter;
    static Csv.Writer partsWriter2;

    final static String underCategoriesFilePath = parseDir+"underCategoriesUrls.csv";
    final static String categoriesUrlsFilePath = parseDir+"catogoriesUrls.csv";
    final static String partsFilePath = parseDir+"PARTS.csv";


    static void bigGifURLParseWrite(Document doc) {

        Elements bigGif = doc.select(".img-responsive");
        for (Element element : bigGif) {

            int picPos = element.outerHtml().indexOf(".gif");
            if (picPos <= 0) {
                picPos = element.outerHtml().indexOf(".jpg");
            }

            String picUrl = element.outerHtml().substring(element.outerHtml().indexOf("src=") + 5, picPos + 4);
            String picFileName = picUrl.substring(picUrl.indexOf("bmwcats.com") + 12, picUrl.length());
            picFileName = parseDir + picFileName;

            try {
                downloadUsingNIO(picUrl, picFileName);
                Thread.sleep(10);
            } catch (IOException e) {
            } catch (InterruptedException e) {
            }


            /*System.out.println(picUrl);
            System.out.println(picUrl.indexOf("bmwcats.com")+12);
            System.out.println(picUrl.length());*/


            partsWriter.value(picUrl).newLine();

        }
    }


    static int getMinParsedPriceForCsv(ArrayList<Integer> parsedPricesArrayList) {

        if (parsedPricesArrayList.size() > 0) {
            Collections.sort(parsedPricesArrayList, new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return o1.compareTo(o2);
                }
            });
            return parsedPricesArrayList.get(0);
        }

        return 0;
    }

    static ArrayList<Integer> parsePrice(String priceUrl) {
        // priceUrl = "https://bmwcats.com/$12147549391/?href={%22catalog%22:%22bmw%22,%22serie%22:%22E60%22,%22mospid%22:47758,%22params%22:%22L:N:200410%22,%22group%22:%2212%22,%22node%22:%2212_1213%22,%22vin%22:%22B817790%22}";
        Document doc = null;

        try {
            doc = Jsoup.connect(priceUrl).get();
        } catch (IOException e) {
        }

        Elements prices = null;
        ArrayList<Integer> parsedPricesArrayList = new ArrayList<>();

        try {
            prices = doc.select(".spare-price-value-amount-span");
        } catch (NullPointerException e) {
            return parsedPricesArrayList;
        }

        System.out.println(prices.size());
        for (Element price : prices) {
            parsedPricesArrayList.add(Integer.parseInt(price.ownText()));
        }

        return parsedPricesArrayList;
    }


    public static void partsParse(String pageUrl) {
        Document doc = null;
        try {
            doc = Jsoup.connect(pageUrl).get();
        } catch (IOException e) {
        }


       /* System.out.println("****");
        System.out.println("___--** parsing parts **--___");
        System.out.println("****");*/

       /* Elements media = doc.select("[src]");

        for (Element element : media) {
            //if ( element.text().startsWith("https://bmwcats.com/") )
            {
               *//* System.out.println(element.text());
                System.out.println(element.attr("[src]"));
                System.out.println(element.data());
                  System.out.println(element.ownerDocument());
                 System.out.println(element.ownText());
                 System.out.println(element.html());
                System.out.println(element.val());
 *//*
                System.out.println(element.outerHtml());
                System.out.println(element.tagName());

            }
        }*/





        /*for (Element element : bigGif) {

            int picPos = element.outerHtml().indexOf(".gif");
            if (picPos<=0) {
                picPos = element.outerHtml().indexOf(".jpg");
            }



            System.out.println(element.outerHtml().substring(element.outerHtml().indexOf("src=")+5,picPos+4));
            *//*System.out.println(element.outerHtml().indexOf("src=")+5);
            System.out.println(picPos+4);*//*
        }*/


//        Elements parts = doc.select(regex);

        Elements partsNums = doc.select(".etk-spares-num");
        Elements partsNames = doc.select(".etk-spares-name");
        Elements partsAddition = doc.select(".etk-spares-additional");
        Elements partsAmount = doc.select(".etk-spares-amount");
        Elements partsPartNumbers = doc.select(".etk-spares-partnr");
        Elements partsPartNumbersDateBegin = doc.select(".etk-spares-date-begin");
        Elements partsPartNumbersDateEnd = doc.select(".etk-spares-date-end");
        Elements partsGetriebe = doc.select(".etk-spares-getriebe");
        Elements partsSteering = doc.select(".etk-spares-steering");

        Elements partsPartnrLink = doc.select(".etk-spares-partnr-link");


        Elements partsHeaders = doc.select(".page-caption");
        System.out.println();
        System.out.println(partsHeaders.size());


        // for (Element part : parts) { print(" * %s <%s> (%s)", part.attr("etk-spares-num"), part.attr("etk-spares-name"), part.attr("etk-spares-partnr")); }

        partsWriter.comment("---printing PARTS----").newLine();
//        partsWriter2.comment("---printing PARTS----").newLine();

        for (Element element : partsHeaders) {
            partsWriter.value(element.text()).newLine();
        }

         bigGifURLParseWrite(doc);

        for (int i = 0; i < partsNums.size(); i++) {

            try {

                String s = "https://bmwcats.com/" + partsPartNumbers.get(i).html()  /*html().replace("a title=\"\" href=\"","")*/
                        .replace("&quot;", "\"")
                        .replace("<a title=\"\" href=\"/", "");
                int n = s.indexOf("\" class=\"etk-spares");

                String ss = s;
                boolean priceLinkFound;
                if (n > 0) {
                    ss = s.substring(0, n);
                    priceLinkFound = true;
                } else priceLinkFound = false;

                ArrayList<Integer> parsedPricesArrayList = new ArrayList<>();
                int priceToWrite = 0;
                if (priceLinkFound) parsedPricesArrayList = parsePrice(ss);
                if (parsedPricesArrayList.size() > 0) {
                    priceToWrite = getMinParsedPriceForCsv(parsedPricesArrayList);
                }

                partsWriter.value(partsNums.get(i).text())
                        .value(partsNames.get(i).text())
                        .value(new String(partsPartNumbers.get(i).text().getBytes("UTF-8"), "UTF-8"))

                        .value(/*"https://bmwcats.com/"+partsPartNumbers.get(i).html()  *//*html().replace("a title=\"\" href=\"","")*//*
                                .replace("&quot;", "\"")
                                .replace("<a title=\"\" href=\"/", "")*/
                                ss)

                        .value(String.valueOf(priceToWrite))

//                        .value(partsPartnrLink.get(i).attr("a title href"))
//                        .value(partsPartnrLink.get(i).text())

                        .value(partsAddition.get(i).text())
                        .value(partsAmount.get(i).text())
                        .value(partsPartNumbersDateBegin.get(i).text())
                        .value(partsPartNumbersDateEnd.get(i).text())
                        .value(partsGetriebe.get(i).text())
                        .value(partsSteering.get(i).text())
                        .newLine();
            } catch (UnsupportedEncodingException ex) {
            }

            //System.out.println(partsNums.get(i).text() + "   " + partsNames.get(i).text() + "   " + partsPartNumbers.get(i).text());
        }

    }

    private static void downloadUsingNIO(String urlStr, String file) throws IOException {
        URL url = new URL(urlStr);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

    public static void getUnderCategoriesUrls(String categoryUrl) {
        Document doc = null;
        try {
            doc = Jsoup.connect(categoryUrl).get();
        } catch (IOException e) {
        }

        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

       /* print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }*/

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }

        Csv.Writer writer = new Csv.Writer(underCategoriesFilePath).delimiter(';');
        writer.comment("---printing UNDERcategories----").newLine();


        for (Element link : links) {
            // TODO �������� ��������-���������� �� ������� ����������� ������ ������������ ��������
            if (link.attr("abs:href").contains("bmwcats.com/bmw/E60/47758/L:N:200410/") && (link.attr("abs:href").length() == 68))
                underCategoriesUrlsList.add(link.attr("abs:href"));
        }

        for (String urll : underCategoriesUrlsList) {
           /*try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}*/
            writer.value(urll).newLine();
        }
        writer.close();

    }

    public static void getCategoriesUrls(String url) {

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
        }

        Elements links = doc.select("a[href]");
        Elements media = doc.select("[src]");
        Elements imports = doc.select("link[href]");

       /* print("\nMedia: (%d)", media.size());
        for (Element src : media) {
            if (src.tagName().equals("img"))
                print(" * %s: <%s> %sx%s (%s)",
                        src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                        trim(src.attr("alt"), 20));
            else
                print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
        }

        print("\nImports: (%d)", imports.size());
        for (Element link : imports) {
            print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
        }*/

        print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }

        for (Element link : links) {
            // TODO �������� ��������-���������� �� ������� ����������� ������ ������������ ��������
            if (link.attr("abs:href").contains(Main.url.substring(0, Main.url.indexOf("vin") - 4)) && (link.attr("abs:href").length() == 60))
                categoriesUrlsList.add(link.attr("abs:href"));
        }

        Csv.Writer writer = new Csv.Writer(categoriesUrlsFilePath).delimiter(',');
        writer.comment("---printing Categories----").newLine();
        // System.out.println("---printing Categories----");

        for (String urll : categoriesUrlsList) {
            /*try {
                Thread.sleep(1);
            } catch (InterruptedException e) {}*/
            writer.value(urll).newLine();
        }
        writer.close();

    }

    public static void getPrices(String url) {

        print("Fetching %s...", url);

        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
        }


        Elements prices = doc.select(".spare-price-value-amount-span");
        for (Element price : prices) {
            print(" * %s <%s> (%s)", price.attr("data-cost"), price.attr("abs:href"), price.attr("rel"));

        }
    }

    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width - 1) + ".";
        else
            return s;
    }
}
