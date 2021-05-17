package fulib.labworkflow;

import org.fulib.FulibTools;
import org.junit.Test;
import org.w3c.dom.Element;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBasics
{
   @Test
   public void testReadAssay() throws IOException
   {
      AssayBuilder assayBuilder = new AssayBuilder();

      JobRequest root = assayBuilder.loadAssay("models/test/minimal/initial.xmi");

      assertThat(root).isNotNull();
      assertThat(root.getAssay().getSteps().size()).isEqualTo(8);
      assertThat(root.getSamples().size()).isEqualTo(12);

      FulibTools.objectDiagrams().dumpSVG("tmp/minimalAssay", root);

      AssayToJobs assayToJobs = new AssayToJobs();
      JobCollection jobCollection = assayToJobs.initial(root);

      FulibTools.objectDiagrams().dumpSVG("tmp/jobCollection", jobCollection);

      List<Path> list = Files.list(Paths.get("models/test/minimal")).collect(Collectors.toList());
      Pattern pattern = Pattern.compile("change(\\d+)\\.txt");
      for (Path path : list) {
         String fileName = path.toString();
         if (fileName.matches(".*change(\\d+)\\.txt")) {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            System.out.print("Running update " + fileName + "... ");
            Update update = new Update();
            update.update(jobCollection, content);
            System.out.println("done!");
         }
      }

      FulibTools.objectDiagrams().dumpSVG("tmp/afterUpdates", jobCollection);
   }


   @Test
   public void testReadScaleAssay() throws IOException
   {
      AssayBuilder assayBuilder = new AssayBuilder();

      JobRequest root = assayBuilder.loadAssay("models/scale_assay/1/initial.xmi");

      assertThat(root).isNotNull();
      assertThat(root.getAssay().getSteps().size()).isEqualTo(8);
      assertThat(root.getSamples().size()).isEqualTo(96);

      FulibTools.objectDiagrams().dumpSVG("tmp/scale_assay", root);

      AssayToJobs assayToJobs = new AssayToJobs();
      JobCollection jobCollection = assayToJobs.initial(root);

      // FulibTools.objectDiagrams().dumpSVG("tmp/scale_assayJobCollection", jobCollection);

      List<Path> list = Files.list(Paths.get("models/scale_assay/1")).collect(Collectors.toList());
      Pattern pattern = Pattern.compile("change(\\d+)\\.txt");
      for (Path path : list) {
         String fileName = path.toString();
         if (fileName.matches(".*change(\\d+)\\.txt")) {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            System.out.print("Running update " + fileName + "... ");
            Update update = new Update();
            update.update(jobCollection, content);
            System.out.println("done!");
         }
      }

      // FulibTools.objectDiagrams().dumpSVG("tmp/scale_assay_afterUpdates", jobCollection);
   }

}
