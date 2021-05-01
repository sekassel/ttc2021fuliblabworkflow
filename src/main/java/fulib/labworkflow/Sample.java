package fulib.labworkflow;
import java.util.Objects;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class Sample
{
   public static final String PROPERTY_SAMPLE_ID = "sampleID";
   public static final String PROPERTY_STATE = "state";
   public static final String PROPERTY_JOB_REQUEST = "jobRequest";
   public static final String PROPERTY_PLATE = "plate";
   public static final String PROPERTY_TUBE = "tube";
   public static final String PROPERTY_TIPS = "tips";
   private String sampleID;
   private String state;
   private JobRequest jobRequest;
   protected PropertyChangeSupport listeners;
   private Microplate plate;
   private TubeRunner tube;
   private List<TipLiquidTransfer> tips;

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

   public Microplate getPlate()
   {
      return this.plate;
   }

   public Sample setPlate(Microplate value)
   {
      if (this.plate == value)
      {
         return this;
      }

      final Microplate oldValue = this.plate;
      if (this.plate != null)
      {
         this.plate = null;
         oldValue.withoutSamples(this);
      }
      this.plate = value;
      if (value != null)
      {
         value.withSamples(this);
      }
      this.firePropertyChange(PROPERTY_PLATE, oldValue, value);
      return this;
   }

   public TubeRunner getTube()
   {
      return this.tube;
   }

   public Sample setTube(TubeRunner value)
   {
      if (this.tube == value)
      {
         return this;
      }

      final TubeRunner oldValue = this.tube;
      if (this.tube != null)
      {
         this.tube = null;
         oldValue.withoutSamples(this);
      }
      this.tube = value;
      if (value != null)
      {
         value.withSamples(this);
      }
      this.firePropertyChange(PROPERTY_TUBE, oldValue, value);
      return this;
   }

   public List<TipLiquidTransfer> getTips()
   {
      return this.tips != null ? Collections.unmodifiableList(this.tips) : Collections.emptyList();
   }

   public Sample withTips(TipLiquidTransfer value)
   {
      if (this.tips == null)
      {
         this.tips = new ArrayList<>();
      }
      if (!this.tips.contains(value))
      {
         this.tips.add(value);
         value.setSample(this);
         this.firePropertyChange(PROPERTY_TIPS, null, value);
      }
      return this;
   }

   public Sample withTips(TipLiquidTransfer... value)
   {
      for (final TipLiquidTransfer item : value)
      {
         this.withTips(item);
      }
      return this;
   }

   public Sample withTips(Collection<? extends TipLiquidTransfer> value)
   {
      for (final TipLiquidTransfer item : value)
      {
         this.withTips(item);
      }
      return this;
   }

   public Sample withoutTips(TipLiquidTransfer value)
   {
      if (this.tips != null && this.tips.remove(value))
      {
         value.setSample(null);
         this.firePropertyChange(PROPERTY_TIPS, value, null);
      }
      return this;
   }

   public Sample withoutTips(TipLiquidTransfer... value)
   {
      for (final TipLiquidTransfer item : value)
      {
         this.withoutTips(item);
      }
      return this;
   }

   public Sample withoutTips(Collection<? extends TipLiquidTransfer> value)
   {
      for (final TipLiquidTransfer item : value)
      {
         this.withoutTips(item);
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
      this.withoutTips(new ArrayList<>(this.getTips()));
      this.setTube(null);
      this.setPlate(null);
      this.setJobRequest(null);
   }
}
