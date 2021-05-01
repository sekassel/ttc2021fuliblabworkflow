package fulib.labworkflow;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class Microplate extends Labware
{
   public static final String PROPERTY_SAMPLES = "samples";
   private List<Sample> samples;

   public List<Sample> getSamples()
   {
      return this.samples != null ? Collections.unmodifiableList(this.samples) : Collections.emptyList();
   }

   public Microplate withSamples(Sample value)
   {
      if (this.samples == null)
      {
         this.samples = new ArrayList<>();
      }
      if (!this.samples.contains(value))
      {
         this.samples.add(value);
         value.setPlate(this);
         this.firePropertyChange(PROPERTY_SAMPLES, null, value);
      }
      return this;
   }

   public Microplate withSamples(Sample... value)
   {
      for (final Sample item : value)
      {
         this.withSamples(item);
      }
      return this;
   }

   public Microplate withSamples(Collection<? extends Sample> value)
   {
      for (final Sample item : value)
      {
         this.withSamples(item);
      }
      return this;
   }

   public Microplate withoutSamples(Sample value)
   {
      if (this.samples != null && this.samples.remove(value))
      {
         value.setPlate(null);
         this.firePropertyChange(PROPERTY_SAMPLES, value, null);
      }
      return this;
   }

   public Microplate withoutSamples(Sample... value)
   {
      for (final Sample item : value)
      {
         this.withoutSamples(item);
      }
      return this;
   }

   public Microplate withoutSamples(Collection<? extends Sample> value)
   {
      for (final Sample item : value)
      {
         this.withoutSamples(item);
      }
      return this;
   }

   @Override
   public void removeYou()
   {
      super.removeYou();
      this.withoutSamples(new ArrayList<>(this.getSamples()));
   }
}
