package web.controlevacinacao.formatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class LocalDateFormatter extends TemporalFormatter<LocalDate> {

	private static final Logger logger = LoggerFactory.getLogger(LocalDateFormatter.class);

	@Autowired
	private Environment env;

	@Override
	public String pattern(Locale locale) {
		logger.trace("Entrou no método pattern");
		String padrao = env.getProperty("localdate.format-" + locale, "yyyy-MM-dd");
		logger.debug("Pattern: {} usado para o locale: {}", padrao, locale);
		return padrao;
	}

	@Override
	public LocalDate parse(String text, DateTimeFormatter formatter) {
		logger.trace("Entrou no método parse");
		logger.debug("String recebida para converter: {} com o DateTimeFormatter: {}", text, formatter);
		return LocalDate.parse(text, formatter);
	}

}