package utils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Year;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiFilmParser {

    /*
     * Reguläre Ausdrücke für das Parsen der Datei
     */
    private Pattern tableBegin = Pattern
            .compile("<h2><span class=\"mw-headline\" id=\"Realfilm-Adaptionen\">.*?</span></h2>");

    private Pattern tableEnd = Pattern
            .compile("<h2><span class=\"mw-headline\" id=\"Anmerkung_zu_Trickfilmen_und_Comiczeichnern\">Anmerkung zu Trickfilmen und Comiczeichnern</span>.*?</h2>");
    // Die Option Pattern.MULTILINE|Pattern.DOTALL bewirkt, dass auch Zeilenumbrüche mit dem . matchen
    private Pattern threeDeeEnum = Pattern.compile("<tr><td(\\srowspan=(\"(\\w)\"))?>(<a.*>)?(.*)+(</a>)?</td>(<td>(<a.*>)?(.*)+(</a>)?</td>)?</tr>",
            Pattern.MULTILINE | Pattern.DOTALL);
    private Scanner wiki3DFilmScanner = null;
    private Pattern liPattern = Pattern.compile(".*?<td>(.*?)</td>.*?");


    public WikiFilmParser(String uri) throws MalformedURLException, IOException {
        wiki3DFilmScanner = new Scanner(new URL(uri).openStream(), "UTF-8");
    }

    public WikiFilmParser(Path path) throws MalformedURLException, IOException {
        this(path.toAbsolutePath().toString());
    }

    public void echoPage() {
        while (wiki3DFilmScanner.hasNextLine()) {
            System.out.println(wiki3DFilmScanner.nextLine());
        }
    }

    public String tryReader() {
        Map<String, Map<YearInterval, List<String>>> comicFilmsMap = new HashMap<>();

        Pattern begin = Pattern.compile("<h2><span class=\"mw-headline\" id=\"Realfilm-Adaptionen\">Realfilm-Adaptionen</span>.*?</h2>");
        Pattern end = Pattern.compile("<h2><span class=\"mw-headline\" id=\"Anmerkung_zu_Trickfilmen_und_Comiczeichnern\">Anmerkung zu Trickfilmen und Comiczeichnern</span>.*?</h2>");
        Pattern comicFilm = Pattern.compile("<td.*?>(.*)[^\\d][^\\d][^\\d][^\\d)]");
        Pattern rowspanRegex = Pattern.compile("<td(\\srowspan=\"(\\d)\")+>(.*)");
        Pattern yearRegex = Pattern.compile("<td>(<a.*>)?(\\w)(</a>)?(.*(\\d*))+");
        String result = "";
        Pattern onlyFilm = Pattern.compile("<td>(.*)(\\((.*)?((\\d\\d\\d\\d)?(-)?(\\d\\d\\d\\d)+)+)");


        wiki3DFilmScanner.useDelimiter(begin);
        if (wiki3DFilmScanner.hasNext()) {
            wiki3DFilmScanner.next();
        }
        while (wiki3DFilmScanner.hasNextLine()) {
            String next = wiki3DFilmScanner.nextLine();

            //matcher
            Matcher matcherComicFilm = comicFilm.matcher(next);
            Matcher onlyFilmMatcher = onlyFilm.matcher(next);
            Matcher rowspanMatcher = rowspanRegex.matcher(next);

            // if a Comic is - erstmal Name extrahieren und dann Key erstellen
            if (end.matcher(next).find()) {
                System.out.println("END OF COMIC FILMS");
                break;

                //<td irgendwas>comic-name
            } else if (matcherComicFilm.find()) {
                System.out.println(next);
                // System.out.println(next);
                   String nameComic = matcherComicFilm.group(1);

               // Key erstellen
                   if (!comicFilmsMap.containsKey(nameComic)) {
                       comicFilmsMap.put(nameComic, null);
                 }
            } else if (onlyFilmMatcher.find()) {
            }

            System.out.println(comicFilmsMap);
            //wenn key vorhanden, weiter
//                if (rowspanMatcher.find()) {
//                    String rowspanNum = rowspanMatcher.group(2);
//
//                } else {


            //             }
        }
        //else if also ist ein Film... Dann extrahieren wir das Jahr Pattern onlyFilm
        //setzen wir Value für den Key und dann hinfügen---
        return result;

    }


    public Map<String, List<String>> contentTo3DFilmsPerYear() {
        Map<String, List<String>> threeDeeMap = new HashMap<>();
        // Positionieren des Scanners vor dem Pattern threeDeeBegin.
        // "<h2><span class=\"mw-headline\"
        // id=\"3D-Filme\">3D-Filme</span>.*?</h2>"
        wiki3DFilmScanner.useDelimiter(tableBegin);
        if (wiki3DFilmScanner.hasNext()) {
            wiki3DFilmScanner.next();
        }
        // Lesen des Bereichs bis zum Ende der Aufzählung, das durch das Pattern
        // threeDeeEnd markiert wird.
        // "<h2><span class=\"mw-headline\"
        // id=\"4D-Filme\">4D-Filme</span>.*?</h2>"
        wiki3DFilmScanner.useDelimiter(tableEnd);
        if (wiki3DFilmScanner.hasNext()) {
            String filmsPerYearEnumeration = wiki3DFilmScanner.next();
            // System.out.println(filmsPerYearEnumeration);
            // Extrahieren der Aufzählung nach Jahren / Zeitspannen. Pattern
            // threeDeeEnum
            // "<h3>.*?<span class=\"mw-headline\"
            // id=.*?>(.*?)</span>.*?</h3>(.*?)</ul>"
            Matcher matcherEnum = threeDeeEnum.matcher(filmsPerYearEnumeration);
            while (matcherEnum.find()) {
                // Lesen der Jahresangabe (Gruppe 1)
                String currentDate = matcherEnum.group(1);
                if (!threeDeeMap.containsKey(currentDate)) {
                    threeDeeMap.put(currentDate, new ArrayList<>());
                    // Extrahieren des Bereichs der ListItems, (Gruppe 2)
                    String filmListContent = matcherEnum.group(2);
                    // Parsen der ListItems und Einfügen in das Verzeichnis
                    contentTo3DFilmList(filmListContent, currentDate, threeDeeMap);
                }
            }
        }
        return threeDeeMap;
    }

    private void contentTo3DFilmList(String ulList, String currentDate, Map<String, List<String>> threeDeeMap) {
        // Erzeugen eines Matchers für das Extrahieren der HTML ListItems
        // (Pattern liPattern)
        // "<li>(.*)?</li>"
        Matcher liMatcher = liPattern.matcher(ulList);
        // Partielles Matching des liPatterns, für alle Items der Liste
        while (liMatcher.find()) {
            String liContent = liMatcher.group(1);
            threeDeeMap.get(currentDate).add(liContent);
        }
    }


}
