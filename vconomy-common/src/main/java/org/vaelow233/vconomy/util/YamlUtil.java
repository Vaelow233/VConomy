package org.vaelow233.vconomy.util;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;

import java.io.InputStream;

public class YamlUtil {

    private YamlUtil() {}

    public static <T> T loadAs(InputStream in, Class<T> rootClass) {
        LoaderOptions options = new LoaderOptions();
        Constructor constructor = new Constructor(rootClass, options);

        PropertyUtils propertyUtils = new PropertyUtils() {
            @Override
            public Property getProperty(Class<?> type, String name) {
                return super.getProperty(type, snakeToCamel(name));
            }
        };
        propertyUtils.setBeanAccess(BeanAccess.FIELD);
        propertyUtils.setSkipMissingProperties(true);
        constructor.setPropertyUtils(propertyUtils);

        Yaml yaml = new Yaml(constructor);
        return yaml.load(in);
    }

    private static String snakeToCamel(String s) {
        if (s == null || s.indexOf('_') < 0) return s;
        StringBuilder sb = new StringBuilder(s.length());
        boolean upperNext = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_') {
                upperNext = true;
            } else if (upperNext) {
                sb.append(Character.toUpperCase(c));
                upperNext = false;
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
