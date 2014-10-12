package me.lory.irc;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Configuration {

    public enum Field {
        NICK, NAME,
    }

    private final Map<Field, String> userConfig;

    private Configuration(Builder b) {
        this.userConfig = new EnumMap<>(Field.class);

        this.putIfNotNull(Field.NICK, b.nick);
        this.putIfNotNull(Field.NAME, b.realName);
    }

    private void putIfNotNull(Field key, String value) {
        if (value != null) {
            this.userConfig.put(key, value);
        }
    }

    /**
     * Check the presence of the given field.
     * 
     * @param field
     * @return True if field was parsed, false otherwise.
     */
    public boolean containsField(Field field) {
        return this.userConfig.containsKey(field);
    }

    /**
     * Returns the configuration value mapped to the Field field. If no value is
     * found, returns null.
     * 
     * @param field
     * @return Value mapped to field field. If no value is mapped, null.
     */
    public String getValue(Field field) {
        return this.userConfig.get(field);
    }

    private static class Builder {
        private String nick, realName;

        public Builder setNick(String n) {
            this.nick = n;
            return this;
        }

        public Builder setRealName(String name) {
            this.realName = name;
            return this;
        }

        public Configuration build() {
            return new Configuration(this);
        }
    }

    public static class Parser {
        private Configuration parsed;
        private final Configuration.Builder builder;
        private final BufferedReader config;

        public Parser(BufferedReader reader) {
            this.config = reader;
            this.builder = new Builder();
            this.parsed = null;
        }

        public Configuration parse() throws IOException {
            if (this.parsed != null) {
                return this.parsed;
            }

            Pattern pattern = Pattern.compile("(\\w+)=(.+)");
            String line;
            List<String> seen = new ArrayList<>();
            while ((line = this.config.readLine()) != null) {
                Matcher m = pattern.matcher(line);
                if (!m.matches() || m.groupCount() != 2) {
                    Lory.LOG.log(Level.WARNING, String.format("Skipping invalid configuration: %s", line));
                    continue;
                }

                String field = m.group(1);

                if (seen.contains(field)) {
                    Lory.LOG.log(Level.WARNING, String.format("Duplicate configuration entry %s. Skipping...", field));
                    continue;
                }

                seen.add(field);

                if (field.toUpperCase().equals("NICK")) {
                    builder.setNick(m.group(2));
                } else if (field.toUpperCase().equals("NAME")) {
                    builder.setRealName(m.group(2));
                }
            }

            return this.builder.build();
        }
    }
}
