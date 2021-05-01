package fulib.labworkflow;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class TipLiquidTransfer
{
   public static final String PROPERTY_SOURCE_CAVITY_INDEX = "sourceCavityIndex";
   public static final String PROPERTY_VOLUME = "volume";
   public static final String PROPERTY_TARGET_CAVITY_INDEX = "targetCavityIndex";
   public static final String PROPERTY_STATUS = "status";
   public static final String PROPERTY_JOB = "job";
   public static final String PROPERTY_SAMPLE = "sample";
   private int sourceCavityIndex;
   private double volume;
   private int targetCavityIndex;
   private String status;
   private LiquidTransferJob job;
   protected PropertyChangeSupport listeners;
   private Sample sample;

   public int getSourceCavityIndex()
   {
      return this.sourceCavityIndex;
   }

   public TipLiquidTransfer setSourceCavityIndex(int value)
   {
      if (value == this.sourceCavityIndex)
      {
         return this;
      }

      final int oldValue = this.sourceCavityIndex;
      this.sourceCavityIndex = value;
      this.firePropertyChange(PROPERTY_SOURCE_CAVITY_INDEX, oldValue, value);
      return this;
   }

   public double getVolume()
   {
      return this.volume;
   }

   public TipLiquidTransfer setVolume(double value)
   {
      if (value == this.volume)
      {
         return this;
      }

      final double oldValue = this.volume;
      this.volume = value;
      this.firePropertyChange(PROPERTY_VOLUME, oldValue, value);
      return this;
   }

   public int getTargetCavityIndex()
   {
      return this.targetCavityIndex;
   }

   public TipLiquidTransfer setTargetCavityIndex(int value)
   {
      if (value == this.targetCavityIndex)
      {
         return this;
      }

      final int oldValue = this.targetCavityIndex;
      this.targetCavityIndex = value;
      this.firePropertyChange(PROPERTY_TARGET_CAVITY_INDEX, oldValue, value);
      return this;
   }

   public String getStatus()
   {
      return this.status;
   }

   public TipLiquidTransfer setStatus(String value)
   {
      if (Objects.equals(value, this.status))
      {
         return this;
      }

      final String oldValue = this.status;
      this.status = value;
      this.firePropertyChange(PROPERTY_STATUS, oldValue, value);
      return this;
   }

   public LiquidTransferJob getJob()
   {
      return this.job;
   }

   public TipLiquidTransfer setJob(LiquidTransferJob value)
   {
      if (this.job == value)
      {
         return this;
      }

      final LiquidTransferJob oldValue = this.job;
      if (this.job != null)
      {
         this.job = null;
         oldValue.withoutTips(this);
      }
      this.job = value;
      if (value != null)
      {
         value.withTips(this);
      }
      this.firePropertyChange(PROPERTY_JOB, oldValue, value);
      return this;
   }

   public Sample getSample()
   {
      return this.sample;
   }

   public TipLiquidTransfer setSample(Sample value)
   {
      if (this.sample == value)
      {
         return this;
      }

      final Sample oldValue = this.sample;
      if (this.sample != null)
      {
         this.sample = null;
         oldValue.withoutTips(this);
      }
      this.sample = value;
      if (value != null)
      {
         value.withTips(this);
      }
      this.firePropertyChange(PROPERTY_SAMPLE, oldValue, value);
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
      result.append(' ').append(this.getStatus());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setJob(null);
      this.setSample(null);
   }
}
