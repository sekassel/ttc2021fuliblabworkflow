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

        public class WashToJobsRule : ProtocolStepRule<Wash>
        {
            public override void DeclareSynchronization()
            {
                MarkInstantiatingFor( SyncRule<ProtocolStepToJobsRule>() );

                SynchronizeManyLeftToRightOnly(
                    SyncRule<WashToWashJob>(),
                    ( step, context ) => from plate in GetPlates( context )
                                         where plate.AnyValidSample.Value
                                         select Tuple.Create( step, plate ),
                    ( jobsOfStep, _ ) => jobsOfStep.Jobs.OfType<IJob, WashJob>() );
            }
        }

        public class WashToWashJob : SynchronizationRule<Tuple<Wash, ProcessPlate>, WashJob>
        {
            public override void DeclareSynchronization()
            {
                SynchronizeLeftToRightOnly( SyncRule<ProcessPlateToMicroplate>(), tuple => tuple.Item2, wash => wash.Microplate as Microplate );

                SynchronizeManyLeftToRightOnly(
                    tuple => tuple.Item2.Columns.SelectMany( c => c.Samples.Where( s => s.Sample.State != SampleState.Error ).Select( s => s.Well ) ),
                    wash => wash.Cavities );

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
