package utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiFilmParser {

    private final int SINGLE_YEAR = 4;
    private final int MULTIPLE_YEAR = 8;

    private Pattern tableBegin = Pattern
            .compile("<h2><span class=\"mw-headline\" id=\"Realfilm-Adaptionen\">.*?</span></h2>");

    private Pattern tableEnd = Pattern
            .compile("<h2><span class=\"mw-headline\" id=\"Anmerkung_zu_Trickfilmen_und_Comiczeichnern\">Anmerkung zu Trickfilmen und Comiczeichnern</span>.*?</h2>");
    // Die Option Pattern.MULTILINE|Pattern.DOTALL bewirkt, dass auch Zeilenumbrüche mit dem . matchen
    private Pattern threeDeeEnum = Pattern.compile("<tr><td(\\srowspan=(\"(\\w)\"))?>(<a.*>)?(.*)+(</a>)?</td>(<td>(<a.*>)?(.*)+(</a>)?</td>)?</tr>",
            Pattern.MULTILINE | Pattern.DOTALL);

    private Pattern comicFilm = Pattern.compile("<td.*?>(.*)");

    private Pattern rowspanRegex = Pattern.compile("<td(\\srowspan=\"(\\d)\")>(.*)");

    private Pattern onlyFilm = Pattern.compile("<td>(.*)(\\((.*)?((\\d\\d\\d\\d)?(-)?(\\d\\d\\d\\d)+\\)+)+)");

    private Scanner wikiFilmScanner = null;

    /**
     * Konstruktor
     *
     * @param uri
     * @throws MalformedURLException
     * @throws IOException
     */
    public WikiFilmParser(String uri) throws MalformedURLException, IOException {
        wikiFilmScanner = new Scanner(new URL(uri).openStream(), "UTF-8");
    }

    /**
     * @param path
     * @throws MalformedURLException
     * @throws IOException
     */
    public WikiFilmParser(Path path) throws MalformedURLException, IOException {
        this(path.toAbsolutePath().toString());
    }

    /**
     * Liest Inhalt aus WikiPage und gibt diesen aus.
     */
    public void echoPage() {
        while (wikiFilmScanner.hasNextLine()) {
            System.out.println(wikiFilmScanner.nextLine());
        }
    }

    /**
     * @return
     * @throws ParseException
     */
    public Map<String, Map<YearInterval, List<String>>> readTableComicFilm() throws ParseException {
        String keyReader = "";
        Map<String, Map<YearInterval, List<String>>> comicFilmsMap = new HashMap<>();

        wikiFilmScanner.useDelimiter(tableBegin);
        if (wikiFilmScanner.hasNext()) {
            wikiFilmScanner.next();
        }
        while (wikiFilmScanner.hasNextLine()) {
            String next = wikiFilmScanner.nextLine();

            //matcher
            Matcher matcherComicFilm = comicFilm.matcher(next);
            Matcher onlyFilmMatcher = onlyFilm.matcher(next);
//            Matcher rowspanMatcher = rowspanRegex.matcher(next);

            // if a Comic is - erstmal Name extrahieren und dann Key erstellen
            if (tableEnd.matcher(next).find()) {

                System.out.println("END OF COMIC FILMS");
                break;

            } else if (onlyFilmMatcher.find()) { //is a film
                String filmName = onlyFilmMatcher.group(1);
                String yearFilm = onlyFilmMatcher.group(2);
                yearFilm = yearFilm.replaceAll("[^0-9]", "");

                // Überprüfe Jahresangabe und liefere ein YearInterval Object zurück
                YearInterval yearIntervalObj = checkYear(yearFilm);

                if (!comicFilmsMap.get(keyReader).containsKey(yearIntervalObj)) {
                    comicFilmsMap.get(keyReader).put(yearIntervalObj, new ArrayList<>());
                }
                comicFilmsMap.get(keyReader).get(yearIntervalObj).add(filmName.replaceAll("<a.*?>|</a>|<i>|</i>", "").trim());


            } else if (matcherComicFilm.find()) { //is a Comic
                String nameComic = matcherComicFilm.group(1);
                nameComic = nameComic.replaceAll("<a.*?>|</a>|<i>|</i>", "");

                if (!comicFilmsMap.containsKey(nameComic) && nameComic.length() > 0) {
                    comicFilmsMap.put(nameComic, new HashMap<>());
                }
                keyReader = nameComic;
            }
        }
        return comicFilmsMap;
    }

    public YearInterval checkYear(String year) {
        Year yearBegin = null;
        YearInterval yearIntervalObj = null;
        if (year.length() > 4) {
            yearBegin = Year.of(Integer.parseInt(year.substring(0, SINGLE_YEAR)));
            Year yearEnd = Year.of(Integer.parseInt(year.substring(SINGLE_YEAR, MULTIPLE_YEAR)));
            yearIntervalObj = new YearInterval(yearBegin, yearEnd);
        } else {
            yearBegin = Year.of(Integer.parseInt(year.substring(0, SINGLE_YEAR)));
            yearIntervalObj = new YearInterval(yearBegin);
        }

        return yearIntervalObj;
    }
}
