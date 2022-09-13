package com.elcom.its.vds.tools;

import com.elcom.its.utils.StringUtil;
import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    public static String snakeCaseFormat(String name) {
        final StringBuilder result = new StringBuilder();

        boolean lastUppercase = false;

        for (int i = 0; i < name.length(); i++) {
            char ch = name.charAt(i);
            char lastEntry = i == 0 ? 'X' : result.charAt(result.length() - 1);
            if (ch == ' ' || ch == '_' || ch == '-' || ch == '.') {
                lastUppercase = false;

                if (lastEntry == '_') {
                    continue;
                } else {
                    ch = '_';
                }
            } else if (Character.isUpperCase(ch)) {
                ch = Character.toLowerCase(ch);
                // is start?
                if (i > 0) {
                    if (lastUppercase) {
                        // test if end of acronym
                        if (i + 1 < name.length()) {
                            char next = name.charAt(i + 1);
                            if (!Character.isUpperCase(next) && Character.isAlphabetic(next)) {
                                // end of acronym
                                if (lastEntry != '_') {
                                    result.append('_');
                                }
                            }
                        }
                    } else {
                        // last was lowercase, insert _
                        if (lastEntry != '_') {
                            result.append('_');
                        }
                    }
                }
                lastUppercase = true;
            } else {
                lastUppercase = false;
            }

            result.append(ch);
        }
        return result.toString();
    }

    public static String toSnakeCase(String camelCaseStr){
        if (camelCaseStr == null)
            return camelCaseStr;

        CaseFormat camelCase = CaseFormat.LOWER_CAMEL;
        CaseFormat snakeCase = CaseFormat.LOWER_UNDERSCORE;
        Converter<String, String> camelToSnake = camelCase.converterTo(snakeCase);
        return camelToSnake.convert(camelCaseStr);
    }

    public static Map<String, Object> normalizeJsonAttrs(Map<String, Object> map){
        if (map == null)
            return map;

        Map<String, Object> results = new HashMap<>();
        for(Map.Entry<String, Object> entry: map.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map){
                value = normalizeJsonAttrs((Map<String, Object>)value);
            }
            results.put(toSnakeCase(key), value);
        }

        return results;
    }

    public static String toCamelCase(String snakeCaseStr){
        if (snakeCaseStr == null)
            return snakeCaseStr;

        CaseFormat camelCase = CaseFormat.LOWER_CAMEL;
        CaseFormat snakeCase = CaseFormat.LOWER_UNDERSCORE;
        Converter<String, String> snakeToCamel = snakeCase.converterTo(camelCase);
        return snakeToCamel.convert(snakeCaseStr);
    }

    public static Map<String, Object> getFields(Object obj, List<String> ignoreFields, List<String> ignoreTypes) {
        Map<String, Object> results = new HashMap<>();
        Class clazz = obj.getClass();
        List<Field> fields = getAllFields(clazz);
        for(Field field: fields){
            try {
                if (ignoreFields != null && ignoreFields.contains(field.getName())) {
                    continue;
                }
                if (ignoreTypes != null && ignoreTypes.contains(field.getType().getName())) {
                    continue;
                }

                if (field.getType().isPrimitive() || field.getType().isEnum()) {
                    results.put(field.getName(), field.get(obj));
                }else {

                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return null;
    }

    public static List<Field> getAllFields(Class clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }

        List<Field> result = new ArrayList<>(getAllFields(clazz.getSuperclass()));
        List<Field> filteredFields = Arrays.stream(clazz.getDeclaredFields())
                //.filter(f -> Modifier.isPublic(f.getModifiers()) || Modifier.isProtected(f.getModifiers()))
                .collect(Collectors.toList());
        result.addAll(filteredFields);

        return result;
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }

    public static String getParams(Integer page, Integer size, String search)
            throws UnsupportedEncodingException {
        String params = null;
        if (!StringUtil.isNullOrEmpty(search)) {
            params = "page=" + page + "&size=" + size + "&search=" + URLEncoder.encode(search, StandardCharsets.UTF_8) + "&sort=id";

            logger.info("Param: " + params);
        } else {
            params = "page=" + page + "&size=" + size  + "&sort=id";
        }
        return params;
    }
}
