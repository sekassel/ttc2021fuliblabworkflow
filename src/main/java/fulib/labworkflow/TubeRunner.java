package fulib.labworkflow;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class TubeRunner extends Labware
{
   public static final String PROPERTY_BARCODES = "barcodes";
   private List<String> barcodes;

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

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder(super.toString());
      result.append(' ').append(this.getBarcodes());
      return result.toString();
   }
}
