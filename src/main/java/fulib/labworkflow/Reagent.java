package fulib.labworkflow;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class Reagent
{
   public static final String PROPERTY_NAME = "name";
   public static final String PROPERTY_SOURCE = "source";
   public static final String PROPERTY_ADD_REAGENT_STEPS = "addReagentSteps";
   public static final String PROPERTY_ASSAY = "assay";
   private String name;
   private String source;
   private List<AddReagent> addReagentSteps;
   private Assay assay;
   protected PropertyChangeSupport listeners;

   public String getName()
   {
      return this.name;
   }

   public Reagent setName(String value)
   {
      if (Objects.equals(value, this.name))
      {
         return this;
      }

      final String oldValue = this.name;
      this.name = value;
      this.firePropertyChange(PROPERTY_NAME, oldValue, value);
      return this;
   }

   public String getSource()
   {
      return this.source;
   }

   public Reagent setSource(String value)
   {
      if (Objects.equals(value, this.source))
      {
         return this;
      }

      final String oldValue = this.source;
      this.source = value;
      this.firePropertyChange(PROPERTY_SOURCE, oldValue, value);
      return this;
   }

   public List<AddReagent> getAddReagentSteps()
   {
      return this.addReagentSteps != null ? Collections.unmodifiableList(this.addReagentSteps) : Collections.emptyList();
   }

   public Reagent withAddReagentSteps(AddReagent value)
   {
      if (this.addReagentSteps == null)
      {
         this.addReagentSteps = new ArrayList<>();
      }
      if (!this.addReagentSteps.contains(value))
      {
         this.addReagentSteps.add(value);
         value.setReagent(this);
         this.firePropertyChange(PROPERTY_ADD_REAGENT_STEPS, null, value);
      }
      return this;
   }

   public Reagent withAddReagentSteps(AddReagent... value)
   {
      for (final AddReagent item : value)
      {
         this.withAddReagentSteps(item);
      }
      return this;
   }

   public Reagent withAddReagentSteps(Collection<? extends AddReagent> value)
   {
      for (final AddReagent item : value)
      {
         this.withAddReagentSteps(item);
      }
      return this;
   }

   public Reagent withoutAddReagentSteps(AddReagent value)
   {
      if (this.addReagentSteps != null && this.addReagentSteps.remove(value))
      {
         value.setReagent(null);
         this.firePropertyChange(PROPERTY_ADD_REAGENT_STEPS, value, null);
      }
      return this;
   }

   public Reagent withoutAddReagentSteps(AddReagent... value)
   {
      for (final AddReagent item : value)
      {
         this.withoutAddReagentSteps(item);
      }
      return this;
   }

   public Reagent withoutAddReagentSteps(Collection<? extends AddReagent> value)
   {
      for (final AddReagent item : value)
      {
         this.withoutAddReagentSteps(item);
      }
      return this;
   }

   public Assay getAssay()
   {
      return this.assay;
   }

   public Reagent setAssay(Assay value)
   {
      if (this.assay == value)
      {
         return this;
      }

      final Assay oldValue = this.assay;
      if (this.assay != null)
      {
         this.assay = null;
         oldValue.withoutReagents(this);
      }
      this.assay = value;
      if (value != null)
      {
         value.withReagents(this);
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

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getName());
      result.append(' ').append(this.getSource());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.withoutAddReagentSteps(new ArrayList<>(this.getAddReagentSteps()));
      this.setAssay(null);
   }
}
