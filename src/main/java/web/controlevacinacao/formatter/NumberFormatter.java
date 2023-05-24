package web.controlevacinacao.formatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.Formatter;

public abstract class NumberFormatter<T extends Number> implements Formatter<T> {

	private static final Logger logger = LoggerFactory.getLogger(NumberFormatter.class);

	@Override
	public String print(T number, Locale locale) {
		logger.trace("Entrou em print");
		logger.debug("Objeto recebido: {}, Locale: {}", number, locale);
		String padrao = pattern(locale);
		logger.debug("Padrao usado no DecimalFormat: {}", padrao);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
		logger.debug("Separador decimal do DecimalFormatSymbols: {}", dfs.getDecimalSeparator());
		logger.debug("Separador de grupo do DecimalFormatSymbols: {}", dfs.getGroupingSeparator());
		NumberFormat numberFormat = new DecimalFormat(padrao, dfs);
		String retorno = numberFormat.format(number);
		logger.debug("String a retornar: {}", retorno);
		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T parse(String text, Locale locale) throws ParseException {
		logger.trace("Entrou em parse");
		logger.debug("String recebida: {}, Locale: {}", text, locale);
		String padrao = pattern(locale);
		logger.debug("Padrao usado no DecimalFormat: {}", padrao);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
		logger.debug("Separador decimal do DecimalFormatSymbols: {}", dfs.getDecimalSeparator());
		logger.debug("Separador de grupo do DecimalFormatSymbols: {}", dfs.getGroupingSeparator());
		DecimalFormat decimalFormat = new DecimalFormat(padrao, dfs);
		decimalFormat.setParseBigDecimal(true);
		T objeto = (T) decimalFormat.parse(text);
		logger.debug("Objeto a retornar: {}", objeto);
		return objeto;
	}

	public abstract String pattern(Locale locale);

}
