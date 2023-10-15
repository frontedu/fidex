package web.fidex.formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LocalTimeFormatter extends TemporalFormatter<LocalTime> {

	@Autowired
	private Environment env;

	@Override
	public String pattern(Locale locale) {
		String padrao = env.getProperty("localtime.format-" + locale, "HH:mm");
		return padrao;
	}

	@Override
	public LocalTime parse(String text, DateTimeFormatter formatter) {
		return LocalTime.parse(text, formatter);
	}

}