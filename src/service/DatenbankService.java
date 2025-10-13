package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
                "ergebnis TEXT," +
                "einheiten INTEGER," +
                "einheitstyp TEXT," +
                "FOREIGN KEY(modulId) REFERENCES module(id)" +
                ");";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upsertAufgabe(Aufgabe aufgabe) {
        // INSERT wenn neu (id == null), sonst UPDATE
        if (aufgabe.getId() == null) {
            String sql = "INSERT INTO aufgabe (typ, titel, beschreibung, start, ende, status, modulId, seiten, ergebnis, einheiten, einheitstyp)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

                int idx = 1;
                pstmt.setString(idx++, aufgabe.getTyp().name());
                pstmt.setString(idx++, aufgabe.getTitel().toString());

                if (aufgabe.getBeschreibung() != null) {
                    pstmt.setString(idx++, aufgabe.getBeschreibung());
                } else {
                    pstmt.setNull(idx++, Types.VARCHAR);
                }

                LocalDate start = aufgabe.getStart();
                if (start != null) {
                    pstmt.setString(idx++, start.toString()); // ISO-String YYYY-MM-DD
                } else {
                    pstmt.setNull(idx++, Types.VARCHAR);
                }

                LocalDate ende = aufgabe.getEnde();
                if (ende != null) {
                    pstmt.setString(idx++, ende.toString());
                } else {
                    pstmt.setNull(idx++, Types.VARCHAR);
                }

                pstmt.setInt(idx++, aufgabe.getStatusIndex());

                if (aufgabe.getModul() != null && aufgabe.getModul().getId() != null) {
                    pstmt.setInt(idx++, aufgabe.getModul().getId());
                } else {
                    pstmt.setNull(idx++, Types.INTEGER);
                }

                if (aufgabe instanceof AufgabeDurcharbeiten) {
                    pstmt.setInt(idx++, ((AufgabeDurcharbeiten) aufgabe).getSeiten());
                } else {
                    pstmt.setNull(idx++, Types.INTEGER);
                }
                
                if (aufgabe instanceof AufgabeEA) {
                    pstmt.setString(idx++, ((AufgabeEA) aufgabe).getErgebnis());
                } else if (aufgabe instanceof AufgabeAltklausur) {
                    pstmt.setString(idx++, ((AufgabeAltklausur) aufgabe).getErgebnis());
                }else {
                    pstmt.setNull(idx++, Types.VARCHAR);
                }
                
                if (aufgabe instanceof AufgabeWiederholen) {
                    pstmt.setInt(idx++, ((AufgabeWiederholen) aufgabe).getEinheiten());
                    pstmt.setString(idx++, ((AufgabeWiederholen) aufgabe).getEinheitstyp().name());
                } else {
                    pstmt.setNull(idx++, Types.INTEGER);
                    pstmt.setNull(idx++, Types.VARCHAR);
                }
                
                

                pstmt.executeUpdate();

                // generierte ID auslesen und in Objekt setzen
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        aufgabe.setId(keys.getInt(1));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
         // UPDATE für vorhandene Aufgabe
            String sql = "UPDATE aufgabe SET typ=?, titel=?, beschreibung=?, start=?, ende=?, status=?, modulId=?, seiten=?, ergebnis=?, einheiten=?, einheitstyp=? WHERE id=?";
            try (Connection conn = connect();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                int idx = 1;
                pstmt.setString(idx++, aufgabe.getTyp().name());
                pstmt.setString(idx++, aufgabe.getTitel());

                if (aufgabe.getBeschreibung() != null) {
                    pstmt.setString(idx++, aufgabe.getBeschreibung());
                } else {
                    pstmt.setNull(idx++, Types.VARCHAR);
                }

                LocalDate start = aufgabe.getStart();
                if (start != null) {
                    pstmt.setString(idx++, start.toString());
                } else {
                    pstmt.setNull(idx++, Types.VARCHAR);
                }

                LocalDate ende = aufgabe.getEnde();
                if (ende != null) {
                    pstmt.setString(idx++, ende.toString());
                } else {
                    pstmt.setNull(idx++, Types.VARCHAR);
                }

                pstmt.setInt(idx++, aufgabe.getStatusIndex());

                if (aufgabe.getModul() != null && aufgabe.getModul().getId() != null) {
                    pstmt.setInt(idx++, aufgabe.getModul().getId());
                } else {
                    pstmt.setNull(idx++, Types.INTEGER);
                }

                if (aufgabe instanceof AufgabeDurcharbeiten) {
                    pstmt.setInt(idx++, ((AufgabeDurcharbeiten) aufgabe).getSeiten());
                } else {
                    pstmt.setNull(idx++, Types.INTEGER);
                }

                // Ergebnis für EA oder Altklausur
                if (aufgabe instanceof AufgabeEA) {
                    pstmt.setString(idx++, ((AufgabeEA) aufgabe).getErgebnis());
                } else if (aufgabe instanceof AufgabeAltklausur) {
                    pstmt.setString(idx++, ((AufgabeAltklausur) aufgabe).getErgebnis());
                } else {
                    pstmt.setNull(idx++, Types.VARCHAR);
                }

                // Einheiten und Einheitstyp für Wiederholen
                if (aufgabe instanceof AufgabeWiederholen) {
                    pstmt.setInt(idx++, ((AufgabeWiederholen) aufgabe).getEinheiten());
                    pstmt.setString(idx++, ((AufgabeWiederholen) aufgabe).getEinheitstyp().name());
                } else {
                    pstmt.setNull(idx++, Types.INTEGER);
                    pstmt.setNull(idx++, Types.VARCHAR);
                }

                // WHERE id = ?
                pstmt.setInt(idx++, aufgabe.getId());
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
            } 
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Aufgabe> getAufgaben() {
        List<Aufgabe> aufgaben = new ArrayList<>();
        String sql = "SELECT id, typ, titel, beschreibung, start, ende, status, modulId, seiten, ergebnis, einheiten, einheitstyp FROM aufgabe"; //Tabelle anpassen hier!

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titel = rs.getString("titel");
                String beschreibung = rs.getString("beschreibung");
                LocalDate start = rs.getString("start") != null ? LocalDate.parse(rs.getString("start")) : null;
                LocalDate ende = rs.getString("ende") != null ? LocalDate.parse(rs.getString("ende")) : null;
                int statusInt = rs.getInt("status");
                Status status;
                switch (statusInt) {
                case 0: 
                    status = Status.NEU;
                    break;
                case 1: 
                    status =  Status.ANGEFANGEN;
                    break;
                case 2: 
                    status =  Status.ERLEDIGT;
                    break;
                case 3:
                    status = Status.WARTEND;
                    break;
                default: 
                    status =  Status.NEU;
                    break;
                }
                int seiten = rs.getInt("seiten");
                String ergebnis = rs.getString("ergebnis");
                int einheiten = rs.getInt("einheiten");
                String einheitstypString = rs.getString("einheitstyp");
                Lerneinheit einheitstyp = Lerneinheit.EINHEIT;
                switch (einheitstypString) {
                case null: einheitstyp =  Lerneinheit.EINHEIT;
                case "EINHEIT": 
                    einheitstyp = Lerneinheit.EINHEIT;
                    break;
                case "KAPITEL": 
                    einheitstyp = Lerneinheit.KAPITEL;
                    break;
                case "LEKTION": 
                    einheitstyp = Lerneinheit.LEKTION;
                    break;
                case "KARTEN": 
                    einheitstyp = Lerneinheit.KARTEN;
                    break;
                case "MODUL": 
                    einheitstyp = Lerneinheit.MODUL;
                    break;
                case "ALTKLAUSUR": 
                    einheitstyp = Lerneinheit.ALTKLAUSUR;
                    break;
                default: einheitstyp =  Lerneinheit.EINHEIT;
                }
                
                int modulId = rs.getInt("modulId");
                Modul modul = null;
                if (!rs.wasNull()) {
                    modul = getModul(modulId);
                }

                Aufgabe aufgabe;
                Aufgabentyp typ = Aufgabentyp.valueOf(rs.getString("typ"));
                switch (typ) {
                    case ALLGEMEIN:
                        aufgabe = new AufgabeAllgemein(titel, beschreibung, ende, start, status, modul);
                        break;
                    case DURCHARBEITEN:
                        aufgabe = new AufgabeDurcharbeiten(titel, beschreibung, ende, start, status, modul, seiten);
                        break;
                    case EA:
                        aufgabe = new AufgabeEA(titel, beschreibung, ende, start, status, modul, ergebnis);
                        break;
                    case ALTKLAUSUR:
                        aufgabe = new AufgabeAltklausur(titel, beschreibung, ende, start, status, modul, ergebnis);
                        break;
                    case WIEDERHOLEN:
                        aufgabe = new AufgabeWiederholen(titel, beschreibung, ende, start, status, modul, einheiten, einheitstyp);
                        break;
                    default:
                        throw new IllegalArgumentException("Unbekannter Aufgabentyp: " + typ);
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
        String sql = "SELECT id, name, klausurTermin, aktuell, note, tageWiederholen FROM module";
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
                int id = rs.getInt("id");
                Modul modul = new Modul(name, termin, aktuell, note);
                modul.setId(id);
                modul.setTageWiederholen(tageWiederholen);
                module.add(modul);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return module;
    }

    public void upsertModul(Modul modul) {
        String sqlInsert = "INSERT INTO module (name, klausurTermin, aktuell, note, tageWiederholen) " +
                "VALUES (?, ?, ?, ?, ?) " +
                "ON CONFLICT(name) DO UPDATE SET " +
                "klausurTermin = excluded.klausurTermin, " +
                "aktuell = excluded.aktuell, " +
                "note = excluded.note, " +
                "tageWiederholen = excluded.tageWiederholen " +
                "RETURNING id;";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {

            pstmt.setString(1, modul.getName());
            if (modul.getKlausurTermin() != null) {
                pstmt.setString(2, modul.getKlausurTermin().toString());
            } else {
                pstmt.setNull(2, java.sql.Types.VARCHAR);
            }
            pstmt.setBoolean(3, modul.getAktuell());
            pstmt.setString(4, modul.getNote());
            pstmt.setInt(5, modul.getTageWiederholen());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    modul.setId(rs.getInt("id")); // Hier wird die ID endlich gesetzt!
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
    
    public void exportDatabase(File zielDatei) {
        File quelle = new File("data/Lernplaner.db");
        try {
            Files.copy(quelle.toPath(), zielDatei.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Datenbank exportiert nach: " + zielDatei.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importDatabase(File quellDatei) {
        File ziel = new File("data/Lernplaner.db");
        try {
            Files.copy(quellDatei.toPath(), ziel.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Datenbank importiert von: " + quellDatei.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
