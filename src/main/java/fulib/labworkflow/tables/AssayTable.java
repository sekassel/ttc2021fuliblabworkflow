package fulib.labworkflow.tables;
import fulib.labworkflow.Assay;
import fulib.labworkflow.JobRequest;
import fulib.labworkflow.ProtocolStep;
import fulib.labworkflow.Reagent;
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

public class AssayTable
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

   public AssayTable setTable(List<List<Object>> value)
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

   public AssayTable setColumnName(String value)
   {
      this.columnName = value;
      return this;
   }

   public Map<String,Integer> getColumnMap()
   {
      return this.columnMap;
   }

   public AssayTable setColumnMap(Map<String,Integer> value)
   {
      this.columnMap = value;
      return this;
   }

   public StringTable expandName(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();

      for (List<Object> row : this.table)
      {
         Assay start = (Assay) row.get(column);
         row.add(start.getName());
      }

      StringTable result = new StringTable();
      result.setColumnMap(this.columnMap);
      result.setColumnName(columnName);
      result.setTable(this.table);
      return result;
   }

   public ReagentTable expandReagents(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();
      for (List<Object> row : oldTable)
      {
         Assay start = (Assay) row.get(column);
         for (Reagent current : start.getReagents())
         {
            List<Object> newRow = new ArrayList<>(row);
            newRow.add(current);
            this.table.add(newRow);
         }
      }
      ReagentTable result = new ReagentTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public AssayTable hasReagents(ReagentTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         Assay start = (Assay) row.get(column);
         Reagent other = (Reagent) row.get(otherColumn);
         return !start.getReagents().contains(other);
      });

      return this;
   }

   public ProtocolStepTable expandSteps(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      List<List<Object>> oldTable = new ArrayList<>(this.table);
      this.table.clear();
      for (List<Object> row : oldTable)
      {
         Assay start = (Assay) row.get(column);
         for (ProtocolStep current : start.getSteps())
         {
            List<Object> newRow = new ArrayList<>(row);
            newRow.add(current);
            this.table.add(newRow);
         }
      }
      ProtocolStepTable result = new ProtocolStepTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public AssayTable hasSteps(ProtocolStepTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         Assay start = (Assay) row.get(column);
         ProtocolStep other = (ProtocolStep) row.get(otherColumn);
         return !start.getSteps().contains(other);
      });

      return this;
   }

   public JobRequestTable expandJobRequest(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         Assay start = (Assay) row.get(column);
         row.add(start.getJobRequest());
      }

      JobRequestTable result = new JobRequestTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public AssayTable hasJobRequest(JobRequestTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         Assay start = (Assay) row.get(column);
         JobRequest other = (JobRequest) row.get(otherColumn);
         return start.getJobRequest() != other;
      });

      return this;
   }

   public AssayTable selectColumns(String... columnNames)
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

   public AssayTable dropColumns(String... columnNames)
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

   public AssayTable filter(Predicate<? super Assay> predicate)
   {
      int column = this.getColumn();
      this.table.removeIf(row -> {
         Assay start = (Assay) row.get(column);
         return !predicate.test(start);
      });
      return this;
   }

   public AssayTable filterRow(Predicate<? super Map<String, Object>> predicate)
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

   public Set<Assay> toSet()
   {
      int column = this.getColumn();
      Set<Assay> result = new LinkedHashSet<>(this.table.size());
      for (List<?> row : this.table)
      {
         Assay value = (Assay) row.get(column);
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

   public AssayTable(Assay... start)
   {
      this.setColumnName("Assay");
      this.columnMap.put("Assay", 0);
      for (Assay current : start)
      {
         List<Object> row = new ArrayList<>();
         row.add(current);
         this.table.add(row);
      }
   }
}