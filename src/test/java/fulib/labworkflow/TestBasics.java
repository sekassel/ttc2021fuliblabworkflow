package fulib.labworkflow;

import org.fulib.FulibTools;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBasics
{
   @Test
   public void testReadAssay()
   {
      AssayBuilder assayBuilder = new AssayBuilder();

      JobRequest root = assayBuilder.loadAssay("models/test/minimal/initial.xmi");

      assertThat(root).isNotNull();
      assertThat(root.getAssay().getSteps().size()).isEqualTo(8);
      assertThat(root.getSamples().size()).isEqualTo(12);

      FulibTools.objectDiagrams().dumpSVG("tmp/minimalAssay", root);
   }
}
