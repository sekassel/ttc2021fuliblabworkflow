package fulib.labworkflow;

public class DistributeSample extends ProtocolStep
{
   public static final String PROPERTY_VOLUME = "volume";
   private double volume;

   public double getVolume()
   {
      return this.volume;
   }

   public DistributeSample setVolume(double value)
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
}
