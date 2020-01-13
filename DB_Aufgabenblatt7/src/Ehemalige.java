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
        con = DriverManager.getConnection(url, "acq571", "Fernanda13425023");   // statt "scott" die a-Kennung hinschreiben, statt "tiger" eigenes HAW-Passwort

        //Erstelle die Anweisung und übergebe an das DBMS
        Statement statement = con.createStatement();
        String befehl = "INSERT INTO Ehemalige (EhemaligenID, Name, Vorname, Geburtsdatum, Geschlecht, Email, Telefonnummer) VALUES (17, 'Sanchez', 'Pedro', '03-06-93', 'männlich', 'pedro.sanchez@java.de', 040768932)";
        int anzahl = statement.executeUpdate(befehl);
        statement.close();

        //Angeben der Anfrgae
        Statement statement2 = con.createStatement();
        String anfrage = "SELECT * FROM Ehemalige";
        ResultSet ergebnis = statement2.executeQuery(anfrage); //das Ergebnis aus der Konsole

        //verarbeitet das Ergebnis
        while (ergebnis.next()) {
            String titel = ergebnis.getString(1);
            System.out.println("Ehemalige: " + titel);                // ergebnis verarbeitet ausgeben
        }
        ergebnis.close(); //result
        statement2.close(); //das Schließen muss nicht vergessen werden!


        Statement statement3 = con.createStatement();
        String secondAnfrage = "select * from EhemaligeHochs,ehemalige\n" +
                "where EhemaligeHochs.ehemaligeID = ehemalige.ehemaligenID \n" +
                "and ehemaligehochs.bezeichnunghochs = 'HAW'";
        ResultSet zweitesErgebnis = statement3.executeQuery(secondAnfrage);

        while (zweitesErgebnis.next()) {
            String titel2 = zweitesErgebnis.getString(1);
            System.out.println("Ergebnis: " + titel2);
        }
        zweitesErgebnis.close();
        statement3.close();
        con.close();
    }

}
