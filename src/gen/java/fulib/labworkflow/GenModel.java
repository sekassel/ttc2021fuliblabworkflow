package fulib.labworkflow;

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
      List<Reagent> reagents;
   }

   class Sample {
      String sampleID;
      String state;

      @Link("samples")
      JobRequest jobRequest;
   }

   class ProtocolStep {
      String id;

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



   @Override
   public void decorate(ClassModelManager mm)
   {
      mm.haveNestedClasses(GenModel.class);
   }
}
