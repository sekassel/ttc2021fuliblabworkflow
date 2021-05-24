package fulib.labworkflow;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import java.beans.PropertyChangeListenerProxy;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;
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

   Map<String, Consumer<Element>> methodMap = null;

   public void visit(Element root)
   {
      initMethodMap();
      String tagName = root.getTagName();

      Consumer<Element> visitFunction = methodMap.get(tagName);

      if (visitFunction != null) {
         visitFunction.accept(root);
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

   private void visitReagent(Element element)
   {
      Reagent reagent = new Reagent();
      String name = element.getAttribute("name");
      reagent.setName(name);
      assay.withReagents(reagent);
   }

   private void visitSample(Element element)
   {
      Sample sample = new Sample();

      String sampleID = element.getAttribute("sampleID");
      sample.setSampleID(sampleID).setState("Waiting");

      jobRequest.withSamples(sample);
   }



   private void visitStep(Element element)
   {
      initStepMap();
      String type = element.getAttribute("xsi:type");
      Consumer<Element> stepConsumer = stepMap.get(type);
      stepConsumer.accept(element);
   }

   LinkedHashMap<String, Consumer<Element>> stepMap = null;

   private void initStepMap()
   {
      if (stepMap == null) {
         stepMap = new LinkedHashMap<>();
         stepMap.put("lab:DistributeSample", this::visitDistributeSampleStep);
         stepMap.put("lab:Incubate", this::visitIncubateStep);
         stepMap.put("lab:Wash", this::visitWashStep);
         stepMap.put("lab:AddReagent", this::visitAddReagentStep);
      }
   }

   private void visitAddReagentStep(Element element)
   {
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

   private void visitWashStep(Element element)
   {
      Wash step = new Wash();
      String id = element.getAttribute("id");
      step.setId(id);

      setPreviousStep(element, step);
      assay.withSteps(step);
   }

   private void visitIncubateStep(Element element)
   {
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

   private void visitDistributeSampleStep(Element element)
   {
      DistributeSample step = new DistributeSample();
      String id = element.getAttribute("id");
      step.setId(id);
      String volumeTxt = element.getAttribute("volume");
      double volume = Double.parseDouble(volumeTxt);
      step.setVolume(volume);

      setPreviousStep(element, step);
      assay.withSteps(step);
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

   private void visitAssay(Element element)
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
   }

   private void visitLabJobRequest(Element element)
   {
      jobRequest = new JobRequest();
      visitChildNodes(element);
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
