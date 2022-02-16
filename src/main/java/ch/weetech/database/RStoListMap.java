package ch.weetech.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RStoListMap {

    private static final Logger logger = LoggerFactory.getLogger(RStoListMap.class);

    public static List<Map<String, Object>> getMapList(ResultSet resultSet) throws SQLException, NullPointerException {

        if (resultSet == null)
            throw new NullPointerException("resultset is null");

        List<Map<String, Object>> hashResults = null;
        String[] columnNames = null;

        resultSet.last();

        int numberOfRows = resultSet.getRow();

        hashResults = new ArrayList<>(numberOfRows);

        // set up columnNames
        ResultSetMetaData resultSetMega = resultSet.getMetaData();
        int numberOfColumns = resultSetMega.getColumnCount();

        if (numberOfRows == 0 && numberOfColumns != 0) {

            if (resultSetMega.getColumnLabel(1).equals("GENERATED_KEY")) {
                hashResults = new ArrayList<>(1);
                hashResults.add(new LinkedHashMap<>());
                return hashResults;
            } else {
                return null;
            }
        }

        columnNames = new String[numberOfColumns];

        for (int i = 1; i <= numberOfColumns; i++) {
            columnNames[i - 1] = resultSetMega.getColumnLabel(i);
        }

        // setup resultset to first row
        resultSet.first();

        // fill HashMap with put(columnName, data)
        if (numberOfRows != 0)  {

            do {

                SQLWarning warning = resultSet.getWarnings();

                while (warning != null) {

                    // Process connection warning
                    // For information on these values, see e241 Handling a SQL Exception
                    String message = warning.getMessage();
                    String sqlState = warning.getSQLState();
                    int errorCode = warning.getErrorCode();

                    logger.warn("message: {} \nsqlState: {} \nerrorCode: {}", message, sqlState, errorCode);
                    warning = warning.getNextWarning();

                }

                LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();

                for (int i = 0; i < numberOfColumns; i++) {

                    Object myObject = null;
                    try {
                        myObject = resultSet.getObject(columnNames[i]);
                        linkedHashMap.put(columnNames[i], myObject);
                    } catch (NullPointerException e) {
                        linkedHashMap.put(columnNames[i], null);
                    }
                }
                hashResults.add(linkedHashMap);

            } while (resultSet.next());
        }

        return hashResults;

    }

}
