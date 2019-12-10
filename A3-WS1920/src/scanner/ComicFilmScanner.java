package scanner;

import utils.YearInterval;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.time.Year;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComicFilmScanner {


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

    private Pattern rowspanRegex = Pattern.compile("<td(\\srowspan=\"(\\d)\")?>(.*)");

    private Pattern onlyFilm = Pattern.compile("<td>(.*)(\\((.*)?((\\d\\d\\d\\d)?(-)?(\\d\\d\\d\\d)+\\)+)+)");


    private Scanner scanner;



    public ComicFilmScanner(String uri) throws MalformedURLException, IOException {
        scanner = new Scanner(new URL(uri).openStream(), "UTF-8");
    }

    public ComicFilmScanner(Path path) throws MalformedURLException, IOException {
        this(path.toAbsolutePath().toString());
    }

    public Map<String, Map<YearInterval, List<String>>> contentToComicMap() {

        String keyReader = "";
        Map<String, Map<YearInterval, List<String>>> comicFilmsMap = new HashMap<>();

        scanner.useDelimiter(tableBegin);
        if (scanner.hasNext()) {
            scanner.next();
        }

        while (scanner.hasNextLine()) {
            String next = scanner.nextLine();

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

    public static String ppMap(Map<String, Map<YearInterval, List<String>>> comicMap) {

        for (Map.Entry<String, Map<YearInterval, List<String>>> entry : comicMap.entrySet()) {
            System.out.printf("%-10s->", entry.getKey());
            ppMap(entry.getValue(), 6);
        }
        return "!";
    }

    public static String ppMap(Map<YearInterval, List<String>> comicFilms, int tab) {
        for (Map.Entry<YearInterval, List<String>> entry : comicFilms.entrySet()) {
            boolean start = true;
            for (String film : entry.getValue()) {
                if (start) {
                    System.out.printf("\n%" + (tab) + "s" + "%-8s->%s", " ", entry.getKey(), entry.getValue());
                    start = false;
                } else {
                    System.out.printf("%" + (tab + 8 + 2) + "s%s", " ", film);
                }
            }
            //System.out.println();
        }
        System.out.println();
        return "!";
    }

}
