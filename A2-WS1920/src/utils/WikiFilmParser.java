package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
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
    private Scanner wikiFilmScanner = null;
    private Pattern liPattern = Pattern.compile(".*?<td>(.*?)</td>.*?");


    public WikiFilmParser(String uri) throws MalformedURLException, IOException {
        wikiFilmScanner = new Scanner(new URL(uri).openStream(), "UTF-8");
    }

    public WikiFilmParser(Path path) throws MalformedURLException, IOException {
        this(path.toAbsolutePath().toString());
    }

    public void echoPage() {
        while (wikiFilmScanner.hasNextLine()) {
            System.out.println(wikiFilmScanner.nextLine());
        }
    }

    public String tryReader() {
        String keyReader = "";
        Map<String, List<String>> comicFilmsMapComplex = new HashMap<>();
        Map<String, Map<YearInterval, List<String>>> comicFilmsMap = new HashMap<>();

        Pattern begin = Pattern.compile("<h2><span class=\"mw-headline\" id=\"Realfilm-Adaptionen\">Realfilm-Adaptionen</span>.*?</h2>");
        Pattern end = Pattern.compile("<h2><span class=\"mw-headline\" id=\"Anmerkung_zu_Trickfilmen_und_Comiczeichnern\">Anmerkung zu Trickfilmen und Comiczeichnern</span>.*?</h2>");
        Pattern comicFilm = Pattern.compile("<td.*?>(.*)");
        Pattern rowspanRegex = Pattern.compile("<td(\\srowspan=\"(\\d)\")>(.*)");
        Pattern yearRegex = Pattern.compile("<td>(<a.*>)?(\\w)(</a>)?(.*(\\d*))+");
        String result = "";
        Pattern onlyFilm = Pattern.compile("<td>(.*)(\\((.*)?((\\d\\d\\d\\d)?(-)?(\\d\\d\\d\\d)+)+)");

        wikiFilmScanner.useDelimiter(begin);
        if (wikiFilmScanner.hasNext()) {
            wikiFilmScanner.next();
        }
        while (wikiFilmScanner.hasNextLine()) {
            String next = wikiFilmScanner.nextLine();

            //matcher
            Matcher matcherComicFilm = comicFilm.matcher(next);
            Matcher onlyFilmMatcher = onlyFilm.matcher(next);
            Matcher rowspanMatcher = rowspanRegex.matcher(next);

            // if a Comic is - erstmal Name extrahieren und dann Key erstellen
            if (end.matcher(next).find()) {
                System.out.println("END OF COMIC FILMS");
                break;

            } else if (onlyFilmMatcher.find()) { //is a film
                String filmName = onlyFilmMatcher.group(1);
                String yearFilm = onlyFilmMatcher.group(2);

                filmName = filmName.replaceAll("<a.*?>|</a>|<i>|</i>", "");
                filmName = filmName + " " + yearFilm + ")";

                comicFilmsMapComplex.get(keyReader).add(filmName);

            } else if (matcherComicFilm.find()) { //is a Comic
                String nameComic = matcherComicFilm.group(1);
                nameComic = nameComic.replaceAll("<a.*?>|</a>|<i>|</i>", "");

                if (!comicFilmsMap.containsKey(nameComic)) {
                    comicFilmsMapComplex.put(nameComic, new ArrayList<>());
                }
                keyReader = nameComic;

            }
        }

        System.out.println(comicFilmsMapComplex);
        return result;
    }

}
