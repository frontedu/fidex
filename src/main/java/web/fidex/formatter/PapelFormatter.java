package web.fidex.formatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;
import web.fidex.model.fidex_model.Papel;
import web.fidex.repository.PapelRepository;

import java.text.ParseException;
import java.util.Locale;

@Component
public class PapelFormatter implements Formatter<Papel> {

    @Autowired
    private PapelRepository papelRepository;

    @Override
    public Papel parse(String text, Locale locale) throws ParseException {
        if (text == null || text.isEmpty()) {
            return null;
        }
        try {
            Long codigo = Long.parseLong(text);
            return papelRepository.findById(codigo).orElse(null);
        } catch (NumberFormatException e) {
            throw new ParseException("Código de papel inválido: " + text, 0);
        }
    }

    @Override
    public String print(Papel papel, Locale locale) {
        return papel != null ? papel.getCodigo().toString() : "";
    }
}
