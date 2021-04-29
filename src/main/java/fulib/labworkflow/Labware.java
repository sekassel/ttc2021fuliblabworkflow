package fulib.labworkflow;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Labware
{
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_JOB_COLLECTION = "jobCollection";
   private String name;
   protected PropertyChangeSupport listeners;
   private JobCollection jobCollection;

   public String getName()
   {
      return this.name;
   }

   public Labware setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      return this;
   }

   public JobCollection getJobCollection()
   {
      return this.jobCollection;
   }

   public Labware setJobCollection(JobCollection value)
   {
      if (this.jobCollection == value)
      {
         return this;
      }

      final JobCollection oldValue = this.jobCollection;
      if (this.jobCollection != null)
      {
         this.jobCollection = null;
         oldValue.withoutLabware(this);
      }
      this.jobCollection = value;
      if (value != null)
      {
         value.withLabware(this);
      }
      this.firePropertyChange(PROPERTY_JOB_COLLECTION, oldValue, value);
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getName());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setJobCollection(null);
   }
}
