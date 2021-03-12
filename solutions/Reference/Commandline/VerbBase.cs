using System;
using System.Collections.Generic;
using System.Text;

namespace TTC2021.LabWorkflows.Commandline
{
    internal abstract class VerbBase
    {
        protected abstract void ExecuteCore();

        public void Execute()
        {
            try
            {
                ExecuteCore();
            }
            catch(Exception exception)
            {
                Console.Error.WriteLine( exception );
                Environment.ExitCode = 1;
            }
        }
    }
}
