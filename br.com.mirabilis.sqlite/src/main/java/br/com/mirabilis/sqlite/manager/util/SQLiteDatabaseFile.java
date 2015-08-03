package br.com.mirabilis.sqlite.manager.util;

import java.util.regex.Pattern;

import br.com.mirabilis.sqlite.manager.exception.SQLiteException;


/**
 * Validate name of database;
 *
 * @author Rodrigo Sim�es Rosa
 */
public class SQLiteDatabaseFile {

    private String database;
    private String path;

    /**
     * Receipt {@link String} that of name from database;
     *
     * @param databaseName
     */
    private SQLiteDatabaseFile(String databaseName) throws SQLiteException {
        if (Pattern.matches("^[a-zA-Z]+$", databaseName)) {
            this.database = databaseName.concat(".db");
        } else {
            throw new SQLiteException("O nome da base de dados � inv�lido");
        }
    }

    /**
     * Return databaseName;
     *
     * @return
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Return path;
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * Return absolutePath
     *
     * @return
     */
    public String getAbsolutePath() {
        if (path != null) {
            return path.concat(database);
        }
        return database;
    }

    /**
     * Builder of {@link br.com.mirabilis.sqlite.manager.util.SQLiteDatabaseFile}
     *
     * @author Rodrigo Sim�es Rosa.
     */
    public static class Builder {

        private SQLiteDatabaseFile instance;

        /**
         * Constructor
         *
         * @param databaseName
         * @throws br.com.mirabilis.sqlite.manager.exception.SQLiteException
         */
        public Builder(String databaseName) throws SQLiteException {
            this.instance = new SQLiteDatabaseFile(databaseName);
        }

        /**
         * Set path of file
         *
         * @param path
         * @return
         */
        public Builder path(String path) {
            this.instance.path = path;
            return this;
        }

        /**
         * Build {@link br.com.mirabilis.sqlite.manager.util.SQLiteDatabaseFile}
         *
         * @return
         */
        public SQLiteDatabaseFile build() {
            return this.instance;
        }
    }
}
