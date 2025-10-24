package service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.time.*;
import java.util.*;

import model.*;

public class Datenbank {
    private static final String DB_URL = "jdbc:sqlite:data/Lernplaner.db";
    private Control control;
    
    public Datenbank(Control control) {
        this.control = control;
    }

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public void init() {
        createModuleTable();
        createAufgabenTable();
        createLerngruppeTable();
        createLerngruppeTermineTable();
    }


    private void createModuleTable() {
        String sql = "CREATE TABLE IF NOT EXISTS module (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," // Tabelle anpassen
                                                                                                     // hier!
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

//@formatter:off
    private void createAufgabenTable() {
        String sql = "CREATE TABLE IF NOT EXISTS aufgabe (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "typ TEXT NOT NULL, " +
                "titel TEXT NOT NULL, " +
                "beschreibung TEXT, " +
                "start TEXT, " +
                "ende TEXT, " +
                "status INTEGER NOT NULL, " +
                "modulId INTEGER, " +
                "seiten INTEGER, " +
                "ergebnis TEXT, " +
                "einheiten INTEGER, " +
                "einheitstyp TEXT," +
                "FOREIGN KEY(modulId) REFERENCES module(id)" +
                ");";
        //@formatter:on
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            // --- ALTER TABLE nur wenn Spalte noch nicht existiert ---
            // Prüfen, ob folgeaufgabe_id schon da ist
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(aufgabe);");
            boolean spalteExistiert = false;
            while (rs.next()) {
                String columnName = rs.getString("name");
                if ("folgeaufgabe_id".equalsIgnoreCase(columnName)) {
                    spalteExistiert = true;
                    break;
                }
            }
            rs.close();
            // Wenn Spalte nicht existiert, hinzufügen
            if (!spalteExistiert) {
                String alterSql = "ALTER TABLE aufgabe ADD COLUMN folgeaufgabe_id INTEGER;";
                stmt.execute(alterSql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void upsertAufgabe(Aufgabe aufgabe) {
        boolean neu = aufgabe.getId() == null;

        String sqlInsert = "INSERT INTO aufgabe " +
                "(typ, titel, beschreibung, start, ende, status, modulId, seiten, ergebnis, einheiten, einheitstyp, folgeaufgabe_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE aufgabe SET typ=?, titel=?, beschreibung=?, start=?, ende=?, status=?, modulId=?, " +
                "seiten=?, ergebnis=?, einheiten=?, einheitstyp=?, folgeaufgabe_id=? WHERE id=?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(neu ? sqlInsert : sqlUpdate, Statement.RETURN_GENERATED_KEYS)) {

            int idx = 1;
            pstmt.setString(idx++, aufgabe.getTyp().name());
            pstmt.setString(idx++, aufgabe.getTitel());
            pstmt.setString(idx++, aufgabe.getBeschreibung() != null ? aufgabe.getBeschreibung() : null);
            pstmt.setString(idx++, aufgabe.getStart() != null ? aufgabe.getStart().toString() : null);
            pstmt.setString(idx++, aufgabe.getEnde() != null ? aufgabe.getEnde().toString() : null);
            pstmt.setInt(idx++, aufgabe.getStatusIndex());
            pstmt.setInt(idx++, aufgabe.getModul() != null && aufgabe.getModul().getId() != null ? aufgabe.getModul().getId() : null);

            if (aufgabe instanceof AufgabeDurcharbeiten)
                pstmt.setInt(idx++, ((AufgabeDurcharbeiten) aufgabe).getSeiten());
            else
                pstmt.setNull(idx++, Types.INTEGER);

            if (aufgabe instanceof AufgabeEA)
                pstmt.setString(idx++, ((AufgabeEA) aufgabe).getErgebnis());
            else if (aufgabe instanceof AufgabeAltklausur)
                pstmt.setString(idx++, ((AufgabeAltklausur) aufgabe).getErgebnis());
            else
                pstmt.setNull(idx++, Types.VARCHAR);

            if (aufgabe instanceof AufgabeWiederholen) {
                pstmt.setInt(idx++, ((AufgabeWiederholen) aufgabe).getEinheiten());
                pstmt.setString(idx++, ((AufgabeWiederholen) aufgabe).getEinheitstyp().name());
            } else {
                pstmt.setNull(idx++, Types.INTEGER);
                pstmt.setNull(idx++, Types.VARCHAR);
            }

            // ✅ Folgeaufgabe richtig speichern
            if (aufgabe.getFolgeAufgabe() != null && aufgabe.getFolgeAufgabe().getId() != null)
                pstmt.setInt(idx++, aufgabe.getFolgeAufgabe().getId());
            else
                pstmt.setNull(idx++, Types.INTEGER);

            // WHERE id nur bei UPDATE
            if (!neu) {
                pstmt.setInt(idx++, aufgabe.getId());
            }

            pstmt.executeUpdate();

            // generierte ID auslesen bei neuem Insert
            if (neu) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        aufgabe.setId(keys.getInt(1));
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
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
        Map<Integer, Aufgabe> idMap = new HashMap<>();

        String sql = "SELECT id, typ, titel, beschreibung, start, ende, status, modulId, seiten, ergebnis, einheiten, einheitstyp, folgeaufgabe_id FROM aufgabe";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            // 1️⃣ Alle Aufgaben zunächst ohne Folgeaufgabe erzeugen
            while (rs.next()) {
                int id = rs.getInt("id");
                String titel = rs.getString("titel");
                String beschreibung = rs.getString("beschreibung");
                LocalDate start = rs.getString("start") != null ? LocalDate.parse(rs.getString("start")) : null;
                LocalDate ende = rs.getString("ende") != null ? LocalDate.parse(rs.getString("ende")) : null;
                Status status = Status.values()[rs.getInt("status")];

                int seiten = rs.getInt("seiten");
                String ergebnis = rs.getString("ergebnis");
                int einheiten = rs.getInt("einheiten");
                String einheitstypString = rs.getString("einheitstyp");
                Lerneinheit einheitstyp = Lerneinheit.EINHEIT;
                if (einheitstypString != null) {
                    try { einheitstyp = Lerneinheit.valueOf(einheitstypString); } catch (IllegalArgumentException ignored) {}
                }

                int modulId = rs.getInt("modulId");
                Modul modul = !rs.wasNull() ? getModul(modulId) : null;

                Aufgabentyp typ = Aufgabentyp.valueOf(rs.getString("typ"));
                Aufgabe aufgabe;
                switch (typ) {
                    case ALLGEMEIN -> aufgabe = new AufgabeAllgemein(titel, beschreibung, ende, start, status, modul);
                    case DURCHARBEITEN -> aufgabe = new AufgabeDurcharbeiten(titel, beschreibung, ende, start, status, modul, seiten, null);
                    case EA -> aufgabe = new AufgabeEA(titel, beschreibung, ende, start, status, modul, ergebnis, null);
                    case ALTKLAUSUR -> aufgabe = new AufgabeAltklausur(titel, beschreibung, ende, start, status, modul, ergebnis, null);
                    case WIEDERHOLEN -> aufgabe = new AufgabeWiederholen(titel, beschreibung, ende, start, status, modul, einheiten, einheitstyp, null);
                    case LG -> aufgabe = new AufgabeLerngruppe(titel, beschreibung, ende, start, status, modul, null);
                    default -> throw new IllegalArgumentException("Unbekannter Aufgabentyp: " + typ);
                }

                aufgabe.setId(id);
                idMap.put(id, aufgabe); // Map zum Lookup nach ID
                aufgaben.add(aufgabe);
            }

            // 2️⃣ Folgeaufgaben zuweisen
            try (ResultSet rs2 = pstmt.executeQuery()) {
                while (rs2.next()) {
                    int id = rs2.getInt("id");
                    int folgeId = rs2.getInt("folgeaufgabe_id");
                    if (!rs2.wasNull()) {
                        Aufgabe aufgabe = idMap.get(id);
                        Aufgabe folgeAufgabe = idMap.get(folgeId);
                        if (aufgabe != null) aufgabe.setFolgeAufgabe(folgeAufgabe);
                    }
                }
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
                LocalDate termin = rs.getString("klausurTermin") != null
                        ? LocalDate.parse(rs.getString("klausurTermin")) // Tabelle anpassen hier!
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
        String sqlInsert = "INSERT INTO module (name, klausurTermin, aktuell, note, tageWiederholen) "
                + "VALUES (?, ?, ?, ?, ?) " + "ON CONFLICT(name) DO UPDATE SET "
                + "klausurTermin = excluded.klausurTermin, " + "aktuell = excluded.aktuell, " + "note = excluded.note, "
                + "tageWiederholen = excluded.tageWiederholen " + "RETURNING id;";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {
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
        String sql = "SELECT id, name, klausurTermin, aktuell, note, tageWiederholen FROM module WHERE id = ?"; // Tabelle
                                                                                                                // anpassen
                                                                                                                // hier!
        Modul modul = null;
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    LocalDate termin = rs.getString("klausurTermin") != null
                            ? LocalDate.parse(rs.getString("klausurTermin"))
                            : null;
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void importDatabase(File quellDatei) {
        File ziel = new File("data/Lernplaner.db");
        try {
            Files.copy(quellDatei.toPath(), ziel.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Aufgabe getAufgabeById(int id) {
        List<Aufgabe> alle = getAufgaben();
        for (Aufgabe a : alle) {
            if (a.getId() == id) return a;
        }
        return null;
    }

    private void createLerngruppeTable() {
        String sql = "CREATE TABLE IF NOT EXISTS lerngruppe (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "modulId INTEGER, " +
                "titel TEXT NOT NULL, " +
                "wochentag INTEGER, " +           // DayOfWeek (1=Montag ... 7=Sonntag)
                "uhrzeit TEXT, " +                // LocalTime als String
                "ende TEXT, " +        // Endtermin
                "FOREIGN KEY(modulId) REFERENCES module(id)" +
                ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createLerngruppeTermineTable() {
        String sql = "CREATE TABLE IF NOT EXISTS lerngruppe_termine (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "lerngruppe_id INTEGER NOT NULL, " +
                "datum TEXT NOT NULL, " +
                "uhrzeit TEXT NOT NULL, " +
                "aufgabe_id INTEGER, " +
                "FOREIGN KEY(lerngruppe_id) REFERENCES lerngruppe(id), " +
                "FOREIGN KEY(aufgabe_id) REFERENCES aufgabe(id)" +
                ");";
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void upsertLg(Lerngruppe lg) {
        boolean neu = lg.getId() == null;

        String sqlInsert = "INSERT INTO lerngruppe (modulId, titel, wochentag, uhrzeit, ende) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdate = "UPDATE lerngruppe SET modulId=?, titel=?, wochentag=?, uhrzeit=?, ende=? WHERE id=?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(neu ? sqlInsert : sqlUpdate, Statement.RETURN_GENERATED_KEYS)) {

            // --- Lerngruppe speichern ---
            pstmt.setInt(1, lg.getModul() != null ? lg.getModul().getId() : Types.NULL);
            pstmt.setString(2, lg.getTitel());
            pstmt.setInt(3, lg.getWochentag().getValue());
            pstmt.setString(4, lg.getUhrzeit() != null ? lg.getUhrzeit().toString() : null);
            pstmt.setString(5, lg.getEnde() != null ? lg.getEnde().toString() : null);

            if (!neu) {
                pstmt.setInt(6, lg.getId());
            }

            pstmt.executeUpdate();

            if (neu) {
                try (ResultSet keys = pstmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        lg.setId(keys.getInt(1));
                    }
                }
            }

            // --- Erst die Aufgaben speichern! ---
            if (lg.getTermine() != null) {
                for (Lerngruppe.Termin t : lg.getTermine()) {
                    if (t.getAufgabe() != null && t.getAufgabe().getId() == null) {
                        upsertAufgabe(t.getAufgabe()); // <-- hier
                    }
                }
            }

            // --- Dann die Termine speichern ---
            upsertTermine(lg);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void upsertTermine(Lerngruppe lg) {
        if (lg.getTermine() == null) return;

        String sqlInsert = "INSERT INTO lerngruppe_termine (lerngruppe_id, datum, uhrzeit, aufgabe_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlInsert)) {

            for (Lerngruppe.Termin t : lg.getTermine()) {
                pstmt.setInt(1, lg.getId());
                pstmt.setString(2, t.getDatum().toString());
                pstmt.setString(3, t.getUhrzeitTermin() != null ? t.getUhrzeitTermin().toString() : null);

                // Aufgabe prüfen und ggf. als AufgabeLerngruppe anlegen
                Aufgabe aufgabe = t.getAufgabe();
                if (aufgabe != null && aufgabe.getId() == null) {
                    AufgabeLerngruppe lgAufgabe = new AufgabeLerngruppe(
                            aufgabe.getTitel(),
                            aufgabe.getBeschreibung(),
                            aufgabe.getEnde(),
                            aufgabe.getStart(),
                            aufgabe.getStatus(),
                            aufgabe.getModul(), null
                    );
                    upsertAufgabe(lgAufgabe); // In DB speichern, damit ID gesetzt wird
                    t.setAufgabe(lgAufgabe);
                }

                // ID der Aufgabe in DB speichern
                if (t.getAufgabe() != null && t.getAufgabe().getId() != null) {
                    pstmt.setInt(4, t.getAufgabe().getId());
                } else {
                    pstmt.setNull(4, Types.INTEGER);
                }

                pstmt.addBatch();
            }

            pstmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public List<Lerngruppe> getLerngruppen() {
        List<Lerngruppe> lerngruppen = new ArrayList<>();

        String sql = "SELECT id, titel, wochentag, uhrzeit, ende, modulId FROM lerngruppe";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String titel = rs.getString("titel");
                DayOfWeek wochentag = DayOfWeek.of(rs.getInt("wochentag"));
                LocalTime uhrzeit = rs.getString("uhrzeit") != null ? LocalTime.parse(rs.getString("uhrzeit")) : null;
                LocalDate ende = rs.getString("ende") != null ? LocalDate.parse(rs.getString("ende")) : null;
                int modulId = rs.getInt("modulId");
                Modul modul = !rs.wasNull() ? getModul(modulId) : null;

                Lerngruppe lg = new Lerngruppe(modul, titel, wochentag, uhrzeit, ende);
                lg.setId(id);
                lg.setTermine(getTermine(lg));

                lerngruppen.add(lg);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lerngruppen;
    }

    
    public void removeLerngruppe(Lerngruppe lg) {
        if (lg.getId() == null) return; // Lerngruppe existiert nicht in DB

        try (Connection conn = connect()) {
            conn.setAutoCommit(false); // Alles als Transaktion, damit wir bei Fehlern zurückrollen können

            // 1️⃣ Alle Aufgaben der Lerngruppe löschen
            String deleteAufgabenSql = "DELETE FROM aufgabe WHERE id IN (SELECT aufgabe_id FROM lerngruppe_termine WHERE lerngruppe_id = ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteAufgabenSql)) {
                pstmt.setInt(1, lg.getId());
                pstmt.executeUpdate();
            }

            // 2️⃣ Alle Termine der Lerngruppe löschen
            String deleteTermineSql = "DELETE FROM lerngruppe_termine WHERE lerngruppe_id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteTermineSql)) {
                pstmt.setInt(1, lg.getId());
                pstmt.executeUpdate();
            }

            // 3️⃣ Lerngruppe selbst löschen
            String deleteLgSql = "DELETE FROM lerngruppe WHERE id = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(deleteLgSql)) {
                pstmt.setInt(1, lg.getId());
                pstmt.executeUpdate();
            }

            conn.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connect().rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }



    private List<Lerngruppe.Termin> getTermine(Lerngruppe lg) {
        List<Lerngruppe.Termin> termine = new ArrayList<>();

        String sql = "SELECT id, datum, uhrzeit, aufgabe_id FROM lerngruppe_termine WHERE lerngruppe_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, lg.getId());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate datum = LocalDate.parse(rs.getString("datum"));
                    LocalTime uhrzeit = rs.getString("uhrzeit") != null ? LocalTime.parse(rs.getString("uhrzeit")) : null;
                    Integer aufgabeId = rs.getInt("aufgabe_id");
                    AufgabeLerngruppe aufgabe = null;

                    if (!rs.wasNull()) {
                        Aufgabe a = getAufgabeById(aufgabeId);
                        if (a instanceof AufgabeLerngruppe) {
                            aufgabe = (AufgabeLerngruppe) a;
                        }
                    }
                    Lerngruppe.Termin t = lg.new Termin(datum, aufgabe);
                    t.setUhrzeitTermin(uhrzeit);
                    termine.add(t);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return termine;
    }


}
