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

      FulibTools.objectDiagrams().dumpSVG("tmp/minimalJobCollection", jobCollection);

      List<Path> list = Files.list(Paths.get("models/test/minimal")).collect(Collectors.toList());
      Pattern pattern = Pattern.compile("change(\\d+)\\.txt");
      for (Path path : list) {
         String fileName = path.toString();
         if (fileName.matches(".*change(\\d+)\\.txt")) {
            String content = Files.readString(path, StandardCharsets.UTF_8);
            Update update = new Update();
            update.update(jobCollection, content);
         }
      }

      FulibTools.objectDiagrams().dumpSVG("tmp/afterUpdates", jobCollection);
   }
}
