package ui;



    import java.awt.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.*;

import org.jdatepicker.impl.*;

import lang.I18n;
import model.Lerngruppe;
import model.Modul;
import service.Control;
import service.Kalender;
import util.CalendarFormatter;

    public class EingabePanelLg extends JPanel implements IAnsicht {
        private boolean neueingabe = false;
        private Lerngruppe lg;
        private JComboBox<Modul> modulFeld;
        private JTextField titelFeld;
        private JComboBox<DayOfWeek> wochentagFeld;
        private JSpinner spinner;
        private JDatePickerImpl endeFeld;
        private Control control;

        // Konstruktor für neue Aufgabe
        public EingabePanelLg(Control control) {
            this.control = control;
            this.lg = null;
            this.neueingabe = true;
            initUI();
        }

        // Konstruktor zum Bearbeiten bestehender Aufgabe
        public EingabePanelLg(Lerngruppe lg, Control control) {
            this.control = control;
            this.lg = lg;
            this.neueingabe = false;
            initUI();
            setLg(lg);
        }

        // GUI-Aufbau
        private void initUI() {
            DateTimeFormatter formatter = control.getEinstellungen().getDatumsformat();
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            // Modul
            JLabel modulLabel = new JLabel(I18n.t("model.Aufgabentyp.Modul"));
            modulFeld = new JComboBox<>(control.getMm().getAktuelleModule().toArray(new Modul[0]));
            if (!neueingabe) {
                modulFeld.setEnabled(false);
            }
            gbc.gridx = 0;
            gbc.gridy = 0;
            this.add(modulLabel, gbc);
            gbc.gridx = 1;
            this.add(modulFeld, gbc);
            
            // Titel
            JLabel titelLabel = new JLabel(I18n.t("ui.Common.Name"));
            titelFeld = new JTextField(I18n.t("ui.Lerngruppe.LG_abr") + ((Modul) modulFeld.getSelectedItem()).getName());
            titelFeld.setPreferredSize(new Dimension(200, 25));
            titelFeld.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
            gbc.gridx = 0;
            gbc.gridy = 5;
            this.add(titelLabel, gbc);
            gbc.gridx = 1;
            this.add(titelFeld, gbc);

            // Wochentag
            JLabel wochentagLabel = new JLabel(I18n.t("ui.Common.Wochentag"));
            wochentagFeld = new JComboBox<>(DayOfWeek.values());
            gbc.gridx = 0;
            gbc.gridy = 12;
            this.add(wochentagLabel, gbc);
            gbc.gridx = 1;
            this.add(wochentagFeld, gbc);
            
            //Uhrzeit
            JLabel uhrzeitLabel = new JLabel(I18n.t("ui.Common.Uhrzeit"));
            SpinnerDateModel model = new SpinnerDateModel();
            spinner = new JSpinner(model);
            spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
            gbc.gridx = 0;
            gbc.gridy = 12;
            this.add(uhrzeitLabel, gbc);
            gbc.gridx = 1;
            this.add(spinner, gbc);
            
            // Enddatum
            JLabel endeLabel = new JLabel(I18n.t("ui.Common.Ende"));
            UtilDateModel endeModel = new UtilDateModel();
            endeModel.setDate(((Modul) modulFeld.getSelectedItem()).getKlausurTermin().getYear(), ((Modul) modulFeld.getSelectedItem()).getKlausurTermin().getMonthValue() -1, ((Modul) modulFeld.getSelectedItem()).getKlausurTermin().getDayOfMonth());
            endeModel.setSelected(true);
            JDatePanelImpl endeDatePanel = new JDatePanelImpl(endeModel, Kalender.getCalendarProperties());
            endeFeld = new JDatePickerImpl(endeDatePanel, new CalendarFormatter());
            gbc.gridx = 0;
            gbc.gridy = 15;
            this.add(endeLabel, gbc);
            gbc.gridx = 1;
            this.add(endeFeld, gbc);
            
            //Action-Listener
            modulFeld.addActionListener(e->{
                titelFeld.setText(I18n.t("ui.Lerngruppe.LG_abr") + ((Modul) modulFeld.getSelectedItem()).getName());
                endeModel.setDate(((Modul) modulFeld.getSelectedItem()).getKlausurTermin().getYear(), ((Modul) modulFeld.getSelectedItem()).getKlausurTermin().getMonthValue() -1, ((Modul) modulFeld.getSelectedItem()).getKlausurTermin().getDayOfMonth());
                endeModel.setSelected(true);
            });

        }

        public Lerngruppe getLg() {
            DayOfWeek wochentag = (DayOfWeek)wochentagFeld.getSelectedItem();
            Date date = (Date) spinner.getValue();
            LocalTime uhrzeit = date.toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalTime();
            Date endDate = (Date) endeFeld.getModel().getValue();
            LocalDate ende = endDate != null ? endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
            if (neueingabe) {
                return new Lerngruppe((Modul) modulFeld.getSelectedItem(), titelFeld.getText(), wochentag, uhrzeit, ende);
            } else {
                lg.setTitel(titelFeld.getText());
                lg.setWochentag(wochentag);
                lg.setUhrzeit(uhrzeit);
                lg.setEnde(ende);
                return lg;
            }
        }

        private void setLg(Lerngruppe lg) {
            this.lg = lg;
            this.neueingabe = false;
            // Modul nach ID suchen und auswählen
            Modul modul = lg.getModul();
            for (int i = 0; i < modulFeld.getItemCount(); i++) {
                Modul m = modulFeld.getItemAt(i);
                if (m.getId() == modul.getId()) { // falls du IDs hast
                    modulFeld.setSelectedIndex(i);
                    break;
                }
            }
            titelFeld.setText(lg.getTitel());
            if (lg.getEnde() != null) {
                ((UtilDateModel) endeFeld.getModel())
                        .setValue(Date.from(lg.getEnde().atStartOfDay(ZoneId.systemDefault()).toInstant()));
                ((UtilDateModel) endeFeld.getModel()).setSelected(true);
            }
            wochentagFeld.setSelectedItem(lg.getWochentag());

            // Uhrzeit setzen:
            ZoneId zone = ZoneId.systemDefault();
            Date date = Date.from(lg.getUhrzeit().atDate(LocalDate.now()).atZone(zone).toInstant());
            SpinnerDateModel model = new SpinnerDateModel();
            spinner.setEditor(new JSpinner.DateEditor(spinner, "HH:mm"));
            model.setValue(date);
        }

        
        @Override
        public void refresh() {
            // kann bei Bedarf implementiert werden
        }
    }


    

