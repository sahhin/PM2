package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URL;

/**
 * Diese Klasse liest und wertet Dateien über Scanner aus
 * und verwaltet eine Leste von Objekte der Klasse Year und Film
 *
 * @author Maria Agustina Bonesso
 */

public class WikipediaComicFilmParser {

    private Pattern tableBegin = Pattern.compile("<h3><span id=\"0\\.E2\\.80\\.939\"></span><span class=\"mw-headline\" id=\"0–9\">.*</h3>");
    private Pattern tableEnd = Pattern.compile("<h2><span class=\"mw-headline\" id=\"Anmerkung_zu_Trickfilmen_und_Comiczeichnern\">Anmerkung zu Trickfilmen und Comiczeichnern</span>.*?</h2>");
    private Pattern comicFilmTable = Pattern.compile("<tr>(<td.*>(<a.*>)?(.*)(/||2)?</td>)(<td.*>(||2)?(.*)(/||2)?</td>)+</tr>");
    private Pattern onlyFilmTable = Pattern.compile("<tr>(<td.*>(<a.*>)?(.*)(/||2)?</td>){1}</tr>");
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


    public Map<String, List<String>> contentToFilm() {
        Map<String, List<String>> threeDeeMap = new HashMap<>();

        wikiComicFilmScanner.useDelimiter(tableBegin);
        if (wikiComicFilmScanner.hasNextLine()) {
            wikiComicFilmScanner.next();
        }

        wikiComicFilmScanner.useDelimiter(tableEnd);
        if (wikiComicFilmScanner.hasNext()) {
            String comicFilm = wikiComicFilmScanner.next();
            Matcher filmComicTable = comicFilmTable.matcher(comicFilm);
        }

        return null;
    }

}
