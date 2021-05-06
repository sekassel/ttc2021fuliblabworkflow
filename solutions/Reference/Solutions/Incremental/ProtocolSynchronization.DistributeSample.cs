using NMF.Expressions.Linq;
using NMF.Synchronizations;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using TTC2021.LabWorkflows.JobCollection;
using TTC2021.LabWorkflows.LaboratoryAutomation;

namespace TTC2021.LabWorkflows.Solutions
{
    internal partial class ProtocolSynchronization
    {
        public class DistributeSampleToJobsRule : ProtocolStepRule<DistributeSample>
        {
            public override void DeclareSynchronization()
            {
                MarkInstantiatingFor( SyncRule<ProtocolStepToJobsRule>() );

                SynchronizeManyLeftToRightOnly(
                    SyncRule<DistributeSampleLiquidTransferToLiquidTransfer>(),
                    ( step, context ) => from plate in GetPlates( context )
                                         from column in plate.Columns
                                         where column.AnyValidSample.Value
                                         select new DistributeSampleLiquidTransfer( plate, column, GetTubes( context ).FirstOrDefault( t => t.Samples.Any( s => column.Samples.Any( s2 => s.Sample == s2.Sample ) ) ), step ),
                    ( jobsOfStep, _ ) => jobsOfStep.Jobs.OfType<IJob, LiquidTransferJob>() );
            }
        }

        public class DistributeSampleLiquidTransferToLiquidTransfer : SynchronizationRule<DistributeSampleLiquidTransfer, LiquidTransferJob>
        {
            public override void DeclareSynchronization()
            {
                SynchronizeLeftToRightOnly( SyncRule<SamplesToTubeRunner>(), distribute => distribute.TubeRunner, liquidTransfer => liquidTransfer.Source as TubeRunner );
                SynchronizeLeftToRightOnly( SyncRule<ProcessPlateToMicroplate>(), step => step.Plate, liquidTransfer => liquidTransfer.Target as Microplate );

                SynchronizeManyLeftToRightOnly( SyncRule<DispenseWellsToTipTransfer>(),
                    step => from s in step.Column.Samples
                            where s.Sample.State != SampleState.Error
                            select new DistributeSampleTip( step, s ),
                    liquidTransfer => new TipCollection( liquidTransfer.Tips ) );

                SynchronizeManyLeftToRightOnly(
                    ( step, _ ) => step.Column.AllSamples,
                    ( liquidTransfer, context ) => GetAffectedSamples( context, liquidTransfer ) );
            }
        }

        public class DispenseWellsToTipTransfer : SynchronizationRule<DistributeSampleTip, ITipLiquidTransfer>
        {
            public override void DeclareSynchronization()
            {
                SynchronizeLeftToRightOnly( well => well.DistributeSample.Volume, transfer => transfer.Volume );
                SynchronizeLeftToRightOnly( well => well.SourceWell.Well, transfer => transfer.SourceCavityIndex );
                SynchronizeLeftToRightOnly( well => well.TargetWell.Well, transfer => transfer.TargetCavityIndex );


                SynchronizeRightToLeftOnly( well => IsSampleFailed( well.TargetWell.Sample ), transfer => transfer.Status == JobStatus.Failed );
            }
        }

        public class DistributeSampleLiquidTransfer
        {
            public DistributeSampleLiquidTransfer( ProcessPlate plate, ProcessColumn column, Tubes tubeRunner, DistributeSample distribute )
            {
                Plate = plate;
                Column = column;
                TubeRunner = tubeRunner;
                Distribute = distribute;
            }

            public ProcessPlate Plate { get; set; }
            public ProcessColumn Column { get; }

            public Tubes TubeRunner { get; set; }

            public DistributeSample Distribute { get; }
        }

        public class DistributeSampleTip
        {
            public DistributeSampleTip( DistributeSampleLiquidTransfer distributeSample, ProcessWell targetWell )
            {
                DistributeSample = distributeSample.Distribute;
                TargetWell = targetWell;
                SourceWell = distributeSample.TubeRunner.Samples.AsEnumerable().FirstOrDefault( w => w.Sample == targetWell.Sample );
            }

            public DistributeSample DistributeSample
            {
                get;
            }

            public ProcessWell TargetWell { get; }

            public ProcessWell SourceWell { get; }
        }
    }
}
