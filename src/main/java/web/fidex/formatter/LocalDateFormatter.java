package web.fidex.formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LocalDateFormatter extends TemporalFormatter<LocalDate> {

	@Autowired
	private Environment env;

	@Override
	public String pattern(Locale locale) {
		String padrao = env.getProperty("localdate.format-" + locale, "yyyy-MM-dd");
		return padrao;
	}

	@Override
	public LocalDate parse(String text, DateTimeFormatter formatter) {
		return LocalDate.parse(text, formatter);
	}

}