package web.fidex.repository.jooq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jooq.Field;
import org.jooq.SortField;
import org.springframework.data.domain.Sort;

public final class JooqSortUtil {

	private JooqSortUtil() {
	}

	public static List<SortField<?>> toSortFields(Sort sort, Map<String, Field<?>> mapping, SortField<?> fallback) {
		if (sort == null || sort.isEmpty()) {
			return fallback != null ? List.of(fallback) : Collections.emptyList();
		}

		List<SortField<?>> fields = new ArrayList<>();
		for (Sort.Order order : sort) {
			Field<?> field = mapping.get(order.getProperty());
			if (field == null) {
				continue;
			}
			fields.add(order.isAscending() ? field.asc() : field.desc());
		}

		if (fields.isEmpty() && fallback != null) {
			fields.add(fallback);
		}
		return fields;
	}
}
