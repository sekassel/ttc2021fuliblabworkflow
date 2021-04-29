package fulib.labworkflow;
import java.util.Objects;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class Assay
{
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_REAGENTS = "reagents";
   public static final String PROPERTY_JOB_REQUEST = "jobRequest";
   private String name;
   protected PropertyChangeSupport listeners;
   private List<Reagent> reagents;
   private JobRequest jobRequest;

   public String getName()
   {
      return this.name;
   }

   public Assay setName(String value)
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

   public List<Reagent> getReagents()
   {
      return this.reagents != null ? Collections.unmodifiableList(this.reagents) : Collections.emptyList();
   }

   public Assay withReagents(Reagent value)
   {
      if (this.reagents == null)
      {
         this.reagents = new ArrayList<>();
      }
      if (!this.reagents.contains(value))
      {
         this.reagents.add(value);
         value.setAssay(this);
         this.firePropertyChange(PROPERTY_REAGENTS, null, value);
      }
      return this;
   }

   public Assay withReagents(Reagent... value)
   {
      for (final Reagent item : value)
      {
         this.withReagents(item);
      }
      return this;
   }

   public Assay withReagents(Collection<? extends Reagent> value)
   {
      for (final Reagent item : value)
      {
         this.withReagents(item);
      }
      return this;
   }

   public Assay withoutReagents(Reagent value)
   {
      if (this.reagents != null && this.reagents.remove(value))
      {
         value.setAssay(null);
         this.firePropertyChange(PROPERTY_REAGENTS, value, null);
      }
      return this;
   }

   public Assay withoutReagents(Reagent... value)
   {
      for (final Reagent item : value)
      {
         this.withoutReagents(item);
      }
      return this;
   }

   public Assay withoutReagents(Collection<? extends Reagent> value)
   {
      for (final Reagent item : value)
      {
         this.withoutReagents(item);
      }
      return this;
   }

   public JobRequest getJobRequest()
   {
      return this.jobRequest;
   }

   public Assay setJobRequest(JobRequest value)
   {
      if (this.jobRequest == value)
      {
         return this;
      }

      final JobRequest oldValue = this.jobRequest;
      if (this.jobRequest != null)
      {
         this.jobRequest = null;
         oldValue.setAssay(null);
      }
      this.jobRequest = value;
      if (value != null)
      {
         value.setAssay(this);
      }
      this.firePropertyChange(PROPERTY_JOB_REQUEST, oldValue, value);
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
      this.withoutReagents(new ArrayList<>(this.getReagents()));
      this.setJobRequest(null);
   }
}
