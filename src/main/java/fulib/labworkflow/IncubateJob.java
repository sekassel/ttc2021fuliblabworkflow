package fulib.labworkflow;
import java.util.Objects;

public class IncubateJob extends Job
{
   public static final String PROPERTY_TEMPERATURE = "temperature";
   public static final String PROPERTY_DURATION = "duration";
   public static final String PROPERTY_MICROPLATE = "microplate";
   private double temperature;
   private int duration;
   private Microplate microplate;

   public double getTemperature()
   {
      return this.temperature;
   }

   public IncubateJob setTemperature(double value)
   {
      if (value == this.temperature)
      {
         return this;
      }

      final double oldValue = this.temperature;
      this.temperature = value;
      this.firePropertyChange(PROPERTY_TEMPERATURE, oldValue, value);
      return this;
   }

   public int getDuration()
   {
      return this.duration;
   }

   public IncubateJob setDuration(int value)
   {
      if (value == this.duration)
      {
         return this;
      }

      final int oldValue = this.duration;
      this.duration = value;
      this.firePropertyChange(PROPERTY_DURATION, oldValue, value);
      return this;
   }

   public Microplate getMicroplate()
   {
      return this.microplate;
   }

   public IncubateJob setMicroplate(Microplate value)
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
