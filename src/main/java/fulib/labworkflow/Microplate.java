package fulib.labworkflow;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;

public class Microplate extends Labware
{
   public static final String PROPERTY_SAMPLES = "samples";
   public static final String PROPERTY_JOBS = "jobs";
   private List<Sample> samples;
   private List<Job> jobs;

   public List<Sample> getSamples()
   {
      return this.samples != null ? Collections.unmodifiableList(this.samples) : Collections.emptyList();
   }

   public Microplate withSamples(Sample value)
   {
      if (this.samples == null)
      {
         this.samples = new ArrayList<>();
      }
      if (!this.samples.contains(value))
      {
         this.samples.add(value);
         value.setPlate(this);
         this.firePropertyChange(PROPERTY_SAMPLES, null, value);
      }
      return this;
   }

   public Microplate withSamples(Sample... value)
   {
      for (final Sample item : value)
      {
         this.withSamples(item);
      }
      return this;
   }

   public Microplate withSamples(Collection<? extends Sample> value)
   {
      for (final Sample item : value)
      {
         this.withSamples(item);
      }
      return this;
   }

   public Microplate withoutSamples(Sample value)
   {
      if (this.samples != null && this.samples.remove(value))
      {
         value.setPlate(null);
         this.firePropertyChange(PROPERTY_SAMPLES, value, null);
      }
      return this;
   }

   public Microplate withoutSamples(Sample... value)
   {
      for (final Sample item : value)
      {
         this.withoutSamples(item);
      }
      return this;
   }

   public Microplate withoutSamples(Collection<? extends Sample> value)
   {
      for (final Sample item : value)
      {
         this.withoutSamples(item);
      }
      return this;
   }

   public List<Job> getJobs()
   {
      return this.jobs != null ? Collections.unmodifiableList(this.jobs) : Collections.emptyList();
   }

   public Microplate withJobs(Job value)
   {
      if (this.jobs == null)
      {
         this.jobs = new ArrayList<>();
      }
      if (!this.jobs.contains(value))
      {
         this.jobs.add(value);
         value.setMicroplate(this);
         this.firePropertyChange(PROPERTY_JOBS, null, value);
      }
      return this;
   }

   public Microplate withJobs(Job... value)
   {
      for (final Job item : value)
      {
         this.withJobs(item);
      }
      return this;
   }

   public Microplate withJobs(Collection<? extends Job> value)
   {
      for (final Job item : value)
      {
         this.withJobs(item);
      }
      return this;
   }

   public Microplate withoutJobs(Job value)
   {
      if (this.jobs != null && this.jobs.remove(value))
      {
         value.setMicroplate(null);
         this.firePropertyChange(PROPERTY_JOBS, value, null);
      }
      return this;
   }

   public Microplate withoutJobs(Job... value)
   {
      for (final Job item : value)
      {
         this.withoutJobs(item);
      }
      return this;
   }

   public Microplate withoutJobs(Collection<? extends Job> value)
   {
      for (final Job item : value)
      {
         this.withoutJobs(item);
      }
      return this;
   }

   @Override
   public void removeYou()
   {
      super.removeYou();
      this.withoutSamples(new ArrayList<>(this.getSamples()));
      this.withoutJobs(new ArrayList<>(this.getJobs()));
   }
}
