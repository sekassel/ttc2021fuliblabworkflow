package fulib.labworkflow;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class JobCollectionWriter
{

   private Document document;
   private Element root;
   private JobCollection jobCollection;

   public void write(JobCollection jobCollection, String scenario, String model, String phase)
   {
      this.jobCollection = jobCollection;
      try {
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder = factory.newDocumentBuilder();
         document = builder.newDocument();
         document.setXmlVersion("1.0");
         document.setXmlStandalone(true);

         root = document.createElement("jobs:JobCollection");
         // document.appendChild(document.createTextNode("\n"));
         root.setAttribute("xmi:version", "2.0");
         root.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
         root.setAttribute("xmlns:xmi", "http://www.omg.org/XMI");
         root.setAttribute("xmlns:jobs", "http://www.transformation-tool-contest.eu/ttc21/jobCollection");
         document.appendChild(root);

         jobCollection.getLabware().forEach(this::writeLabware);
         jobCollection.getJobs().forEach(this::writeJob);

         TransformerFactory transformerFactory = TransformerFactory.newInstance();
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         DOMSource source = new DOMSource(document);

         String fileName = String.format("models/%s/%s/results/%sResult-Fulib.mxi", scenario, model, phase);
         FileWriter writer = new FileWriter(new File(fileName));
         StreamResult result = new StreamResult(writer);

         transformer.transform(source, result);
      }
      catch (IOException | ParserConfigurationException | TransformerException e) {
         e.printStackTrace();
      }
   }

   private void writeJob(Job job)
   {
      initJobsMap();
      Element element = document.createElement("jobs");
      String simpleName = job.getClass().getSimpleName();
      element.setAttribute("xsi:type", "jobs:" + simpleName);
      element.setAttribute("protocolStepName", job.getProtocolStepName());
      BiConsumer<Job, Element> jobAttributesWriter = jobsMap.get(job.getClass());
      if (jobAttributesWriter != null) {
         jobAttributesWriter.accept(job, element);
      }
      if (job.getPrevious() != null) {
         element.setAttribute("previous", String.format("#//@jobs.%d", jobCollection.getJobs().indexOf(job.getPrevious())));
      }
      if (job.getNext() != null) {
         element.setAttribute("next", String.format("#//@jobs.%d", jobCollection.getJobs().indexOf(job.getNext())));
      }
      root.appendChild(element);
   }

   LinkedHashMap<Class, BiConsumer<Job, Element>> jobsMap = null;
   private void initJobsMap()
   {
      if (jobsMap == null) {
         jobsMap = new LinkedHashMap<>();
         jobsMap.put(LiquidTransferJob.class, this::writeLiquidTransferJobAttributes);
         jobsMap.put(IncubateJob.class, this::writeIncubateJobAttributes);
         jobsMap.put(WashJob.class, this::writeWashJobAttributes);
      }
   }

   private void writeWashJobAttributes(Job job, Element element)
   {
      WashJob washJob = (WashJob) job;
      String collect = washJob.getCavities().stream().map(i -> "" + i).collect(Collectors.joining(" "));
      element.setAttribute("cavities", collect);
      element.setAttribute("microplate", String.format("#//@labware.%d", jobCollection.getLabware().indexOf(washJob.getMicroplate())));
   }

   private void writeIncubateJobAttributes(Job job, Element element)
   {
      IncubateJob incubateJob = (IncubateJob) job;
      element.setAttribute("temperature", "" + incubateJob.getTemperature());
      element.setAttribute("duration", "" + incubateJob.getDuration());
      element.setAttribute("microplate", String.format("#//@labware.%d", jobCollection.getLabware().indexOf(incubateJob.getMicroplate())));
   }

   private void writeLiquidTransferJobAttributes(Job job, Element element)
   {
      LiquidTransferJob liquidTransferJob = (LiquidTransferJob) job;
      Labware target = liquidTransferJob.getTarget();
      int index = jobCollection.getLabware().indexOf(target);
      element.setAttribute("target", String.format("jobs:%s #//@labware.%d", target.getClass().getSimpleName(), index));
      Labware source = liquidTransferJob.getSource();
      index = jobCollection.getLabware().indexOf(source);
      element.setAttribute("source", String.format("jobs:%s #//@labware.%d", source.getClass().getSimpleName(), index));


      liquidTransferJob.getTips().forEach(tip -> this.writeTips(tip, element));
   }

   private void writeTips(TipLiquidTransfer tip, Element element)
   {
      Element tipElement = document.createElement("tips");
      tipElement.setAttribute("volume", "" + (int) tip.getVolume());
      tipElement.setAttribute("sourceCavityIndex", "" + tip.getSourceCavityIndex());
      tipElement.setAttribute("targetCavityIndex", "" + tip.getTargetCavityIndex());
      element.appendChild(tipElement);
   }

   private void writeLabware(Labware labware)
   {
      Element element = document.createElement("labware");
      String simpleName = labware.getClass().getSimpleName();
      element.setAttribute("xsi:type", "jobs:" + simpleName);
      element.setAttribute("name", labware.getName());
      if (labware instanceof TubeRunner) {
         TubeRunner tubeRunner = (TubeRunner) labware;
         String join = String.join(" ", tubeRunner.getBarcodes());
         element.setAttribute("barcodes", join);
      }
      root.appendChild(element);
   }
}
