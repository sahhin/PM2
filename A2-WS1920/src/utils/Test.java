package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    private static final String RESOURCE_DIR = "C:\\Users\\AugustinaBonesso\\Desktop\\PM2\\A2-WS1920\\ressources\\Liste von Comicverfilmungen.html";
    private static final Pattern EMPTY_LINE = Pattern.compile("\\s*");
    private static final Pattern begin = Pattern.compile("<h3><span id=\"0\\.E2\\.80\\.939\"></span><span class=\"mw-headline\" id=\"0–9\">.*</h3>");
    private static final Pattern end = Pattern.compile("<h2><span class=\"mw-headline\" id=\"Anmerkung_zu_Trickfilmen_und_Comiczeichnern\">Anmerkung zu Trickfilmen und Comiczeichnern</span>.*?</h2>");
    private static final Pattern comicFilmRegex = Pattern.compile("<(w+?)>(.*?)</||1</||1>");

    public static void main(String[] args) {
        String htmlText1 = "<tr><td>2 Guns</td><td><a href=\"https://de.wikipedia.org/wiki/2_Guns\" title=\"2 Guns\">2 Guns</a> (2013)</td></tr>";
        String htmlText2 = "<tr><td rowspan=\"2\"><a href=\"https://de.wikipedia.org/wiki/XIII_(Comicserie)\" title=\"XIII (Comicserie)\">XIII</a> </td>" +
                "<td><a href=\"https://de.wikipedia.org/wiki/XIII_%E2%80%93_Die_Verschw%C3%B6rung\" title=\"XIII – Die Verschwörung\">XIII – Die Verschwörung</a> (Fernseh-Zweiteiler, 2008)" +
                "</td></tr>";
        String html3 = "<tr><td>47:an Löken blåser på (1972)" +
                "</td></tr>";
//        Pattern comicFilmTable = Pattern.compile("<tr>(<td(\\srowspan= (\"(\\d)\"))?>(<a.*>)?(.*)(</a>)?</td>)(<td.*>(</a>)?(.*)(</a>)?</td>)?</tr>");
        Pattern comicFilmTable = Pattern.compile("<tr>(<td(\\srowspan=(\"(\\w)\"))?>(<a.*>)?(.*)+(</a>)?</td>)+</tr>");
        Matcher matcher = comicFilmTable.matcher(htmlText2);
       // int rowspan = Integer.parseInt(matcher.group(4));
        System.out.println(html3.matches(String.valueOf(comicFilmTable)));


    }
}

//        try (Scanner scanner = new Scanner(new File(RESOURCE_DIR))) {
//            List<MatchResult> results = new ArrayList<MatchResult>();
//            while (scanner.hasNextLine() &&
//                    scanner.skip(EMPTY_LINE).hasNextLine()) {
//                scanner.useDelimiter(begin);
//                if (scanner.hasNextLine()) {
//                    scanner.next();
//                }
//
//                scanner.useDelimiter(end);
//                if (scanner.hasNextLine()) {
//                    scanner.next();
//                }
//                scanner.useDelimiter(comicFilmRegex);
//                if (scanner.hasNext()) {
//                    //   String comicFilm = scanner.next();
//                    //Matcher matcher = comicFilmRegex.matcher(comicFilm);
//                    while (scanner.hasNext()) {
//                        scanner.next().matches();
//                        results.add(scanner.match());
//                    }
//                    System.out.println(results);
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }



