package ch.weetech.database;

import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class RStoListMapTest {

    @Mock
    ResultSet resultSet;

    @Mock
    ResultSetMetaData resultSetMetaData;

    @Mock
    SQLWarning sqlWarning;

      @Test
      public void testUnit() throws SQLException {
          when(resultSet.last()).thenReturn(true);
          when(resultSet.getRow()).thenReturn(2);
          when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
          when(resultSet.first()).thenReturn(true);
          when(resultSet.getWarnings()).thenReturn(null);

          when(resultSet.next()).thenReturn(true, false);
          when(resultSetMetaData.getColumnCount()).thenReturn(3);

          when(resultSetMetaData.getColumnLabel(1)).thenReturn("key1");
          when(resultSetMetaData.getColumnLabel(2)).thenReturn("key2");
          when(resultSetMetaData.getColumnLabel(3)).thenReturn("key3");

          when(resultSet.getObject("key1")).thenReturn("val1");
          when(resultSet.getObject("key2")).thenReturn("val2");
          when(resultSet.getObject("key3")).thenReturn("val3");

          List<Map<String, Object>> res = RStoListMap.getMapList(resultSet);
          assertNotNull(res);
          assertEquals(2, res.size());
          assertEquals("[{key1=val1, key2=val2, key3=val3}, {key1=val1, key2=val2, key3=val3}]", res.toString());
      }

}
