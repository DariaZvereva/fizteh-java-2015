package ru.fizteh.fivt.students.DariaZvereva.MiniORM;

import com.google.common.base.CaseFormat;
import org.h2.jdbcx.JdbcConnectionPool;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseService<T>  {
    private String connectionName;
    private String userName;
    private String password;

    private String tableName;
    private Class<T> bdClass;
    private Field[] fields;
    private String[] namesForColumns;
    private int primaryKey = -1;


    private JdbcConnectionPool connectionPool;


    DatabaseService(Class<T> type) throws IOException {
        /*Properties properties = new Properties();
        try (InputStream inputStream = this.getClass().getResourceAsStream("/h2.properties")) {
            properties.load(inputStream);
        }*/
        //connectionName = properties.getProperty("connection_name");
        //userName = properties.getProperty("login");
        //password = properties.getProperty("password");
        bdClass = type;

        if (!bdClass.isAnnotationPresent(Table.class)) {
            throw new IllegalArgumentException("Class is not a Table");
        }

        if (Objects.equals(bdClass.getAnnotation(Table.class).name(), "")) {
            tableName = bdClass.getSimpleName();
        } else {
            tableName = bdClass.getAnnotation(Table.class).name();
        }

        List<String> columnNames = new ArrayList<>();
        List<Field> columns = new ArrayList<>();
        for (Field currColumn : bdClass.getDeclaredFields()) {
            if (!currColumn.isAnnotationPresent(Column.class)) {
                throw new IllegalArgumentException("Not all fields are columns");
            }
            String currColumnName = currColumn.getAnnotation(Column.class).name();
            if (Objects.equals(currColumnName, "")) {
                currColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, type.getName());
            }

            if (currColumn.isAnnotationPresent(PrimaryKey.class)) {
                if (primaryKey != -1) {
                    throw new IllegalArgumentException("Not one primary Key");
                } else {
                    primaryKey = columns.size();
                }
            }

            columnNames.add(currColumnName);
            columns.add(currColumn);
        }

        fields = new Field[columns.size()];
        fields = columns.toArray(fields);

        namesForColumns = new String[columnNames.size()];
        namesForColumns = columnNames.toArray(namesForColumns);

        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("No H2 driver found");
        }

        connectionPool = JdbcConnectionPool.create("jdbc:h2:~/test", "test", "test");
    }

    void createTable() throws SQLException {
        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        for (int i = 0; i < fields.length; ++i) {
            if (i != 0) {
                statementBuilder.append(", ");
            }
            statementBuilder.append(fields[i].getAnnotation(Column.class).name()).append(" ")
                    .append(Converter.convert(fields[i].getType()));
            if (i == primaryKey) {
                statementBuilder.append(" PRIMARY KEY ");
            }
        }
        statementBuilder.append(")");
        Connection connection = connectionPool.getConnection();
        connection.createStatement().execute(statementBuilder.toString());
    }

    void dropTable() throws SQLException {
        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append("DROP TABLE IF EXISTS ").append(tableName);
        Connection connection = connectionPool.getConnection();
        connection.createStatement().execute(statementBuilder.toString());
    }

    public void insert(T record) throws SQLException, IllegalAccessException {
        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append("INSERT INTO ").append(tableName);
        statementBuilder.append(" VALUES (");
        for (int i = 0; i < fields.length; ++i) {
            if (i > 0) {
                statementBuilder.append(" ,");
            }
            statementBuilder.append("?");
        }
        statementBuilder.append(")");
        Connection connection = connectionPool.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(statementBuilder.toString());
        for (int i = 0; i < fields.length; ++i) {
            preparedStatement.setObject(i + 1, fields[i].get(record));
        }
        preparedStatement.execute();
    }


    public void delete(T record) throws IllegalArgumentException,
            IllegalAccessException, SQLException {
        if (primaryKey == -1) {
            throw new IllegalArgumentException("NO @PrimaryKey");
        }
        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append("DELETE FROM ").append(tableName)
                .append(" WHERE ").append(fields[primaryKey].getName())
                .append(" = ?");



        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement
                    = connection.prepareStatement(statementBuilder.toString());
            statement.setObject(1, fields[primaryKey].get(record));
            statement.execute();
        }
    }

    public void update(T record) throws IllegalArgumentException,
            SQLException, IllegalAccessException {
        if (primaryKey == -1) {
            throw new IllegalArgumentException("NO @PrimaryKey");
        }

        StringBuilder statementBuilder = new StringBuilder();
        statementBuilder.append("UPDATE ").append(tableName).append(" SET ");
        for (int i = 0; i < fields.length; ++i) {
            if (i != 0) {
                statementBuilder.append(", ");
            }
            statementBuilder.append(fields[i].getAnnotation(Column.class).name()).append(" = ?");
        }
        statementBuilder.append(" WHERE ").append(fields[primaryKey].getAnnotation(Column.class).name())
                .append(" = ?");


        try (Connection connection = connectionPool.getConnection()) {
            PreparedStatement statement
                    = connection.prepareStatement(statementBuilder.toString());
            for (int i = 0; i < fields.length; ++i) {
                statement.setObject(i + 1, fields[i].get(record));
            }
            statement.setObject(fields.length + 1, fields[primaryKey].get(record));
            statement.execute();
        }
    }

    public List<T> queryForAll() throws SQLException {
        List<T> result = new ArrayList<>();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM ").append(tableName);

        try (Connection conn = connectionPool.getConnection()) {
            try (ResultSet rs = conn.createStatement()
                    .executeQuery(queryBuilder.toString())) {
                while (rs.next()) {
                    T record = bdClass.newInstance();
                    for (int i = 0; i < fields.length; ++i) {
                        if (fields[i].getClass()
                                .isAssignableFrom(Number.class)) {
                            Long val = rs.getLong(i + 1);
                            fields[i].set(record, val);
                        } else if (fields[i].getType() != String.class) {
                            fields[i].set(record, rs.getObject(i + 1));
                        } else {
                            Clob data = rs.getClob(i + 1);
                            fields[i].set(record,
                                    data.getSubString(1, (int) data.length()));
                        }
                    }
                    result.add(record);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("wrong class");
            }
        }
        return result;
    }

    public <K> T queryById(K key) throws IllegalArgumentException,
            SQLException {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM ").append(tableName)
                .append(" WHERE ").append(namesForColumns[primaryKey])
                .append(" = ?");
        try (Connection conn = connectionPool.getConnection()) {
            PreparedStatement statement
                    = conn.prepareStatement(queryBuilder.toString());
            statement.setString(1, key.toString());

            try (ResultSet rs = statement.executeQuery()) {
                rs.next();
                T record = bdClass.newInstance();
                for (int i = 0; i < fields.length; ++i) {
                    if (fields[i].getClass()
                            .isAssignableFrom(Number.class)) {
                        Long val = rs.getLong(i + 1);
                        fields[i].set(record, val);
                    } else if (fields[i].getType() != String.class) {
                        fields[i].set(record, rs.getObject(i + 1));
                    } else {
                        Clob data = rs.getClob(i + 1);
                        fields[i].set(record,
                                data.getSubString(1, (int) data.length()));
                    }
                }
                return record;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalArgumentException("wrong class");
            }
        }

    }


}


