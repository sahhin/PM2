import java.sql.*;
//import java.sql.Connection;
//import java.sql.Connection;
//import java.sql.Statement;
//import java.sql.ResultSet;
//import java.sql.ResultSetMetaData;


public class Ehemalige {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Class.forName("oracle.jdbc.driver.OracleDriver"); // Treiber für daas DBMS
        //Verbindungen aufzubauen
        Connection con;
        String url = "jdbc:oracle:thin:@ora14:1521:inf14";
        con = DriverManager.getConnection(url, "acc387 ", "st8515449SS");   // statt "scott" die a-Kennung hinschreiben, statt "tiger" eigenes HAW-Passwort

        //Erstelle die Anweisung und übergebe an das DBMS
        Statement statement = con.createStatement();
        String befehl = "insert into Ehemalige(Rufname, Vorname, Nachname, Geburtsname, Geburtsdatum, adresse, EMail, TelNr) \n" +
                "VALUES ('Pepe', 'Pedro', 'Rosales', 'Pedro Sanchez', '03-06-93',adresse('Bachstrasse 111', 22083, 'Hamburg', 'Deutschland'), 'pedro.sanchez@java.de', 040768932)";
        int anzahl = statement.executeUpdate(befehl);
        statement.close();

        //Angeben der Anfrgae
        Statement statement2 = con.createStatement();
        String anfrage = "SELECT * FROM Ehemalige";
        ResultSet ergebnis = statement2.executeQuery(anfrage); //das Ergebnis aus der Konsole
        String titel = "";
        //verarbeitet das Ergebnis
        while (ergebnis.next()) {
            titel = ergebnis.getString(1);
        }
        System.out.println("Ehemalige: " + titel);                // ergebnis verarbeitet ausgeben
        ergebnis.close(); //result
        statement2.close(); //das Schließen muss nicht vergessen werden!


        Statement statement3 = con.createStatement();
        String secondAnfrage = "SELECT * FROM Ehemalige e join hatbelegtstudiengang h on e.studentid = h.studentid join bietet_studiengang b on b.studiengangkuerzel = h.studiengangkuerzel where h.studiengangkuerzel = 'INF' and b.institutid = (select institutid from institution i where i.bezeichnung = 'HAW')";
        ResultSet zweitesErgebnis = statement3.executeQuery(secondAnfrage);
        String titel2 = "";
        while (zweitesErgebnis.) {
            titel2 = zweitesErgebnis.getString(1);
            titel2 = titel2 + " " + zweitesErgebnis.getString(3);
            System.out.println("Ergebnis: " + titel2);

        }
        zweitesErgebnis.close();
        statement3.close();
        con.close();
    }

}
