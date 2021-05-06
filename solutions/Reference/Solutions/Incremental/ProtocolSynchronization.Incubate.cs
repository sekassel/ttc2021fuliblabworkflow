using NMF.Expressions.Linq;
using NMF.Synchronizations;
using System;
using System.Collections.Generic;
using System.Text;
using TTC2021.LabWorkflows.JobCollection;
using TTC2021.LabWorkflows.LaboratoryAutomation;

namespace TTC2021.LabWorkflows.Solutions
{
    internal partial class ProtocolSynchronization
    {
        public class IncubateToJobsRule : ProtocolStepRule<Incubate>
        {
            public override void DeclareSynchronization()
            {
                MarkInstantiatingFor( SyncRule<ProtocolStepToJobsRule>() );

                SynchronizeManyLeftToRightOnly(
                    SyncRule<IncubateToIncubateJob>(),
                    ( step, context ) => from plate in GetPlates( context )
                                         where plate.AnyValidSample.Value
                                         select Tuple.Create( step, plate ),
                    ( jobsOfStep, _ ) => jobsOfStep.Jobs.OfType<IJob, IncubateJob>() );
            }
        }

        public class IncubateToIncubateJob : SynchronizationRule<Tuple<Incubate, ProcessPlate>, IncubateJob>
        {
            public override void DeclareSynchronization()
            {
                SynchronizeLeftToRightOnly( tuple => tuple.Item1.Duration, incubate => incubate.Duration );
                SynchronizeLeftToRightOnly( tuple => tuple.Item1.Temperature, incubate => incubate.Temperature );
                SynchronizeLeftToRightOnly( SyncRule<ProcessPlateToMicroplate>(), tuple => tuple.Item2, incubate => incubate.Microplate as Microplate );

                SynchronizeManyLeftToRightOnly(
                    ( step, _ ) => step.Item2.AllSamples,
                    ( job, context ) => GetAffectedSamples( context, job ) );

                SynchronizeRightToLeftOnly(
                    step => AreAllFailed( step.Item2.AllSamples ),
                    job => job.State == JobStatus.Failed );
            }
        }
    }
}
