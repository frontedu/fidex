package web.controlevacinacao.formatter;

import java.math.BigDecimal;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class BigDecimalFormatter extends NumberFormatter<BigDecimal> {

	private static final Logger logger = LoggerFactory.getLogger(BigDecimalFormatter.class);

	@Autowired
	private Environment env;

	@Override
	public String pattern(Locale locale) {
		logger.trace("Entrou no método pattern");
		String padrao = env.getProperty("bigdecimal.format", "#,##0.00");
		logger.debug("Pattern: {} usado para o locale: {}", padrao, locale);
		return padrao;
	}

}