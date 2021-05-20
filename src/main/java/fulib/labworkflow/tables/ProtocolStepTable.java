package fulib.labworkflow.tables;
import fulib.labworkflow.Assay;
import fulib.labworkflow.ProtocolStep;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProtocolStepTable
{
   public static final String PROPERTY_TABLE = "table";
   public static final String PROPERTY_COLUMN_NAME = "columnName";
   public static final String PROPERTY_COLUMN_MAP = "columnMap";
   private List<List<Object>> table = new ArrayList<>();
   private String columnName;
   private Map<String,Integer> columnMap = new LinkedHashMap<>();

   public List<List<Object>> getTable()
   {
      return this.table;
   }

   public ProtocolStepTable setTable(List<List<Object>> value)
   {
      this.table = value;
      return this;
   }

   public int getColumn()
   {
      return this.columnMap.get(this.columnName);
   }

   public String getColumnName()
   {
      return this.columnName;
   }

   public ProtocolStepTable setColumnName(String value)
   {
      this.columnName = value;
      return this;
   }

   public Map<String,Integer> getColumnMap()
   {
      return this.columnMap;
   }

   public ProtocolStepTable setColumnMap(Map<String,Integer> value)
   {
      this.columnMap = value;
      return this;
   }

   public StringTable expandId(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();

      for (List<Object> row : this.table)
      {
         ProtocolStep start = (ProtocolStep) row.get(column);
         row.add(start.getId());
      }

      StringTable result = new StringTable();
      result.setColumnMap(this.columnMap);
      result.setColumnName(columnName);
      result.setTable(this.table);
      return result;
   }

   public AssayTable expandAssay(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         ProtocolStep start = (ProtocolStep) row.get(column);
         row.add(start.getAssay());
      }

      AssayTable result = new AssayTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public ProtocolStepTable hasAssay(AssayTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         ProtocolStep start = (ProtocolStep) row.get(column);
         Assay other = (Assay) row.get(otherColumn);
         return start.getAssay() != other;
      });

      return this;
   }

   public ProtocolStepTable expandNext(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         ProtocolStep start = (ProtocolStep) row.get(column);
         row.add(start.getNext());
      }

      ProtocolStepTable result = new ProtocolStepTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public ProtocolStepTable hasNext(ProtocolStepTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         ProtocolStep start = (ProtocolStep) row.get(column);
         ProtocolStep other = (ProtocolStep) row.get(otherColumn);
         return start.getNext() != other;
      });

      return this;
   }

   public ProtocolStepTable expandPrevious(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         ProtocolStep start = (ProtocolStep) row.get(column);
         row.add(start.getPrevious());
      }

      ProtocolStepTable result = new ProtocolStepTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public ProtocolStepTable hasPrevious(ProtocolStepTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         ProtocolStep start = (ProtocolStep) row.get(column);
         ProtocolStep other = (ProtocolStep) row.get(otherColumn);
         return start.getPrevious() != other;
      });

      return this;
   }

   public ProtocolStepTable selectColumns(String... columnNames)
   {
      Map<String, Integer> oldColumnMap = new LinkedHashMap<>(this.columnMap);
      this.columnMap.clear();

      for (int i = 0; i < columnNames.length; i++)
      {
         String name = columnNames[i];
         if (oldColumnMap.get(name) == null)
         {
            throw new IllegalArgumentException("unknown column name: " + name);
         }
         this.columnMap.put(name, i);
      }

      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();

      Set<List<Object>> rowSet = new HashSet<>();
      for (List<Object> row : oldTable)
      {
         List<Object> newRow = new ArrayList<>();
         for (String name : columnNames)
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
         {
            this.table.add(newRow);
         }
      }

      return this;
   }

   public ProtocolStepTable dropColumns(String... columnNames)
   {
      Map<String, Integer> oldColumnMap = new LinkedHashMap<>(this.columnMap);
      this.columnMap.clear();

      Set<String> dropNames = new HashSet<>(Arrays.asList(columnNames));
      int i = 0;
      for (String name : oldColumnMap.keySet())
      {
         if (!dropNames.contains(name))
         {
            this.columnMap.put(name, i);
            i++;
         }
      }

      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();

      Set<List<Object>> rowSet = new HashSet<>();
      for (List<Object> row : oldTable)
      {
         List<Object> newRow = new ArrayList<>();
         for (String name : this.columnMap.keySet())
         {
            Object value = row.get(oldColumnMap.get(name));
            newRow.add(value);
         }
         if (rowSet.add(newRow))
         {
            this.table.add(newRow);
         }
      }

      return this;
   }

   public void addColumn(String columnName, Function<? super Map<String, Object>, ?> function)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      for (List<Object> row : this.table)
      {
         Map<String, Object> map = new LinkedHashMap<>();
         for (Map.Entry<String, Integer> entry : this.columnMap.entrySet())
         {
            map.put(entry.getKey(), row.get(entry.getValue()));
         }
         Object result = function.apply(map);
         row.add(result);
      }
      this.columnMap.put(columnName, newColumnNumber);
   }

   public ProtocolStepTable filter(Predicate<? super ProtocolStep> predicate)
   {
      int column = this.getColumn();
      this.table.removeIf(row -> {
         ProtocolStep start = (ProtocolStep) row.get(column);
         return !predicate.test(start);
      });
      return this;
   }

   public ProtocolStepTable filterRow(Predicate<? super Map<String, Object>> predicate)
   {
      this.table.removeIf(row -> {
         Map<String, Object> map = new LinkedHashMap<>();
         for (Map.Entry<String, Integer> entry : this.columnMap.entrySet())
         {
            map.put(entry.getKey(), row.get(entry.getValue()));
         }
         return !predicate.test(map);
      });
      return this;
   }

   public Set<ProtocolStep> toSet()
   {
      int column = this.getColumn();
      Set<ProtocolStep> result = new LinkedHashSet<>(this.table.size());
      for (List<?> row : this.table)
      {
         ProtocolStep value = (ProtocolStep) row.get(column);
         result.add(value);
      }
      return result;
   }

   public String toString()
   {
      StringBuilder buf = new StringBuilder();
      for (String key : this.columnMap.keySet())
      {
         buf.append("| ").append(key).append(" \t");
      }
      buf.append("|\n");

      for (String ignored : this.columnMap.keySet())
      {
         buf.append("| --- ");
      }
      buf.append("|\n");

      for (List<?> row : this.table)
      {
         for (Object cell : row)
         {
            buf.append("| ").append(cell).append(" \t");
         }
         buf.append("|\n");
      }
      return buf.toString();
   }

   public ProtocolStepTable(ProtocolStep... start)
   {
      this.setColumnName("ProtocolStep");
      this.columnMap.put("ProtocolStep", 0);
      for (ProtocolStep current : start)
      {
         List<Object> row = new ArrayList<>();
         row.add(current);
         this.table.add(row);
      }
   }
}