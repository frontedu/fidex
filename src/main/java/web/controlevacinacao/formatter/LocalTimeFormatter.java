package web.controlevacinacao.formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LocalTimeFormatter extends TemporalFormatter<LocalTime> {

	private static final Logger logger = LoggerFactory.getLogger(LocalTimeFormatter.class);

	@Autowired
	private Environment env;

	@Override
	public String pattern(Locale locale) {
		logger.trace("Entrou no método pattern");
		String padrao = env.getProperty("localtime.format-" + locale, "HH:mm");
		logger.debug("Pattern: {} usado para o locale: {}", padrao, locale);
		return padrao;
	}

	@Override
	public LocalTime parse(String text, DateTimeFormatter formatter) {
		logger.trace("Entrou no método parse");
		logger.debug("String recebida para converter: {} com o DateTimeFormatter: {}", text, formatter);
		return LocalTime.parse(text, formatter);
	}

}