package org.seamoo.persistence.daos.jdo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Id;

public class JdoPersistenceHelper {
	public static <T> Object findPrimaryKey(T entity) {
		if (entity == null) {
			return null;
		}
		for (Field l : entity.getClass().getDeclaredFields()) {
			if (l.getAnnotation(PrimaryKey.class) != null || l.getAnnotation(Id.class) != null) {
				l.setAccessible(true);
				try {
					return l.get(entity);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		throw new IllegalArgumentException(String.format("Entity of type %s doesn't have PrimaryKey/Id",
				entity.getClass().getName()));
	}

	public static <T> void copyPersistentFields(T fromEntity, T toEntity) {
		for (Method setter : fromEntity.getClass().getMethods()) {
			if (setter.getName().startsWith("set") && Character.isUpperCase(setter.getName().charAt(3))) {
				setter.setAccessible(true);
				try {
					Method getter = fromEntity.getClass().getMethod("get" + setter.getName().substring(3));
					if (getter == null) {
						// either try getSomething or isSomething
						getter = fromEntity.getClass().getMethod("is" + setter.getName().substring(3));
					}
					getter.setAccessible(true);
					setter.invoke(toEntity, getter.invoke(fromEntity));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
