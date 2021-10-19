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
import java.util.regex.MatchResult;
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

    private Pattern rowspanRegex = Pattern.compile("<td(\\srowspan=\"(\\d)\")?>(.*)");

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

    public Map<String, Map<YearInterval, List<String>>> readTableComicFilm2() throws ParseException {
        Map<String, Map<YearInterval, List<String>>> comicFilmsMap = new HashMap<>();
        Pattern comicFilmRegex = Pattern.compile("</td></tr>.*?<tr>", Pattern.MULTILINE | Pattern.DOTALL);
        Pattern rowspanRegex2 = Pattern.compile("<td(\\srowspan=\"(\\d)\")?>(.*)</td>.*?<td>(.*)(\\((.*)?((\\d\\d\\d\\d)?(-)?(\\d\\d\\d\\d)+\\)+)+)", Pattern.MULTILINE | Pattern.DOTALL);
        Pattern filmRegex = Pattern.compile("");
        Pattern findFIlm = Pattern.compile("<td>(.*)(\\((.*)?((\\d\\d\\d\\d)?(-)?(\\d\\d\\d\\d)+\\)+)+)");


        wikiFilmScanner.useDelimiter(tableBegin);
        wikiFilmScanner.useDelimiter(comicFilmRegex);
        if (wikiFilmScanner.hasNext()) {
            wikiFilmScanner.next();
        }
        while (wikiFilmScanner.hasNext()) {
            String next = wikiFilmScanner.next();
            Matcher matcherComicFIlm = rowspanRegex2.matcher(next);

            if (matcherComicFIlm.find()) {
//                for (int i = 0; i <= matcherComicFIlm.groupCount(); i++) {
//                    System.out.println(i + ":" + matcherComicFIlm.group(i));
//                }
                String comicName = matcherComicFIlm.group(3);
                String filmName = matcherComicFIlm.group(4);
                String rowspan = matcherComicFIlm.group(2);
                filmName = filmName.replaceAll("<a.*?>|</a>|<i>|</i>", "");
                comicName = comicName.replaceAll("<a.*?>|</a>|<i>|</i>", "");


                comicFilmsMap.put(comicName, new HashMap<>());

                if (rowspan != null) {
                    int rowspanNum = Integer.parseInt(rowspan);

                    wikiFilmScanner.findWithinHorizon(findFIlm, 0);
                    MatchResult mr = wikiFilmScanner.match();
                    for (int i = 1; i <= mr.groupCount() ; i++) {

                        String FilmName = mr.group(1).replaceAll("<a.*?>|</a>|<i>|</i>", "");
                        String yearFilm = mr.group(2);
                        yearFilm = yearFilm.replaceAll("[^0-9]", "");
                        YearInterval yearIntervalObj = checkYear(yearFilm);
                        if (!comicFilmsMap.get(comicName).containsKey(yearIntervalObj)) {
                            comicFilmsMap.get(comicName).put(yearIntervalObj, new ArrayList<>());
                        }
                        comicFilmsMap.get(comicName).get(yearIntervalObj).add(filmName.trim());

                    }
                } else {
                    String yearFilm = matcherComicFIlm.group(5);
                    yearFilm = yearFilm.replaceAll("[^0-9]", "");

                    YearInterval yearIntervalObj = checkYear(yearFilm);
                    if (!comicFilmsMap.get(comicName).containsKey(yearIntervalObj)) {
                        comicFilmsMap.get(comicName).put(yearIntervalObj, new ArrayList<>());
                    }
                    comicFilmsMap.get(comicName).get(yearIntervalObj).add(filmName.trim());
                }

            }
        }
        return comicFilmsMap;
    }


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


    public Map<String, Map<YearInterval, List<String>>> readTableComicRowspan() throws ParseException {
        Map<String, Map<YearInterval, List<String>>> comicFilmsMap = new HashMap<>();

        wikiFilmScanner.useDelimiter(tableBegin);
        if (wikiFilmScanner.hasNext()) {
            wikiFilmScanner.next();
        }
        while (wikiFilmScanner.hasNextLine()) {
            String next = wikiFilmScanner.nextLine();
            Pattern rowspanRegex2 = Pattern.compile("<td(\\srowspan=\"(\\d)\")?>(.*)</td>.*?<td>(.*)");

            //matcher
            Matcher rowspanMatcher2 = rowspanRegex2.matcher(next);


            if (tableEnd.matcher(next).find()) {

                System.out.println("END OF COMIC FILMS");
                break;

            } else if (rowspanMatcher2.find()) { //is a film
                String comicName = rowspanMatcher2.group(3);
                String rowspan = rowspanMatcher2.group(2);
                comicName = comicName.replaceAll("<a.*?>|</a>|<i>|</i>", "");

                comicFilmsMap.put(comicName, new HashMap<>());


                if (rowspan != null) {
                    int rowspanNum = new Integer(rowspan);

                    for (int i = 1; i <= rowspanNum; i++) {
                        if (wikiFilmScanner.hasNextLine()) {
                            String nextFilm = wikiFilmScanner.nextLine();

                            while (!onlyFilm.matcher(nextFilm).find()) {
                                nextFilm = wikiFilmScanner.nextLine();
                            }
                            Matcher onlyFilm2 = onlyFilm.matcher(nextFilm);
                            if (onlyFilm2.find()) {
                                String filmName = onlyFilm2.group(1);
                                String yearFilm = onlyFilm2.group(2);
                                yearFilm = yearFilm.replaceAll("[^0-9]", "");
                                YearInterval yearIntervalObj = checkYear(yearFilm);

                                if (!comicFilmsMap.get(comicName).containsKey(yearIntervalObj)) {
                                    comicFilmsMap.get(comicName).put(yearIntervalObj, new ArrayList<>());
                                }
                                comicFilmsMap.get(comicName).get(yearIntervalObj).add(filmName.replaceAll("<a.*?>|</a>|<i>|</i>", "").trim());
                            }
                        }
                        System.out.println();
                    }
                } else {
                    if (wikiFilmScanner.hasNextLine()) {
                        String nextFilm = wikiFilmScanner.nextLine();
                        while (!onlyFilm.matcher(nextFilm).find()) {
                            nextFilm = wikiFilmScanner.nextLine();
                        }
                        Matcher onlyFilm2 = onlyFilm.matcher(nextFilm);
                        if (onlyFilm2.find()) {

                            String filmName = onlyFilm2.group(1);
                            String yearFilm = onlyFilm2.group(2);
                            yearFilm = yearFilm.replaceAll("[^0-9]", "");
                            YearInterval yearIntervalObj = checkYear(yearFilm);
                            if (!comicFilmsMap.get(comicName).containsKey(yearIntervalObj)) {
                                comicFilmsMap.get(comicName).put(yearIntervalObj, new ArrayList<>());
                            }
                            comicFilmsMap.get(comicName).get(yearIntervalObj).add(filmName.replaceAll("<a.*?>|</a>|<i>|</i>", "").trim());
                        }
                    }
                }
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
