package fulib.labworkflow;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class JobRequest
{
   public static final String PROPERTY_SAMPLES = "samples";
   public static final String PROPERTY_ASSAY = "assay";
   private List<Sample> samples;
   protected PropertyChangeSupport listeners;
   private Assay assay;

   public List<Sample> getSamples()
   {
      return this.samples != null ? Collections.unmodifiableList(this.samples) : Collections.emptyList();
   }

   public JobRequest withSamples(Sample value)
   {
      if (this.samples == null)
      {
         this.samples = new ArrayList<>();
      }
      if (!this.samples.contains(value))
      {
         this.samples.add(value);
         value.setJobRequest(this);
         this.firePropertyChange(PROPERTY_SAMPLES, null, value);
      }
      return this;
   }

   public JobRequest withSamples(Sample... value)
   {
      for (final Sample item : value)
      {
         this.withSamples(item);
      }
      return this;
   }

   public JobRequest withSamples(Collection<? extends Sample> value)
   {
      for (final Sample item : value)
      {
         this.withSamples(item);
      }
      return this;
   }

   public JobRequest withoutSamples(Sample value)
   {
      if (this.samples != null && this.samples.remove(value))
      {
         value.setJobRequest(null);
         this.firePropertyChange(PROPERTY_SAMPLES, value, null);
      }
      return this;
   }

   public JobRequest withoutSamples(Sample... value)
   {
      for (final Sample item : value)
      {
         this.withoutSamples(item);
      }
      return this;
   }

   public JobRequest withoutSamples(Collection<? extends Sample> value)
   {
      for (final Sample item : value)
      {
         this.withoutSamples(item);
      }
      return this;
   }

   public Assay getAssay()
   {
      return this.assay;
   }

   public JobRequest setAssay(Assay value)
   {
      if (this.assay == value)
      {
         return this;
      }

      final Assay oldValue = this.assay;
      if (this.assay != null)
      {
         this.assay = null;
         oldValue.setJobRequest(null);
      }
      this.assay = value;
      if (value != null)
      {
         value.setJobRequest(this);
      }
      this.firePropertyChange(PROPERTY_ASSAY, oldValue, value);
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
      this.withoutSamples(new ArrayList<>(this.getSamples()));
      this.setAssay(null);
   }
}
