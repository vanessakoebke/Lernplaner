package service;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.*;

public class DatenbankService {
    private static final String DB_URL = "jdbc:sqlite:data/Lernplaner.db";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void init() {
        createModuleTable();
        createAufgabenTable();
    }

    private void createModuleTable() {
        String sql = "CREATE TABLE IF NOT EXISTS module (" + "id INTEGER PRIMARY KEY AUTOINCREMENT,"  //Tabelle anpassen hier!
                + "name TEXT UNIQUE NOT NULL," + "klausurTermin TEXT," + // speichern wir als ISO-String, z.B.
                                                                         // "2025-09-13"
                "aktuell INTEGER NOT NULL," + // -- 0 = false, 1 = true
                "note TEXT," + "tageWiederholen INTEGER NOT NULL" + ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void createAufgabenTable() {
        String sql = "CREATE TABLE IF NOT EXISTS aufgabe (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +  //Tabelle anpassen hier!
                "typ TEXT NOT NULL, " +
                "titel TEXT NOT NULL, " +
                "beschreibung TEXT, " +
                "start TEXT, " +
                "ende TEXT, " +
                "status INTEGER NOT NULL, " +
                "modulId INTEGER, " +
                "seiten INTEGER, " +
                "FOREIGN KEY(modulId) REFERENCES module(id)" +
                ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upsertAufgabe(Aufgabe aufgabe) {
        String sql = "INSERT INTO aufgabe (typ, titel, beschreibung, start, ende, status, modulId, seiten) " +  //Tabelle anpassen hier!
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                     "ON CONFLICT(id) DO UPDATE SET " +
                     "typ = excluded.typ, " +
                     "titel = excluded.titel, " +
                     "beschreibung = excluded.beschreibung, " +
                     "start = excluded.start, " +
                     "ende = excluded.ende, " +
                     "status = excluded.status, " +
                     "modulId = excluded.modulId, " +
                     "seiten = excluded.seiten;";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, aufgabe.getTyp());                 // Subklasse
            pstmt.setString(2, aufgabe.getTitel());
            pstmt.setString(3, aufgabe.getBeschreibung());

            if (aufgabe.getStart() != null) {
                pstmt.setString(4, aufgabe.getStart().toString());
            } else {
                pstmt.setNull(4, java.sql.Types.VARCHAR);
            }

            if (aufgabe.getEnde() != null) {
                pstmt.setString(5, aufgabe.getEnde().toString());
            } else {
                pstmt.setNull(5, java.sql.Types.VARCHAR);
            }

            pstmt.setInt(6, aufgabe.getStatusIndex());
            
            if (aufgabe.getModul() != null && aufgabe.getModul().getId() != null) {
                pstmt.setInt(7, aufgabe.getModul().getId());
            } else {
                pstmt.setNull(7, java.sql.Types.INTEGER);
            }

            // Beispiel für optionale Unterklassenfelder
            if (aufgabe instanceof AufgabeDurcharbeiten) {
                pstmt.setInt(8, ((AufgabeDurcharbeiten) aufgabe).getSeiten());
            } else {
                pstmt.setNull(8, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();

            // ID übernehmen, falls neu eingefügt
            if (aufgabe.getId() == null) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        aufgabe.setId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAufgabe(Aufgabe aufgabe) {
        if (aufgabe.getId() == null) {
            return; // Sicherheit: Aufgabe ist noch nie in der DB gewesen
        }

        String sql = "DELETE FROM aufgabe WHERE id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, aufgabe.getId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                System.out.println("Keine Aufgabe mit dieser ID gefunden: " + aufgabe.getId());
            } else {
                System.out.println("Aufgabe erfolgreich gelöscht: " + aufgabe.getId());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Aufgabe> getAufgaben() {
        List<Aufgabe> aufgaben = new ArrayList<>();
        String sql = "SELECT id, typ, titel, beschreibung, start, ende, status, modulId, seiten FROM aufgabe"; //Tabelle anpassen hier!

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String typ = rs.getString("typ");
                String titel = rs.getString("titel");
                String beschreibung = rs.getString("beschreibung");
                LocalDate start = rs.getString("start") != null ? LocalDate.parse(rs.getString("start")) : null;
                LocalDate ende = rs.getString("ende") != null ? LocalDate.parse(rs.getString("ende")) : null;
                int status = rs.getInt("status");
                Integer modulId = rs.getInt("modulId");
                int seiten = rs.getInt("seiten");//Tabelle anpassen hier!

                Modul modul = null;
                if (modulId != null) {
                    modul = getModul(modulId);
                }

                Aufgabe aufgabe;
                switch (typ) {
                    case "allgemein":
                        aufgabe = new AufgabeAllgemein(titel, beschreibung, ende, start, status, modul);
                        break;
                    case "durcharbeiten":
                        aufgabe = new AufgabeDurcharbeiten(titel, beschreibung, ende, start, status, modul, seiten);
                        break;
//                    case "ea":
//                        //aufgabe = new AufgabeEA(titel, beschreibung, ende, start, status, modul);
//                        break;
//                    case "altklausur":
//                        //aufgabe = new AufgabeAltklausur(titel, beschreibung, ende, start, status, modul);
//                        break;
                    default:
                        // Fallback, falls Typ unbekannt
                        aufgabe = new AufgabeAllgemein(titel, beschreibung, ende, start, status, modul);
                }

                aufgabe.setId(id); // ID setzen, falls du sie brauchst
                aufgaben.add(aufgabe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return aufgaben;
    }


    public List<Modul> getModule() {
        List<Modul> module = new ArrayList<>();
        String sql = "SELECT name, klausurTermin, aktuell, note, tageWiederholen FROM Module"; //Tabelle anpassen hier!
        try (Connection conn = connect();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString("name");
                LocalDate termin = rs.getString("klausurTermin") != null ? LocalDate.parse(rs.getString("klausurTermin")) //Tabelle anpassen hier!
                        : null;
                boolean aktuell = rs.getBoolean("aktuell");
                String note = rs.getString("note");
                int tageWiederholen = rs.getInt("tageWiederholen");
                Modul modul = new Modul(name, termin, aktuell, note);
                modul.setTageWiederholen(tageWiederholen);
                module.add(modul);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return module;
    }

    public void upsertModul(Modul modul) {
        String sql = "INSERT INTO Module (name, klausurTermin, aktuell, note, tageWiederholen) "                                //Tabelle anpassen hier!
                + "VALUES (?, ?, ?, ?, ?) " + "ON CONFLICT(name) DO UPDATE SET "
                + "klausurTermin = excluded.klausurTermin, " + "aktuell = excluded.aktuell, " + "note = excluded.note, "
                + "tageWiederholen = excluded.tageWiederholen;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, modul.getName());
            if (modul.getKlausurTermin() != null) {
                pstmt.setString(2, modul.getKlausurTermin().toString());
            } else {
                pstmt.setNull(2, java.sql.Types.DATE);
            }
            pstmt.setBoolean(3, modul.getAktuell());
            pstmt.setString(4, modul.getNote()); // kann null sein
            pstmt.setInt(5, modul.getTageWiederholen());
            pstmt.executeUpdate();
            // ID aus der DB übernehmen (nur wenn neues Modul)
            if (modul.getId() == null) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        modul.setId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteModul(Modul modul) {
        if (modul.getId() == null) {
            return; // Sicherheit, falls Modul nie in DB war
        }
        String sql = "DELETE FROM Module WHERE id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, modul.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Modul getModul(int id) {
        String sql = "SELECT id, name, klausurTermin, aktuell, note, tageWiederholen FROM module WHERE id = ?"; //Tabelle anpassen hier!
        Modul modul = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    LocalDate termin = rs.getString("klausurTermin") != null ? 
                                       LocalDate.parse(rs.getString("klausurTermin")) : null;
                    boolean aktuell = rs.getBoolean("aktuell");
                    String note = rs.getString("note");
                    int tageWiederholen = rs.getInt("tageWiederholen");

                    modul = new Modul(name, termin, aktuell, note);
                    modul.setTageWiederholen(tageWiederholen);
                    modul.setId(id); // falls du die ID in Modul speicherst
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return modul;
    }

}
