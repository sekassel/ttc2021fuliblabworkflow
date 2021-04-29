package fulib.labworkflow;

public class AddReagent extends ProtocolStep
{
   public static final String PROPERTY_VOLUME = "volume";
   public static final String PROPERTY_REAGENT = "reagent";
   private double volume;
   private Reagent reagent;

   public double getVolume()
   {
      return this.volume;
   }

   public AddReagent setVolume(double value)
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

   public Reagent getReagent()
   {
      return this.reagent;
   }

   public AddReagent setReagent(Reagent value)
   {
      if (this.reagent == value)
      {
         return this;
      }

      final Reagent oldValue = this.reagent;
      if (this.reagent != null)
      {
         this.reagent = null;
         oldValue.withoutAddReagentSteps(this);
      }
      this.reagent = value;
      if (value != null)
      {
         value.withAddReagentSteps(this);
      }
      this.firePropertyChange(PROPERTY_REAGENT, oldValue, value);
      return this;
   }

   @Override
   public void removeYou()
   {
      super.removeYou();
      this.setReagent(null);
   }
}
