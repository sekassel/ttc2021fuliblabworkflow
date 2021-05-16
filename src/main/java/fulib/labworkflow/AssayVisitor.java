package fulib.labworkflow;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.beans.PropertyChangeListenerProxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

public class AssayVisitor
{
   private JobRequest jobRequest;
   private Assay assay;

   public JobRequest getJobRequest()
   {
      return jobRequest;
   }

   Map<String, Function<Element, Object>> methodMap = null;

   public void visit(Element root)
   {
      initMethodMap();
      String tagName = root.getTagName();

      Function<Element, Object> visitFunction = methodMap.get(tagName);

      if (visitFunction != null) {
         visitFunction.apply(root);
      }
      else
      {
         Logger.getGlobal().severe(String.format("Don't know how to handle %s", tagName));
      }
   }

   private void initMethodMap()
   {
      if (methodMap == null) {
         methodMap = new LinkedHashMap<>();

         methodMap.put("lab:JobRequest", this::visitLabJobRequest);
         methodMap.put("assay", this::visitAssay);
         methodMap.put("steps", this::visitStep);
         methodMap.put("samples", this::visitSample);
         methodMap.put("reagents", this::visitReagent);
      }
   }

   private Object visitReagent(Element element)
   {
      Reagent reagent = new Reagent();
      String name = element.getAttribute("name");
      reagent.setName(name);
      assay.withReagents(reagent);
      return reagent;
   }

   private Object visitSample(Element element)
   {
      Sample sample = new Sample();

      String sampleID = element.getAttribute("sampleID");
      sample.setSampleID(sampleID).setState("Waiting");

      jobRequest.withSamples(sample);
      return null;
   }

   private Object visitStep(Element element)
   {
      String type = element.getAttribute("xsi:type");
      if (type.equals("lab:DistributeSample")) {
         DistributeSample step = new DistributeSample();
         String id = element.getAttribute("id");
         step.setId(id);
         String volumeTxt = element.getAttribute("volume");
         double volume = Double.parseDouble(volumeTxt);
         step.setVolume(volume);

         setPreviousStep(element, step);
         assay.withSteps(step);
      }
      else if (type.equals("lab:Incubate")) {
         Incubate step = new Incubate();
         String id = element.getAttribute("id");
         step.setId(id);
         String txt = element.getAttribute("temperature");
         double value = Double.parseDouble(txt);
         step.setTemperature(value);
         txt = element.getAttribute("duration");
         int duration = Integer.parseInt(txt);
         step.setDuration(duration);

         setPreviousStep(element, step);

         assay.withSteps(step);
      }
      else if (type.equals("lab:Wash")) {
         Wash step = new Wash();
         String id = element.getAttribute("id");
         step.setId(id);

         setPreviousStep(element, step);
         assay.withSteps(step);
      }
      else if (type.equals("lab:AddReagent")) {
         AddReagent step = new AddReagent();
         String id = element.getAttribute("id");
         step.setId(id);
         String volumeTxt = element.getAttribute("volume");
         double volume = Double.parseDouble(volumeTxt);
         step.setVolume(volume);
         String ref = element.getAttribute("reagent");
         reagentMap.put(step, ref);
         setPreviousStep(element, step);
         assay.withSteps(step);
      }
      return null;
   }

   private LinkedHashMap<AddReagent, String> reagentMap = new LinkedHashMap<>();

   private void setPreviousStep(Element element, ProtocolStep step)
   {
      String previousTxt = element.getAttribute("previous");
      if (previousTxt != null && previousTxt.length() > 0) {
         int pos = previousTxt.indexOf("@steps.");
         String numText = previousTxt.substring(pos + "@steps.".length());
         int i = Integer.parseInt(numText);
         ProtocolStep previous = assay.getSteps().get(i);
         previous.setNext(step);
      }
   }

   private Object visitAssay(Element element)
   {
      assay = new Assay();
      jobRequest.setAssay(assay);
      String name = element.getAttribute("name");
      assay.setName(name);

      visitChildNodes(element);

      for (Map.Entry<AddReagent, String> entry : reagentMap.entrySet()) {
         AddReagent step = entry.getKey();
         String ref = entry.getValue();
         int pos = ref.indexOf("@reagents.");
         String numText = ref.substring(pos + "@reagents.".length());
         int i = Integer.parseInt(numText);
         Reagent reagent = assay.getReagents().get(i);
         step.setReagent(reagent);
      }

      return assay;
   }

   private Object visitLabJobRequest(Element element)
   {
      jobRequest = new JobRequest();

      visitChildNodes(element);

      return jobRequest;
   }

   private void visitChildNodes(Element element)
   {
      NodeList childNodes = element.getChildNodes();

      for (int i = 0; i < childNodes.getLength(); i++) {
         Node node = childNodes.item(i);
         if (node instanceof Element) {
            visit((Element) node);
         }
         else if (node instanceof Text) {
            // ignore
         }
         else {
            Logger.getGlobal().severe("child is no element");
         }

      }
   }


}
