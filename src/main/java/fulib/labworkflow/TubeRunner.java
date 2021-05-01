package fulib.labworkflow;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class TubeRunner extends Labware
{
   public static final String PROPERTY_BARCODES = "barcodes";
   public static final String PROPERTY_SAMPLES = "samples";
   private List<String> barcodes;
   private List<Sample> samples;

   public List<String> getBarcodes()
   {
      return this.barcodes != null ? Collections.unmodifiableList(this.barcodes) : Collections.emptyList();
   }

   public TubeRunner withBarcodes(String value)
   {
      if (this.barcodes == null)
      {
         this.barcodes = new ArrayList<>();
      }
      if (this.barcodes.add(value))
      {
         this.firePropertyChange(PROPERTY_BARCODES, null, value);
      }
      return this;
   }

   public TubeRunner withBarcodes(String... value)
   {
      for (final String item : value)
      {
         this.withBarcodes(item);
      }
      return this;
   }

   public TubeRunner withBarcodes(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withBarcodes(item);
      }
      return this;
   }

   public TubeRunner withoutBarcodes(String value)
   {
      if (this.barcodes != null && this.barcodes.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_BARCODES, value, null);
      }
      return this;
   }

   public TubeRunner withoutBarcodes(String... value)
   {
      for (final String item : value)
      {
         this.withoutBarcodes(item);
      }
      return this;
   }

   public TubeRunner withoutBarcodes(Collection<? extends String> value)
   {
      for (final String item : value)
      {
         this.withoutBarcodes(item);
      }
      return this;
   }

   public List<Sample> getSamples()
   {
      return this.samples != null ? Collections.unmodifiableList(this.samples) : Collections.emptyList();
   }

   public TubeRunner withSamples(Sample value)
   {
      if (this.samples == null)
      {
         this.samples = new ArrayList<>();
      }
      if (!this.samples.contains(value))
      {
         this.samples.add(value);
         value.setTube(this);
         this.firePropertyChange(PROPERTY_SAMPLES, null, value);
      }
      return this;
   }

   public TubeRunner withSamples(Sample... value)
   {
      for (final Sample item : value)
      {
         this.withSamples(item);
      }
      return this;
   }

   public TubeRunner withSamples(Collection<? extends Sample> value)
   {
      for (final Sample item : value)
      {
         this.withSamples(item);
      }
      return this;
   }

   public TubeRunner withoutSamples(Sample value)
   {
      if (this.samples != null && this.samples.remove(value))
      {
         value.setTube(null);
         this.firePropertyChange(PROPERTY_SAMPLES, value, null);
      }
      return this;
   }

   public TubeRunner withoutSamples(Sample... value)
   {
      for (final Sample item : value)
      {
         this.withoutSamples(item);
      }
      return this;
   }

   public TubeRunner withoutSamples(Collection<? extends Sample> value)
   {
      for (final Sample item : value)
      {
         this.withoutSamples(item);
      }
      return this;
   }

   @Override
   public String toString() // no fulib
   {
      final StringBuilder result = new StringBuilder(super.toString());
      return result.toString();
   }

   @Override
   public void removeYou()
   {
      super.removeYou();
      this.withoutSamples(new ArrayList<>(this.getSamples()));
   }
}
