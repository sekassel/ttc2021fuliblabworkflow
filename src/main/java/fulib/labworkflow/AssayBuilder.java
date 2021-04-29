package fulib.labworkflow;

import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class AssayBuilder
{

   private JobRequest jobRequest;

   public JobRequest loadAssay(String filename)
   {
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();
         Document document = builder.parse(new File(filename));

         Element root = document.getDocumentElement();
         root.normalize();

         AssayVisitor visitor = new AssayVisitor();
         visitor.visit(root);

         jobRequest = visitor.getJobRequest();

         return jobRequest;
      }
      catch (Exception e) {
         e.printStackTrace();
      }

      return null;
   }

}
