package fulib.labworkflow.tables;
import fulib.labworkflow.Job;
import fulib.labworkflow.JobCollection;
import fulib.labworkflow.Microplate;
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

public class JobTable
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

   public JobTable setTable(List<List<Object>> value)
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

   public JobTable setColumnName(String value)
   {
      this.columnName = value;
      return this;
   }

   public Map<String,Integer> getColumnMap()
   {
      return this.columnMap;
   }

   public JobTable setColumnMap(Map<String,Integer> value)
   {
      this.columnMap = value;
      return this;
   }

   public StringTable expandState(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();

      for (List<Object> row : this.table)
      {
         Job start = (Job) row.get(column);
         row.add(start.getState());
      }

      StringTable result = new StringTable();
      result.setColumnMap(this.columnMap);
      result.setColumnName(columnName);
      result.setTable(this.table);
      return result;
   }

   public StringTable expandProtocolStepName(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();

      for (List<Object> row : this.table)
      {
         Job start = (Job) row.get(column);
         row.add(start.getProtocolStepName());
      }

      StringTable result = new StringTable();
      result.setColumnMap(this.columnMap);
      result.setColumnName(columnName);
      result.setTable(this.table);
      return result;
   }

   public MicroplateTable expandMicroplate(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         Job start = (Job) row.get(column);
         row.add(start.getMicroplate());
      }

      MicroplateTable result = new MicroplateTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public JobTable hasMicroplate(MicroplateTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         Job start = (Job) row.get(column);
         Microplate other = (Microplate) row.get(otherColumn);
         return start.getMicroplate() != other;
      });

      return this;
   }

   public JobCollectionTable expandJobCollection(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         Job start = (Job) row.get(column);
         row.add(start.getJobCollection());
      }

      JobCollectionTable result = new JobCollectionTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public JobTable hasJobCollection(JobCollectionTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         Job start = (Job) row.get(column);
         JobCollection other = (JobCollection) row.get(otherColumn);
         return start.getJobCollection() != other;
      });

      return this;
   }

   public JobTable expandPrevious(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         Job start = (Job) row.get(column);
         row.add(start.getPrevious());
      }

      JobTable result = new JobTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public JobTable hasPrevious(JobTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         Job start = (Job) row.get(column);
         Job other = (Job) row.get(otherColumn);
         return start.getPrevious() != other;
      });

      return this;
   }

   public JobTable expandNext(String columnName)
   {
      int newColumnNumber = this.table.isEmpty() ? 0 : this.table.get(0).size();
      this.columnMap.put(columnName, newColumnNumber);

      int column = this.getColumn();
      for (List<Object> row : this.table)
      {
         Job start = (Job) row.get(column);
         row.add(start.getNext());
      }

      JobTable result = new JobTable();
      result.setColumnMap(this.columnMap);
      result.setTable(this.table);
      result.setColumnName(columnName);
      return result;
   }

   public JobTable hasNext(JobTable rowName)
   {
      int column = this.getColumn();
      int otherColumn = rowName.getColumn();

      this.table.removeIf(row -> {
         Job start = (Job) row.get(column);
         Job other = (Job) row.get(otherColumn);
         return start.getNext() != other;
      });

      return this;
   }

   public JobTable selectColumns(String... columnNames)
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

   public JobTable dropColumns(String... columnNames)
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

   public JobTable filter(Predicate<? super Job> predicate)
   {
      int column = this.getColumn();
      this.table.removeIf(row -> {
         Job start = (Job) row.get(column);
         return !predicate.test(start);
      });
      return this;
   }

   public JobTable filterRow(Predicate<? super Map<String, Object>> predicate)
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

   public Set<Job> toSet()
   {
      int column = this.getColumn();
      Set<Job> result = new LinkedHashSet<>(this.table.size());
      for (List<?> row : this.table)
      {
         Job value = (Job) row.get(column);
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

   public JobTable(Job... start)
   {
      this.setColumnName("Job");
      this.columnMap.put("Job", 0);
      for (Job current : start)
      {
         List<Object> row = new ArrayList<>();
         row.add(current);
         this.table.add(row);
      }
   }
}