package fulib.labworkflow;

public class Incubate extends ProtocolStep
{
   public static final String PROPERTY_TEMPERATURE = "temperature";
   public static final String PROPERTY_DURATION = "duration";
   private double temperature;
   private int duration;

   public double getTemperature()
   {
      return this.temperature;
   }

   public Incubate setTemperature(double value)
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

   public Incubate setDuration(int value)
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
}
