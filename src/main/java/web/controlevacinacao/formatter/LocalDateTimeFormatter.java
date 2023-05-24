package web.controlevacinacao.formatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LocalDateTimeFormatter extends TemporalFormatter<LocalDateTime> {

	private static final Logger logger = LoggerFactory.getLogger(LocalDateTimeFormatter.class);

	@Autowired
	private Environment env;

	@Override
	public LocalDateTime parse(String text, DateTimeFormatter formatter) {
		logger.trace("Entrou no método parse");
		logger.debug("String recebida para converter: {} com o DateTimeFormatter: {}", text, formatter);
		return LocalDateTime.parse(text, formatter);
	}

	@Override
	public String pattern(Locale locale) {
		logger.trace("Entrou no método pattern");
		String padrao = env.getProperty("localdatetime.format-" + locale, "yyyy-MM-dd HH:mm");
		logger.debug("Pattern: {} usado para o locale: {}", padrao, locale);
		return padrao;
	}

}