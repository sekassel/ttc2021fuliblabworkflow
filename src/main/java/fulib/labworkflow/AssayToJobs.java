package fulib.labworkflow;

import fulib.labworkflow.tables.JobCollectionTable;
import fulib.labworkflow.tables.LabwareTable;

import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class AssayToJobs
{

   private JobCollection jobCollection;
   private JobRequest jobRequest;

   public JobCollection initial(JobRequest jobRequest)
   {
      this.jobRequest = jobRequest;
      jobCollection = new JobCollection();

      jobRequest.getSamples().forEach(this::assignToTube);
      jobRequest.getSamples().forEach(this::assignToPlate);
      jobRequest.getAssay().getReagents().forEach((this::assignToTrough));
      jobRequest.getAssay().getSteps().forEach(this::assignJob);

      return jobCollection;
   }

   Map<Class, Consumer<ProtocolStep>> stepAssignRules = null;

   private void assignJob(ProtocolStep protocolStep)
   {
      initStepAssignRules();
      Consumer<ProtocolStep> rule = stepAssignRules.get(protocolStep.getClass());
      if (rule != null) {
         rule.accept(protocolStep);
      }
      else {
         Logger.getGlobal().severe("no rule for " + protocolStep.getClass().getSimpleName());
      }
   }

   private void initStepAssignRules()
   {
      if (stepAssignRules == null) {
         stepAssignRules = new LinkedHashMap<>();
         stepAssignRules.put(DistributeSample.class, this::assignLiquidTransferJob4Samples);
         stepAssignRules.put(Incubate.class, this::assignIncubateJob);
         stepAssignRules.put(Wash.class, this::assignWashJob);
         stepAssignRules.put(AddReagent.class, this::assignAddReagentJob);
      }
   }

   private void assignAddReagentJob(ProtocolStep protocolStep)
   {
      liquidTransferJob = null;
      jobRequest.getSamples().forEach(sample -> assignTipAddReagent((AddReagent) protocolStep, sample));
   }

   private void assignTipAddReagent(AddReagent addReagent, Sample sample)
   {
      if (liquidTransferJob == null || liquidTransferJob.getTips().size() == 8) {
         liquidTransferJob = new LiquidTransferJob();
         liquidTransferJob.setProtocolStepName(addReagent.getId())
               .setState("Planned")
               .setJobCollection(jobCollection)
               .setPrevious(lastJob);
         lastJob = liquidTransferJob;
         Trough trough = (Trough) jobCollection.getLabware().stream()
               .filter(l -> l.getName().equals(addReagent.getReagent().getName()))
               .findFirst().get();
         liquidTransferJob.setSource(trough)
               .setTarget(sample.getPlate());
      }

      TipLiquidTransfer tip = new TipLiquidTransfer();
      tip.setSourceCavityIndex(sample.getTube().getSamples().indexOf(sample))
            .setVolume(addReagent.getVolume())
            .setTargetCavityIndex(sample.getPlate().getSamples().indexOf(sample))
            .setStatus("Planned")
            .setJob(liquidTransferJob)
            .setSample(sample);
   }

   private void assignWashJob(ProtocolStep protocolStep)
   {
      jobCollection.getLabware().stream()
            .filter(l -> l instanceof Microplate)
            .forEach(plate -> assignWashJob2Plate((Wash) protocolStep, (Microplate) plate));
   }

   private void assignWashJob2Plate(Wash wash, Microplate plate)
   {
      WashJob washJob = new WashJob();
      washJob.setProtocolStepName(wash.getId())
            .setState("Planned")
            .setJobCollection(jobCollection)
            .setPrevious(lastJob);
      lastJob = washJob;
      washJob.setMicroplate(plate);
      for (Sample sample : plate.getSamples()) {
         washJob.withCavities(plate.getSamples().indexOf(sample));
      }
   }

   private void assignIncubateJob(ProtocolStep protocolStep)
   {
      new JobCollectionTable(jobCollection)
            .expandLabware("labware")
            .filterMicroplate()
            .forEach(plate -> assignIncubateJob2Plate(protocolStep, plate));
   }

   private void assignIncubateJob2Plate(ProtocolStep protocolStep, Labware plate)
   {
      Incubate incubateStep = (Incubate) protocolStep;
      IncubateJob incubateJob = new IncubateJob();
      incubateJob.setProtocolStepName(protocolStep.getId())
            .setState("Planned")
            .setJobCollection(jobCollection)
            .setPrevious(lastJob);
      incubateJob.setTemperature(incubateStep.getTemperature())
            .setDuration(incubateStep.getDuration())
            .setMicroplate((Microplate) plate);
      lastJob = incubateJob;
   }

   private Job lastJob = null;

   private void assignLiquidTransferJob4Samples(ProtocolStep protocolStep)
   {
      jobRequest.getSamples().forEach(sample -> assignTipLiquidTransfer(protocolStep, sample));
   }

   LiquidTransferJob liquidTransferJob = null;

   private void assignTipLiquidTransfer(ProtocolStep protocolStep, Sample sample)
   {
      DistributeSample distributeSample = (DistributeSample) protocolStep;
      if (liquidTransferJob == null || liquidTransferJob.getTips().size() == 8) {
         liquidTransferJob = new LiquidTransferJob();
         liquidTransferJob.setProtocolStepName(protocolStep.getId())
               .setState("Planned")
               .setJobCollection(jobCollection)
               .setPrevious(lastJob);
         lastJob = liquidTransferJob;
         liquidTransferJob.setSource(sample.getTube())
               .setTarget(sample.getPlate());
      }

      TipLiquidTransfer tip = new TipLiquidTransfer();
      tip.setSourceCavityIndex(sample.getTube().getSamples().indexOf(sample))
            .setVolume(distributeSample.getVolume())
            .setTargetCavityIndex(sample.getPlate().getSamples().indexOf(sample))
            .setStatus("Planned")
            .setJob(liquidTransferJob)
            .setSample(sample);
   }

   private void assignToTrough(Reagent reagent)
   {
      Trough trough = new Trough();
      trough.setName(reagent.getName())
            .setJobCollection(jobCollection);
   }

   private Microplate plate;
   private int plateNumber = 1;

   private void assignToPlate(Sample sample)
   {
      if (plate == null || plate.getSamples().size() == 96) {
         plate = new Microplate();
         plate.setName(String.format("Plate%02d", plateNumber))
               .setJobCollection(jobCollection);
         plateNumber++;
      }
      plate.withSamples(sample);
   }

   TubeRunner tube;
   int tubeNumber = 1;

   private void assignToTube(Sample sample)
   {
      if (tube == null || tube.getBarcodes().size() == 16) {
         tube = new TubeRunner();
         tube.setName(String.format("Tube%02d", tubeNumber))
               .setJobCollection(jobCollection);
         tubeNumber++;
      }
      tube.withBarcodes(sample.getSampleID());
      tube.withSamples(sample);
   }
}
