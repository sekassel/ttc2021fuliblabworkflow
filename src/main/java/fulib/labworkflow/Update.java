package fulib.labworkflow;

import org.fulib.tables.ObjectTable;

import java.util.ArrayList;
import java.util.Optional;

public class Update
{
   private JobCollection jobCollection;

   public void update(JobCollection jobCollection, String updates)
   {
      this.jobCollection = jobCollection;
      String[] split = updates.split("\n");
      for (String line : split) {
         updateOne(line.trim());
      }

      jobCollection.getJobs().stream()
            .filter(j -> j.getState().equals("Planned"))
            .forEach(job -> removeObsoleteJob(job));
   }

   private void removeObsoleteJob(Job job)
   {
      if (isObsolete(job)) {
         job.setJobCollection(null);
         if (job.getPrevious() != null) {
            job.getPrevious().setNext(job.getNext());
         }
         else {
            job.setNext(null);
         }
      }
   }

   private boolean isObsolete(Job job)
   {
      if (job instanceof LiquidTransferJob) {
         LiquidTransferJob transferJob = (LiquidTransferJob) job;

         for (TipLiquidTransfer tip : transferJob.getTips()) {
            if ( ! tip.getSample().getState().equals("Error")) {
               return false;
            }
         }
         return true;
      }
      else {
         for (Sample sample : job.getMicroplate().getSamples()) {
            if ( ! sample.getState().equals("Error")) {
               return false;
            }
         }
         return true;
      }
   }

   private void updateOne(String change)
   {
      String[] split = change.split("_");
      String stepName = split[0];
      String plateName = split[1];
      String states = split[2];

      if (states.length() == 1) {
         Microplate plate = getPlate(plateName);
         plate.getSamples().forEach(sample -> updateSampleAndJob(sample, plate, states, stepName));
      }
      else {
         Microplate plate = getPlate(plateName);
         plate.getSamples().forEach(sample -> updateSampleAndTip(sample, plate, states, stepName));
      }

   }

   private void updateSampleAndJob(Sample sample, Microplate plate, String states, String stepName)
   {
      String jobState = states.equals('S') ? "Planned" : "Failed";
      plate.getJobs().stream()
            .filter(j -> j.getProtocolStepName().equals(stepName))
            .forEach(job -> job.setState(jobState));
   }

   private void updateSampleAndTip(Sample sample, Microplate plate, String states, String stepName)
   {
      int index = plate.getSamples().indexOf(sample);
      char state = states.charAt(index);
      if (state == 'F') {
         sample.setState("Error");
      }
      TipLiquidTransfer tip = sample.getTips().stream()
            .filter(t -> t.getJob().getProtocolStepName().equals(stepName))
            .findFirst().get();

      if (state == 'S') {
         tip.setStatus("Succeeded");
         LiquidTransferJob job = tip.getJob();
         tip.getJob().setState("Succeeded");
      }
      else {
         tip.setStatus("Failed");
         LiquidTransferJob job = tip.getJob();
         if (job.getState().equals("Planned")) {
            job.setState("Failed");
         }
      }
   }

   private Microplate getPlate(String plateName)
   {
      Microplate plate = (Microplate) jobCollection.getLabware().stream()
            .filter(labware -> labware.getName().equals(plateName))
            .findFirst().get();
      return plate;
   }
}
