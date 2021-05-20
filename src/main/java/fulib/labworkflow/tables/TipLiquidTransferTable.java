package fulib.labworkflow.tables;
import fulib.labworkflow.LiquidTransferJob;
import fulib.labworkflow.Sample;
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

public class TipLiquidTransferTable
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

   public TipLiquidTransferTable setTable(List<List<Object>> value)
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

   public TipLiquidTransferTable setColumnName(String value)
   {
      this.columnName = value;
      return this;
   }

   public Map<String,Integer> getColumnMap()
   {
      return this.columnMap;
   }

   public TipLiquidTransferTable setColumnMap(Map<String,Integer> value)
   {
      this.columnMap = value;
      return this;
   }

   public intTable expandSourceCavityIndex(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();

      for (List<Object> row : this.table)
      {
         TipLiquidTransfer start = (TipLiquidTransfer) row.get(column);
         row.add(start.getSourceCavityIndex());
      }

      intTable result = new intTable();
      result.setColumnMap(this.columnMap);
      result.setColumnName(columnName);
      result.setTable(this.table);
      return result;
   }

   public doubleTable expandVolume(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();

      for (List<Object> row : this.table)
      {
         TipLiquidTransfer start = (TipLiquidTransfer) row.get(column);
         row.add(start.getVolume());
      }

      doubleTable result = new doubleTable();
      result.setColumnMap(this.columnMap);
      result.setColumnName(columnName);
      result.setTable(this.table);
      return result;
   }

   public intTable expandTargetCavityIndex(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();

      for (List<Object> row : this.table)
      {
         TipLiquidTransfer start = (TipLiquidTransfer) row.get(column);
         row.add(start.getTargetCavityIndex());
      }

      intTable result = new intTable();
      result.setColumnMap(this.columnMap);
      result.setColumnName(columnName);
      result.setTable(this.table);
      return result;
   }

   public StringTable expandStatus(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();

      for (List<Object> row : this.table)
      {
         TipLiquidTransfer start = (TipLiquidTransfer) row.get(column);
         row.add(start.getStatus());
      }

      StringTable result = new StringTable();
      result.setColumnMap(this.columnMap);
      result.setColumnName(columnName);
      result.setTable(this.table);
      return result;
   }

   public LiquidTransferJobTable expandJob(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         TipLiquidTransfer start = (TipLiquidTransfer) row.get(column);
         row.add(start.getJob());
      }

      LiquidTransferJobTable result = new LiquidTransferJobTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public TipLiquidTransferTable hasJob(LiquidTransferJobTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         TipLiquidTransfer start = (TipLiquidTransfer) row.get(column);
         LiquidTransferJob other = (LiquidTransferJob) row.get(otherColumn);
         return start.getJob() != other;
      });

      return this;
   }

   public SampleTable expandSample(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         TipLiquidTransfer start = (TipLiquidTransfer) row.get(column);
         row.add(start.getSample());
      }

      SampleTable result = new SampleTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public TipLiquidTransferTable hasSample(SampleTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         TipLiquidTransfer start = (TipLiquidTransfer) row.get(column);
         Sample other = (Sample) row.get(otherColumn);
         return start.getSample() != other;
      });

      return this;
   }

   public TipLiquidTransferTable selectColumns(String... columnNames)
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

   public TipLiquidTransferTable dropColumns(String... columnNames)
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

   public TipLiquidTransferTable filter(Predicate<? super TipLiquidTransfer> predicate)
   {
      int column = this.getColumn();
      this.table.removeIf(row -> {
         TipLiquidTransfer start = (TipLiquidTransfer) row.get(column);
         return !predicate.test(start);
      });
      return this;
   }

   public TipLiquidTransferTable filterRow(Predicate<? super Map<String, Object>> predicate)
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

   public Set<TipLiquidTransfer> toSet()
   {
      int column = this.getColumn();
      Set<TipLiquidTransfer> result = new LinkedHashSet<>(this.table.size());
      for (List<?> row : this.table)
      {
         TipLiquidTransfer value = (TipLiquidTransfer) row.get(column);
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

   public TipLiquidTransferTable(TipLiquidTransfer... start)
   {
      this.setColumnName("TipLiquidTransfer");
      this.columnMap.put("TipLiquidTransfer", 0);
      for (TipLiquidTransfer current : start)
      {
         List<Object> row = new ArrayList<>();
         row.add(current);
         this.table.add(row);
      }
   }
}