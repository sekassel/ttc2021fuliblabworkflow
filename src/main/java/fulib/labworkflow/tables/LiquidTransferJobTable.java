package fulib.labworkflow.tables;
import fulib.labworkflow.LiquidTransferJob;
import fulib.labworkflow.TipLiquidTransfer;
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

public class LiquidTransferJobTable extends JobTable
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

   public LiquidTransferJobTable setTable(List<List<Object>> value)
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

   public LiquidTransferJobTable setColumnName(String value)
   {
      this.columnName = value;
      return this;
   }

   public Map<String,Integer> getColumnMap()
   {
      return this.columnMap;
   }

   public LiquidTransferJobTable setColumnMap(Map<String,Integer> value)
   {
      this.columnMap = value;
      return this;
   }

   public LabwareTable expandSource(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();

      for (List<Object> row : this.table)
      {
         LiquidTransferJob start = (LiquidTransferJob) row.get(column);
         row.add(start.getSource());
      }

      LabwareTable result = new LabwareTable();
      result.setColumnMap(this.columnMap);
      result.setColumnName(columnName);
      result.setTable(this.table);
      return result;
   }

   public LabwareTable expandTarget(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();

      for (List<Object> row : this.table)
      {
         LiquidTransferJob start = (LiquidTransferJob) row.get(column);
         row.add(start.getTarget());
      }

      LabwareTable result = new LabwareTable();
      result.setColumnMap(this.columnMap);
      result.setColumnName(columnName);
      result.setTable(this.table);
      return result;
   }

   public TipLiquidTransferTable expandTips(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();
      for (List<Object> row : oldTable)
      {
         LiquidTransferJob start = (LiquidTransferJob) row.get(column);
         for (TipLiquidTransfer current : start.getTips())
         {
            List<Object> newRow = new ArrayList<>(row);
            newRow.add(current);
            this.table.add(newRow);
         }
      }
      TipLiquidTransferTable result = new TipLiquidTransferTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public LiquidTransferJobTable hasTips(TipLiquidTransferTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         LiquidTransferJob start = (LiquidTransferJob) row.get(column);
         TipLiquidTransfer other = (TipLiquidTransfer) row.get(otherColumn);
         return !start.getTips().contains(other);
      });

      return this;
   }

   public LiquidTransferJobTable selectColumns(String... columnNames)
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

   public LiquidTransferJobTable dropColumns(String... columnNames)
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

   public LiquidTransferJobTable filterRow(Predicate<? super Map<String, Object>> predicate)
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

   public LiquidTransferJobTable(LiquidTransferJob... start)
   {
      this.setColumnName("LiquidTransferJob");
      this.columnMap.put("LiquidTransferJob", 0);
      for (LiquidTransferJob current : start)
      {
         List<Object> row = new ArrayList<>();
         row.add(current);
         this.table.add(row);
      }
   }
}