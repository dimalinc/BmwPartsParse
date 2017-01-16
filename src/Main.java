import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Main {

    final static String regex = ".tr"/*+"[0-9]{2}"*/;
    final static String url = "https://bmwcats.com/bmw/E60/47758/L:N:200410/?vin=B817790";
    final static String linksFile = "d:\\Java\\1_BMW_parse\\BMW_URL_list\\BMWcats_links_cleared2.txt";

    static ArrayList<String> categoriesUrlsList = new ArrayList<String>();
   static ArrayList<String> underCategoriesUrlsList = new ArrayList<String>();

    static Csv.Writer partsWriter;
    static Csv.Writer partsWriter2;

   static List<String> lines = new ArrayList<String>();
    static {
        lines.add("https://bmwcats.com/bmw1/E82/51865/");
    }




    public static void main(String[] args) throws IOException {

        /*BufferedReader reader = new BufferedReader(new FileReader(linksFile));
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }*/
        //если нужен массив то список можно запросто преобрпзовать
        // String [] linesAsArray = lines.toArray(new String[lines.size()]);


        for (String lineFromListLines:lines) {

            if (check6slashes(lineFromListLines)) {
                particularCarPartsParse(lineFromListLines);
            }
        }


    }

   static boolean check6slashes(String url) {

        int slashCount=0;

       char[] charArray =  url.toCharArray();
        for (int i = 0; i < charArray.length; i++) {


            if (charArray[i] == '/') {
                slashCount++;
            }



        }

       System.out.println("UrlCharArray" + "slash count = " + slashCount);

        if (slashCount == 6) return true;
        return false;
    }

   static void particularCarPartsParse(String url) {



        PartsFromLinkParser.getCategoriesUrls(url);

        for (String categoryUrl: PartsFromLinkParser.categoriesUrlsList) {
            PartsFromLinkParser.getUnderCategoriesUrls(categoryUrl);
        }

        PartsFromLinkParser.partsWriter = new Csv.Writer(PartsFromLinkParser.partsFilePath).delimiter(';');
        // partsWriter2 = new Csv.Writer("c:\Java\0_BMW_parse\\PARTS2.csv").delimiter(';');

        for (String underCategoryUrl: PartsFromLinkParser.underCategoriesUrlsList) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {}
            PartsFromLinkParser.partsParse(underCategoryUrl);
        }

        PartsFromLinkParser.partsWriter.close();
    }

    // Validate.isTrue(args.length == 1, "usage: supply url to fetch");

    // Csv.Writer writer = new Csv.Writer("c:\\Java\\0_BMW_parse\\1.csv").delimiter(',');

        /*writer.comment("example of csv")
                .value("a").value("b").newLine()
                .value("c").close();*/

        /*Csv.Reader reader = new Csv.Reader(new FileReader("c:\\Java\\0_BMW_parse\\\\1.csv"))
                .delimiter(',').ignoreComments(true);*/
       /* System.out.println(reader.readLine());*/




}
