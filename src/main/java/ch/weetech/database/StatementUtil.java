package ch.weetech.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class StatementUtil {

    public static void setStatement(Map<String, Object> hashmap, PreparedStatement stmt) throws SQLException {
        Object value = null;
        int count = 0;
        int i = 1;

        if (hashmap != null) {
            while (hashmap.containsKey("" + i)) {
                value = hashmap.get("" + i);
                stmt.setObject(++count, value);
                i++;
            }
        }
    }

}
