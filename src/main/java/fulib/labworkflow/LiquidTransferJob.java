package fulib.labworkflow;
import java.util.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class LiquidTransferJob extends Job
{
   public static final String PROPERTY_SOURCE = "source";
   public static final String PROPERTY_TARGET = "target";
   public static final String PROPERTY_TIPS = "tips";
   private Labware source;
   private Labware target;
   private List<TipLiquidTransfer> tips;

   public Labware getSource()
   {
      return this.source;
   }

   public LiquidTransferJob setSource(Labware value)
   {
      if (Objects.equals(value, this.source))
      {
         return this;
      }

      final Labware oldValue = this.source;
      this.source = value;
      this.firePropertyChange(PROPERTY_SOURCE, oldValue, value);
      return this;
   }

   public Labware getTarget()
   {
      return this.target;
   }

   public LiquidTransferJob setTarget(Labware value)
   {
      if (Objects.equals(value, this.target))
      {
         return this;
      }

      final Labware oldValue = this.target;
      this.target = value;
      this.firePropertyChange(PROPERTY_TARGET, oldValue, value);
      return this;
   }

   public List<TipLiquidTransfer> getTips()
   {
      return this.tips != null ? Collections.unmodifiableList(this.tips) : Collections.emptyList();
   }

   public LiquidTransferJob withTips(TipLiquidTransfer value)
   {
      if (this.tips == null)
      {
         this.tips = new ArrayList<>();
      }
      if (!this.tips.contains(value))
      {
         this.tips.add(value);
         value.setJob(this);
         this.firePropertyChange(PROPERTY_TIPS, null, value);
      }
      return this;
   }

   public LiquidTransferJob withTips(TipLiquidTransfer... value)
   {
      for (final TipLiquidTransfer item : value)
      {
         this.withTips(item);
      }
      return this;
   }

   public LiquidTransferJob withTips(Collection<? extends TipLiquidTransfer> value)
   {
      for (final TipLiquidTransfer item : value)
      {
         this.withTips(item);
      }
      return this;
   }

   public LiquidTransferJob withoutTips(TipLiquidTransfer value)
   {
      if (this.tips != null && this.tips.remove(value))
      {
         value.setJob(null);
         this.firePropertyChange(PROPERTY_TIPS, value, null);
      }
      return this;
   }

   public LiquidTransferJob withoutTips(TipLiquidTransfer... value)
   {
      for (final TipLiquidTransfer item : value)
      {
         this.withoutTips(item);
      }
      return this;
   }

   public LiquidTransferJob withoutTips(Collection<? extends TipLiquidTransfer> value)
   {
      for (final TipLiquidTransfer item : value)
      {
         this.withoutTips(item);
      }
      return this;
   }

   @Override
   public void removeYou()
   {
      super.removeYou();
      this.withoutTips(new ArrayList<>(this.getTips()));
   }
}
