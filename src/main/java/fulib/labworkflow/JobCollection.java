package fulib.labworkflow;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class JobCollection
{
   public static final String PROPERTY_JOBS = "jobs";
   public static final String PROPERTY_LABWARE = "labware";
   private List<Job> jobs;
   protected PropertyChangeSupport listeners;
   private List<Labware> labware;

   public List<Job> getJobs()
   {
      return this.jobs != null ? Collections.unmodifiableList(this.jobs) : Collections.emptyList();
   }

   public JobCollection withJobs(Job value)
   {
      if (this.jobs == null)
      {
         this.jobs = new ArrayList<>();
      }
      if (!this.jobs.contains(value))
      {
         this.jobs.add(value);
         value.setJobCollection(this);
         this.firePropertyChange(PROPERTY_JOBS, null, value);
      }
      return this;
   }

   public JobCollection withJobs(Job... value)
   {
      for (final Job item : value)
      {
         this.withJobs(item);
      }
      return this;
   }

   public JobCollection withJobs(Collection<? extends Job> value)
   {
      for (final Job item : value)
      {
         this.withJobs(item);
      }
      return this;
   }

   public JobCollection withoutJobs(Job value)
   {
      if (this.jobs != null && this.jobs.remove(value))
      {
         value.setJobCollection(null);
         this.firePropertyChange(PROPERTY_JOBS, value, null);
      }
      return this;
   }

   public JobCollection withoutJobs(Job... value)
   {
      for (final Job item : value)
      {
         this.withoutJobs(item);
      }
      return this;
   }

   public JobCollection withoutJobs(Collection<? extends Job> value)
   {
      for (final Job item : value)
      {
         this.withoutJobs(item);
      }
      return this;
   }

   public List<Labware> getLabware()
   {
      return this.labware != null ? Collections.unmodifiableList(this.labware) : Collections.emptyList();
   }

   public JobCollection withLabware(Labware value)
   {
      if (this.labware == null)
      {
         this.labware = new ArrayList<>();
      }
      if (!this.labware.contains(value))
      {
         this.labware.add(value);
         value.setJobCollection(this);
         this.firePropertyChange(PROPERTY_LABWARE, null, value);
      }
      return this;
   }

   public JobCollection withLabware(Labware... value)
   {
      for (final Labware item : value)
      {
         this.withLabware(item);
      }
      return this;
   }

   public JobCollection withLabware(Collection<? extends Labware> value)
   {
      for (final Labware item : value)
      {
         this.withLabware(item);
      }
      return this;
   }

   public JobCollection withoutLabware(Labware value)
   {
      if (this.labware != null && this.labware.remove(value))
      {
         value.setJobCollection(null);
         this.firePropertyChange(PROPERTY_LABWARE, value, null);
      }
      return this;
   }

   public JobCollection withoutLabware(Labware... value)
   {
      for (final Labware item : value)
      {
         this.withoutLabware(item);
      }
      return this;
   }

   public JobCollection withoutLabware(Collection<? extends Labware> value)
   {
      for (final Labware item : value)
      {
         this.withoutLabware(item);
      }
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

   public void removeYou()
   {
      this.withoutLabware(new ArrayList<>(this.getLabware()));
      this.withoutJobs(new ArrayList<>(this.getJobs()));
   }
}
