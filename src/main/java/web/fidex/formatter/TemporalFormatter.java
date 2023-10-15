package web.fidex.formatter;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Locale;
import org.springframework.format.Formatter;

public abstract class TemporalFormatter<T extends Temporal> implements Formatter<T> {

	@Override
	public String print(T temporal, Locale locale) {
		DateTimeFormatter formatter = getDateTimeFormatter(locale);
		String retorno = formatter.format(temporal);
		return retorno;
	}

	@Override
	public T parse(String text, Locale locale) throws ParseException {
		DateTimeFormatter formatter = getDateTimeFormatter(locale);
		T objeto = parse(text, formatter);
		return objeto;
	}

	private DateTimeFormatter getDateTimeFormatter(Locale locale) {
		String padrao = pattern(locale);
		return DateTimeFormatter.ofPattern(padrao);
	}

	public abstract String pattern(Locale locale);

	public abstract T parse(String text, DateTimeFormatter formatter);
}