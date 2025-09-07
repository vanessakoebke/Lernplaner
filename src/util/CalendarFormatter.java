package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

import lang.I18n;

public class CalendarFormatter extends AbstractFormatter {
    private SimpleDateFormat dateFormatter = new SimpleDateFormat(I18n.t("Common.Datumsformat_Java"));

    @Override
    public Object stringToValue(String text) throws ParseException {
        return Calendar.getInstance(); // optional: parse Text, hier einfach aktuelles Datum
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        if (value != null && value instanceof Calendar) {
            Calendar cal = (Calendar) value;
            return dateFormatter.format(cal.getTime());
        }
        return "";
    }
}

