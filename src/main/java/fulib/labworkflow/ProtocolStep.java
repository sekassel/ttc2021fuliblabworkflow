package fulib.labworkflow;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class ProtocolStep
{
   public static final String PROPERTY_ID = "id";
   public static final String PROPERTY_NEXT = "next";
   public static final String PROPERTY_PREVIOUS = "previous";
   public static final String PROPERTY_ASSAY = "assay";
   private String id;
   private ProtocolStep next;
   private ProtocolStep previous;
   protected PropertyChangeSupport listeners;
   private Assay assay;

   public String getId()
   {
      return this.id;
   }

   public ProtocolStep setId(String value)
   {
      if (Objects.equals(value, this.id))
      {
         return this;
      }

      final String oldValue = this.id;
      this.id = value;
      this.firePropertyChange(PROPERTY_ID, oldValue, value);
      return this;
   }

   public ProtocolStep getNext()
   {
      return this.next;
   }

   public ProtocolStep setNext(ProtocolStep value)
   {
      if (this.next == value)
      {
         return this;
      }

      final ProtocolStep oldValue = this.next;
      if (this.next != null)
      {
         this.next = null;
         oldValue.setPrevious(null);
      }
      this.next = value;
      if (value != null)
      {
         value.setPrevious(this);
      }
      this.firePropertyChange(PROPERTY_NEXT, oldValue, value);
      return this;
   }

   public ProtocolStep getPrevious()
   {
      return this.previous;
   }

   public ProtocolStep setPrevious(ProtocolStep value)
   {
      if (this.previous == value)
      {
         return this;
      }

      final ProtocolStep oldValue = this.previous;
      if (this.previous != null)
      {
         this.previous = null;
         oldValue.setNext(null);
      }
      this.previous = value;
      if (value != null)
      {
         value.setNext(this);
      }
      this.firePropertyChange(PROPERTY_PREVIOUS, oldValue, value);
      return this;
   }

   public Assay getAssay()
   {
      return this.assay;
   }

   public ProtocolStep setAssay(Assay value)
   {
      if (this.assay == value)
      {
         return this;
      }

      final Assay oldValue = this.assay;
      if (this.assay != null)
      {
         this.assay = null;
         oldValue.withoutSteps(this);
      }
      this.assay = value;
      if (value != null)
      {
         value.withSteps(this);
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
      result.append(' ').append(this.getId());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setAssay(null);
      this.setNext(null);
      this.setPrevious(null);
   }
}
