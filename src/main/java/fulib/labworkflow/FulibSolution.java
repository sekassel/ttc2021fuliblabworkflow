package fulib.labworkflow;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FulibSolution
{

   private Path fulibOutputPath;
   private String scenario;
   private String model;
   private JobRequest root;
   private String phase;
   private JobCollection jobCollection;
   private String iteration;
   private Runtime runtime;

   public static void main(String[] args)
   {
      new FulibSolution().run();
   }

   public void run()
   {
      prepareOutput();

      runExample("test", "minimal");
      for (int i = 0; i <= 10; i++) {
         runExample("scale_samples", "" + (long) Math.pow(2, i));
      }
      for (int i = 0; i <= 5; i++) {
         runExample("scale_assay", "" + (long) Math.pow(2, i));
      }
   }

   private void runExample(String scenario, String model)
   {
      System.out.println(String.format("Running scenario %s model %s", scenario, model));
      this.scenario = scenario;
      this.model = model;

      this.root = null;
      this.jobCollection = null;
      this.iteration = "0";
      this.phase = "Load";
      AssayBuilder assayBuilder = new AssayBuilder();
      measure(() -> root = assayBuilder.loadAssay(scenario, model));

      this.phase = "Initial";
      AssayToJobs assayToJobs = new AssayToJobs();
      measure(() -> jobCollection = assayToJobs.initial(root));

      try {
         Path dir = Paths.get(String.format("models/%s/%s", scenario, model));
         List<Path> list = Files.list(dir).collect(Collectors.toList());
         Pattern pattern = Pattern.compile("change(\\d+)\\.txt");
         int i = 1;
         for (Path path : list) {
            String fileName = path.toString();
            if (fileName.matches(".*change(\\d+)\\.txt")) {
               String content = Files.readString(path, StandardCharsets.UTF_8);
               Update update = new Update();
               phase = "Update";
               iteration = "" + i;
               measure(() -> update.update(jobCollection, content));
               i++;
            }
         }
      }
      catch (IOException e) {
         Logger.getGlobal().log(Level.SEVERE, "could not open directory " + model, e);
      }

   }

   private void measure(Runnable job)
   {
      if (runtime == null) {
         runtime = Runtime.getRuntime();
      }

      long startTime = System.nanoTime();
      job.run();
      long endTime = System.nanoTime();
      long usedTime = endTime - startTime;

      try {
         String result = String.format("Fulib;%s;%s;0;%s;%s;Time;%d\n", scenario, model, iteration, phase, usedTime);
         Files.writeString(fulibOutputPath, result, StandardOpenOption.APPEND);

         System.gc();
         System.runFinalization();
         long usedBytes = runtime.totalMemory();
         String memory = String.format("Fulib;%s;%s;0;%s;%s;Memory;%d\n", scenario, model, iteration, phase, usedBytes);
         Files.writeString(fulibOutputPath, memory, StandardOpenOption.APPEND);
      }
      catch (IOException e) {
         Logger.getGlobal().log(Level.SEVERE, "could not write to " + fulibOutputPath.getFileName(), e);
      }
   }

   private void prepareOutput()
   {
      try {
         fulibOutputPath = Files.copy(Paths.get("output/header.csv"), Paths.get("output/fulibTablesOutput.csv"), StandardCopyOption.REPLACE_EXISTING);
      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }
}
