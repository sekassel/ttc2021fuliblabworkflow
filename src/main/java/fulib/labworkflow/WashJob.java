package fulib.labworkflow;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.Objects;

public class WashJob extends Job
{
   public static final String PROPERTY_CAVITIES = "cavities";
   public static final String PROPERTY_MICROPLATE = "microplate";
   private List<Integer> cavities;
   private Microplate microplate;

   public List<Integer> getCavities()
   {
      return this.cavities != null ? Collections.unmodifiableList(this.cavities) : Collections.emptyList();
   }

   public WashJob withCavities(Integer value)
   {
      if (this.cavities == null)
      {
         this.cavities = new ArrayList<>();
      }
      if (this.cavities.add(value))
      {
         this.firePropertyChange(PROPERTY_CAVITIES, null, value);
      }
      return this;
   }

   public WashJob withCavities(Integer... value)
   {
      for (final Integer item : value)
      {
         this.withCavities(item);
      }
      return this;
   }

   public WashJob withCavities(Collection<? extends Integer> value)
   {
      for (final Integer item : value)
      {
         this.withCavities(item);
      }
      return this;
   }

   public WashJob withoutCavities(Integer value)
   {
      if (this.cavities != null && this.cavities.removeAll(Collections.singleton(value)))
      {
         this.firePropertyChange(PROPERTY_CAVITIES, value, null);
      }
      return this;
   }

   public WashJob withoutCavities(Integer... value)
   {
      for (final Integer item : value)
      {
         this.withoutCavities(item);
      }
      return this;
   }

   public WashJob withoutCavities(Collection<? extends Integer> value)
   {
      for (final Integer item : value)
      {
         this.withoutCavities(item);
      }
      return this;
   }

   public Microplate getMicroplate()
   {
      return this.microplate;
   }

   public WashJob setMicroplate(Microplate value)
   {
      if (Objects.equals(value, this.microplate))
      {
         return this;
      }

      final Microplate oldValue = this.microplate;
      this.microplate = value;
      this.firePropertyChange(PROPERTY_MICROPLATE, oldValue, value);
      return this;
   }
}
