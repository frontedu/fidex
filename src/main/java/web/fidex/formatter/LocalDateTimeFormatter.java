package web.fidex.formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeFormatter extends TemporalFormatter<LocalDateTime> {

	@Autowired
	private Environment env;

	@Override
	public LocalDateTime parse(String text, DateTimeFormatter formatter) {
		return LocalDateTime.parse(text, formatter);
	}

	@Override
	public String pattern(Locale locale) {
		String padrao = env.getProperty("localdatetime.format-" + locale, "yyyy-MM-dd HH:mm");
		return padrao;
	}

}