package fulib.labworkflow;

import org.fulib.Fulib;
import org.fulib.FulibTools;
import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

import java.util.List;

public class GenModel implements ClassModelDecorator
{
   class JobRequest {
      @Link("jobRequest")
      List<Sample> samples;

      @Link("jobRequest")
      Assay assay;
   }

   class Assay {
      String name;

      @Link("assay")
      JobRequest jobRequest;

      @Link("assay")
      List<ProtocolStep> steps;

      @Link("assay")
      List<Reagent> reagents;
   }

   class Sample {
      String sampleID;
      String state;

      @Link("samples")
      JobRequest jobRequest;

      @Link("samples")
      TubeRunner tube;

      @Link("samples")
      Microplate plate;

      @Link("sample")
      List<TipLiquidTransfer> tips;
   }

   class ProtocolStep {
      String id;

      @Link("steps")
      Assay assay;

      @Link("previous")
      ProtocolStep next;

      @Link("next")
      ProtocolStep previous;
   }

   class Reagent {
      String name;
      String source;

      @Link("reagents")
      Assay assay;

      @Link("reagent")
      List<AddReagent> addReagentSteps;
   }

   class Wash extends ProtocolStep {

   }

   class Incubate extends ProtocolStep {
      double temperature;
      int duration;
   }

   class DistributeSample extends ProtocolStep {
      double volume;
   }

   class AddReagent extends ProtocolStep {
      double volume;

      @Link("addReagentSteps")
      Reagent reagent;
   }

   // lab model
   class JobCollection {
      @Link("jobCollection")
      List<Job> jobs;

      @Link("jobCollection")
      List<Labware> labware;
   }

   class Job {
      String state;
      String protocolStepName;

      @Link("jobs")
      JobCollection jobCollection;

      @Link("next")
      Job previous;

      @Link("previous")
      Job next;

      @Link("jobs")
      Microplate microplate;
   }

   class Labware {
      String name;

      @Link("labware")
      JobCollection jobCollection;
   }

   class Microplate extends Labware {
      @Link("plate")
      List<Sample> samples;

      @Link("microplate")
      List<Job> jobs;
   }

   class Trough extends Labware {

   }

   class TubeRunner extends Labware {
      List<String> barcodes;

      @Link("tube")
      List<Sample> samples;
   }

   class LiquidTransferJob extends Job {
      Labware source;
      Labware target;

      @Link("job")
      List<TipLiquidTransfer> tips;
   }

   class TipLiquidTransfer {
      int sourceCavityIndex;
      double volume;
      int targetCavityIndex;
      String status;

      @Link("tips")
      LiquidTransferJob job;

      @Link("tips")
      Sample sample;
   }

   class IncubateJob extends Job {
      double temperature;
      int duration;
   }

   class WashJob extends Job {
      List<Integer> cavities;
   }

   @Override
   public void decorate(ClassModelManager mm)
   {
      mm.haveNestedClasses(GenModel.class);

      Fulib.tablesGenerator().generate(mm.getClassModel());
   }
}
