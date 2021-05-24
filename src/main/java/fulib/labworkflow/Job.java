package fulib.labworkflow;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Job
{
   public static final String PROPERTY_STATE = "state";
   public static final String PROPERTY_PROTOCOL_STEP_NAME = "protocolStepName";
   public static final String PROPERTY_PREVIOUS = "previous";
   public static final String PROPERTY_NEXT = "next";
   public static final String PROPERTY_JOB_COLLECTION = "jobCollection";
   public static final String PROPERTY_MICROPLATE = "microplate";
   private String state; // no fulib
   private String protocolStepName;
   private Job previous;
   private Job next;
   protected PropertyChangeSupport listeners;
   private JobCollection jobCollection;
   private Microplate microplate;

   public String getState()
   {
      return this.state;
   }

   public Job setState(String value)
   {
      if (Objects.equals(value, this.state))
      {
         return this;
      }

      final String oldValue = this.state;
      this.state = value;
      this.firePropertyChange(PROPERTY_STATE, oldValue, value);
      return this;
   }

   public String getProtocolStepName()
   {
      return this.protocolStepName;
   }

   public Job setProtocolStepName(String value)
   {
      if (Objects.equals(value, this.protocolStepName))
      {
         return this;
      }

      final String oldValue = this.protocolStepName;
      this.protocolStepName = value;
      this.firePropertyChange(PROPERTY_PROTOCOL_STEP_NAME, oldValue, value);
      return this;
   }

   public Job getPrevious()
   {
      return this.previous;
   }

   public Job setPrevious(Job value)
   {
      if (this.previous == value)
      {
         return this;
      }

      final Job oldValue = this.previous;
      if (this.previous != null)
      {
         this.previous = null;
         oldValue.setNext(null);
      }
      this.previous = value;
      if (value != null)
      {
         value.setNext(this);
      }
      this.firePropertyChange(PROPERTY_PREVIOUS, oldValue, value);
      return this;
   }

   public Job getNext()
   {
      return this.next;
   }

   public Job setNext(Job value)
   {
      if (this.next == value)
      {
         return this;
      }

      final Job oldValue = this.next;
      if (this.next != null)
      {
         this.next = null;
         oldValue.setPrevious(null);
      }
      this.next = value;
      if (value != null)
      {
         value.setPrevious(this);
      }
      this.firePropertyChange(PROPERTY_NEXT, oldValue, value);
      return this;
   }

   public JobCollection getJobCollection()
   {
      return this.jobCollection;
   }

   public Job setJobCollection(JobCollection value)
   {
      if (this.jobCollection == value)
      {
         return this;
      }

      final JobCollection oldValue = this.jobCollection;
      if (this.jobCollection != null)
      {
         this.jobCollection = null;
         oldValue.withoutJobs(this);
      }
      this.jobCollection = value;
      if (value != null)
      {
         value.withJobs(this);
      }
      this.firePropertyChange(PROPERTY_JOB_COLLECTION, oldValue, value);
      return this;
   }

   public Microplate getMicroplate()
   {
      return this.microplate;
   }

   public Job setMicroplate(Microplate value)
   {
      if (this.microplate == value)
      {
         return this;
      }

      final Microplate oldValue = this.microplate;
      if (this.microplate != null)
      {
         this.microplate = null;
         oldValue.withoutJobs(this);
      }
      this.microplate = value;
      if (value != null)
      {
         value.withJobs(this);
      }
      this.firePropertyChange(PROPERTY_MICROPLATE, oldValue, value);
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
      result.append(' ').append(this.getState());
      result.append(' ').append(this.getProtocolStepName());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setMicroplate(null);
      this.setJobCollection(null);
      this.setPrevious(null);
      this.setNext(null);
   }
}
