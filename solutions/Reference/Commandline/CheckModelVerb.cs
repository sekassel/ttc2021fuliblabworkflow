using CommandLine;
using NMF.Models;
using NMF.Models.Repository;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using TTC2021.LabWorkflows.JobCollection;

namespace TTC2021.LabWorkflows.Commandline
{
    [Verb("check")]
    internal class CheckModelVerb : VerbBase
    {
        private string Scenario;
        private string ModelPath;
        private string RunIndex;
        private int Sequences;
        private string Tool;
        private string Model;

        private static string[] AllowedStepNames =
        {
            "AddSample",
            "BindAntibodies",
            "WashAfterAntibodiesBound",
            "AddConjugate",
            "BindConjugate",
            "WashConjugate",
            "AddSubstrate",
            "WaitForColorReaction"
        };

        protected override void ExecuteCore()
        {
            ModelPath = Environment.GetEnvironmentVariable( nameof( ModelPath ) );
            RunIndex = Environment.GetEnvironmentVariable( nameof( RunIndex ) );
            Sequences = int.Parse( Environment.GetEnvironmentVariable( nameof( Sequences ) ) );
            Tool = Environment.GetEnvironmentVariable( nameof( Tool ) );
            Model = Environment.GetEnvironmentVariable( nameof( Model ) );
            Scenario = Environment.GetEnvironmentVariable( nameof( Scenario ) );

            var repository = new ModelRepository();

            CheckOutput( repository.Resolve( Path.Combine( ModelPath, "results", $"initialResult-{Tool}.xmi" ) ), BenchmarkPhase.Initial );
            for(int i = 1; i <= Sequences; i++)
            {
                CheckOutput( repository.Resolve( Path.Combine( ModelPath, "results", $"change{i:00}Result-{Tool}.xmi" ) ), BenchmarkPhase.Update, i );
            }
        }

        private void CheckOutput(Model resultModel, BenchmarkPhase phase, int? iteration = null)
        {
            var description = phase switch
            {
                BenchmarkPhase.Initial => "initial result",
                BenchmarkPhase.Update => $"model after update {iteration}",
                _ => throw new ArgumentOutOfRangeException( nameof( phase ) )
            };
            var jobCollection = resultModel.RootElements.OfType<IJobCollection>().FirstOrDefault();

            if (jobCollection == null)
            {
                throw new ArgumentException( "No job collection found in " + description );
            }

            AssertEmpty( jobCollection.Jobs.Select( job => job.ProtocolStepName ).Distinct().Where( name => !AllowedStepNames.Any(name.StartsWith) ), "protocol step names", description );

            AssertEmpty( from liquidTransfer in jobCollection.Jobs.OfType<ILiquidTransferJob>()
                         from tipCavity in liquidTransfer.Tips
                         where tipCavity.TargetCavityIndex < 0 || tipCavity.TargetCavityIndex >= 96
                         select tipCavity.TargetCavityIndex.ToString(), "target cavity indices", description );

            AssertEmpty( from liquidTransfer in jobCollection.Jobs.OfType<ILiquidTransferJob>()
                         from tipCavity in liquidTransfer.Tips
                         group tipCavity by (liquidTransfer.Target?.Name, tipCavity.TargetCavityIndex, liquidTransfer.ProtocolStepName) into transferGroup
                         where transferGroup.Count() > 1
                         select $"{transferGroup.Key.Name} into cavity {transferGroup.Key.TargetCavityIndex} for {transferGroup.Key.ProtocolStepName} ({transferGroup.Count()} hits)",
                         "cavities used multiple times",
                         description );

            Report( phase, iteration, resultModel.Descendants().Count() );
        }

        private void Report( BenchmarkPhase phase, int? iteration, int elements )
        {
            Console.WriteLine( $"{Tool};{Scenario};{Model};{RunIndex};{iteration ?? 0};{phase};Elements;{elements}" );
        }

        private void AssertEmpty( IEnumerable<string> forbiddenQuery, string description, string modelDescription )
        {
            var list = forbiddenQuery.ToList();
            if (list.Any())
            {
                throw new ArgumentException( $"The {description} in the {modelDescription} {string.Join( ',', list )} are not allowed" );
            }
        }
    }
}
