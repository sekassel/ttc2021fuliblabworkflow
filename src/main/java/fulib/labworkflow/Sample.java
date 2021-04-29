package fulib.labworkflow;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Sample
{
   public static final String PROPERTY_SAMPLE_ID = "sampleID";
   public static final String PROPERTY_STATE = "state";
   public static final String PROPERTY_JOB_REQUEST = "jobRequest";
   private String sampleID;
   private String state;
   private JobRequest jobRequest;
   protected PropertyChangeSupport listeners;

   public String getSampleID()
   {
      return this.sampleID;
   }

   public Sample setSampleID(String value)
   {
      if (Objects.equals(value, this.sampleID))
      {
         return this;
      }

      final String oldValue = this.sampleID;
      this.sampleID = value;
      this.firePropertyChange(PROPERTY_SAMPLE_ID, oldValue, value);
      return this;
   }

   public String getState()
   {
      return this.state;
   }

   public Sample setState(String value)
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

   public JobRequest getJobRequest()
   {
      return this.jobRequest;
   }

   public Sample setJobRequest(JobRequest value)
   {
      if (this.jobRequest == value)
      {
         return this;
      }

      final JobRequest oldValue = this.jobRequest;
      if (this.jobRequest != null)
      {
         this.jobRequest = null;
         oldValue.withoutSamples(this);
      }
      this.jobRequest = value;
      if (value != null)
      {
         value.withSamples(this);
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
      result.append(' ').append(this.getSampleID());
      result.append(' ').append(this.getState());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setJobRequest(null);
   }
}
