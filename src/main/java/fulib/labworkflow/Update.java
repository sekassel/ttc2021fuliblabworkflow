package fulib.labworkflow;

import fulib.labworkflow.tables.JobCollectionTable;
import fulib.labworkflow.tables.MicroplateTable;
import fulib.labworkflow.tables.SampleTable;
import fulib.labworkflow.tables.TipLiquidTransferTable;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

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

      new JobCollectionTable(jobCollection)
            .expandJobs("job")
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
      String states = split[2];


      if (states.length() == 1) {
         updateJobJob(states, stepName);
      }
      else {
         updateSamplesAndTips(stepName, states);
      }

   }


   private void updateJobJob(String states, String stepName)
   {
      String jobState = states.equals("S") ? "Succeeded" : "Failed";
      new JobCollectionTable(jobCollection)
            .expandLabware("plate")
            .filterMicroplate()
            .expandJobs("job")
            .filter(j -> j.getProtocolStepName().equals(stepName))
            .forEach(job -> job.setState(jobState));
   }

   private void updateSamplesAndTips(String stepName, String states)
   {
      new JobCollectionTable(jobCollection)
            .expandLabware("plate")
            .filterMicroplate().expandSamples("sample")
            .forEach(sample -> updateOneSampleAndTip(sample, states, stepName));
   }


   private void updateOneSampleAndTip(Sample sample, String states, String stepName)
   {
      JobRequest jobRequest = sample.getJobRequest();
      int index = jobRequest.getSamples().indexOf(sample);
      char state = index >= states.length() ? 'F' : states.charAt(index);
      if (state == 'F') {
         sample.setState("Error");
      }

      TipLiquidTransferTable tipTable = new SampleTable(sample)
            .expandTips("tip")
            .filter(t -> t.getJob().getProtocolStepName().equals(stepName));
      TipLiquidTransfer tip = tipTable.get(0);

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

}
