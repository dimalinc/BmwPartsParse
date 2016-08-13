import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


public class Main {


    final static String regex = ".tr"/*+"[0-9]{2}"*/;
    final static String url = "https://bmwcats.com/bmw/E60/47758/L:N:200410/?vin=B817790";


    static ArrayList<String> categoriesUrlsList = new ArrayList<String>();
   static ArrayList<String> underCategoriesUrlsList = new ArrayList<String>();

    static Csv.Writer partsWriter;
    static Csv.Writer partsWriter2;


    public static void main(String[] args) throws IOException {
       // Validate.isTrue(args.length == 1, "usage: supply url to fetch");

       // Csv.Writer writer = new Csv.Writer("c:\\Java\\0_BMW_parse\\1.csv").delimiter(',');

        /*writer.comment("example of csv")
                .value("a").value("b").newLine()
                .value("c").close();*/

        /*Csv.Reader reader = new Csv.Reader(new FileReader("c:\\Java\\0_BMW_parse\\\\1.csv"))
                .delimiter(',').ignoreComments(true);*/
       /* System.out.println(reader.readLine());*/



        Parser.getCategoriesUrls(url);

        for (String categoryUrl: Parser.categoriesUrlsList) {
            Parser.getUnderCategoriesUrls(categoryUrl);
        }

        Parser.partsWriter = new Csv.Writer(Parser.partsFilePath).delimiter(';');
       // partsWriter2 = new Csv.Writer("c:\Java\0_BMW_parse\\PARTS2.csv").delimiter(';');

        for (String underCategoryUrl:Parser.underCategoriesUrlsList) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {}
            Parser.partsParse(underCategoryUrl);
        }

        Parser.partsWriter.close();
    }




}
