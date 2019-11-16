package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;

/**
 * Diese Klasse liest und wertet Dateien über Scanner aus
 * und verwaltet eine Leste von Objekte der Klasse Year und Film
 *
 * @author Maria Agustina Bonesso, Sahin Tekes
 */

public class WikipediaComicFilmParser {


    public static Pattern tableBegin = Pattern.compile("<h2><span class=\"mw-headline\" id=\"Realfilm-Adaptionen\">Realfilm-Adaptionen</span>.*?</h2>");
    private Pattern tableEnd = Pattern.compile("<h2><span class=\"mw-headline\" id=\"Anmerkung_zu_Trickfilmen_und_Comiczeichnern\">Anmerkung zu Trickfilmen und Comiczeichnern</span>.*?</h2>");
    public static Pattern comicFilmTableRegex = Pattern.compile("<tr><td(\\srowspan=(\"(\\w)\"))?>(<a.*>)?(.*)+(</a>)?</td>(<td>(<a.*>)?(.*)+(</a>)?</td>)?</tr>");
    private Scanner wikiComicFilmScanner = null;


    public WikipediaComicFilmParser(String uri) throws MalformedURLException, IOException {
        wikiComicFilmScanner = new Scanner(new URL(uri).openStream(), "UTF-8");
    }

    public WikipediaComicFilmParser(Path path) throws MalformedURLException, IOException {
        this(path.toAbsolutePath().toString());
    }


    public void echoPage() {
        while (wikiComicFilmScanner.hasNextLine()) {
            System.out.println(wikiComicFilmScanner.nextLine());
        }
    }


    public Iterable<String> contentToFilm() {
        List<String> comicName = new ArrayList<String>();

        wikiComicFilmScanner.useDelimiter(tableBegin);
        while (wikiComicFilmScanner.hasNext()) {
           comicName.add(wikiComicFilmScanner.next());
        }

//        wikiComicFilmScanner.useDelimiter(tableBegin);
//        if (wikiComicFilmScanner.hasNextLine()) {
//            wikiComicFilmScanner.next();
//        }
//
//        wikiComicFilmScanner.useDelimiter(tableEnd);
//
//        if (wikiComicFilmScanner.hasNext()) {
//            String allComicFilmTable = wikiComicFilmScanner.next();
//            Matcher comicFilmMatcher = comicFilmTableRegex.matcher(allComicFilmTable);
//
//            while (comicFilmMatcher.find()) {
//                comicName.add(comicFilmMatcher.toMatchResult());
//            }
//
//        }
//        wikiComicFilmScanner.next();

        return comicName;
    }

    private static final String RESSOURCE_DIR = "out\\production\\A2-WS1920";
    public static void main(String[] args) throws IOException {

        Path wikiComicFilms = Paths.get(RESSOURCE_DIR + "\\Liste von Comicverfilmungen.html");

        WikipediaComicFilmParser sc = new WikipediaComicFilmParser("file:///" + wikiComicFilms.toAbsolutePath());

        System.out.println(sc.contentToFilm());
    }

}
