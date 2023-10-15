package web.fidex.formatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

public abstract class NumberFormatter<T extends Number> implements Formatter<T> {


	@Override
	public String print(T number, Locale locale) {
		String padrao = pattern(locale);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
		NumberFormat numberFormat = new DecimalFormat(padrao, dfs);
		String retorno = numberFormat.format(number);
		return retorno;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T parse(String text, Locale locale) throws ParseException {
		String padrao = pattern(locale);
		DecimalFormatSymbols dfs = new DecimalFormatSymbols(locale);
		DecimalFormat decimalFormat = new DecimalFormat(padrao, dfs);
		decimalFormat.setParseBigDecimal(true);
		T objeto = (T) decimalFormat.parse(text);
		return objeto;
	}

	public abstract String pattern(Locale locale);

}
