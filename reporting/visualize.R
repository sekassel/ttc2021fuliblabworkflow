library("jsonlite", quietly=T, verbose=F, warn.conflicts=FALSE)
library("ggplot2",quietly=T, verbose=F, warn.conflicts=FALSE)
library("plyr", quietly=T, verbose=F, warn.conflicts=FALSE)
source("functions.R")
source("plot.R")
source("constants.R")

options(error = traceback)

args <- commandArgs(trailingOnly = TRUE)
configPath <- "../config/reporting.json"

results <-read.csv2(resultsPath, header=TRUE, row.names = NULL)

config <- fromJSON(configPath)




if (validPhase(results, config$Summarize_Functions$Phases) == FALSE){
  print("Non existing phasename provided!")
  quit()
}

index <- 0
settings <- PlotSettings()
uniqueModels <- unique(results$Model)

for(row in 1:nrow(config$Summarize_Functions)){
  phases <- config$Summarize_Functions[row,]$Phases
  name <- config$Summarize_Functions[row,]$Name
  index <- index + 1

  for(model in uniqueModels){
    metric <- "Time"
    subData1 <- subset(results, Model==model & MetricName == metric)
    subData1$MetricValue <- as.integer(subData1$MetricValue) * (10**-6)
    
    if (config$Dimensions$Groups$Scenario){
      uniqueTools <- unique(subData1$Tool)
      settings <- setGroup(settings, "Scenario")
      for(tool in uniqueTools){
        subData2 <- subset(subData1, Tool==tool)
        
        if (config$Dimensions$X_Dimensions$Model){
          title <- paste(tool, ", ", model, ", Function: ", concatPhases(phases), sep='')

          print("Got it up to line 48 ")

          settings <- setTitle(settings, title)
          settings <- setDimensions(settings, "Model", "MetricValue")
          settings <- setLabels(settings, "Model", "Time (ms)")
          settings <- setAxis(settings, "Factor", yAxis)
          for (extension in config$Extension){
            fileName <- paste(rootPath, model, "-", tool, "-GroupBy-Scenario-", metric, "-", name, ".",  extension, sep='')
            savePlot(subData2, settings, phases, fileName)
          }
          write.csv(subData2, file = paste(rootPath, model, "-", tool, "-GroupBy-Scenario-", metric, "-", name, ".csv", sep=''))
        }

        if (config$Dimensions$X_Dimensions$Iteration){
          uniqueModels <-unique(subData2$Model)
          for(changeSet in uniqueModels){
            subData3 <- subset(subData2, Model==changeSet)
            title <- paste(tool, ", Model: ", changeSet, ", Function: ", concatPhases(phases), sep='')
            print("Got it up to line 66 ")
            print(changeSet)
            print(tool)
            settings <- setTitle(settings, title)
            settings <- setDimensions(settings, "Iteration", "MetricValue")
            settings <- setLabels(settings, "Iterations", "Time (ms)")
            settings <- setAxis(settings, "Continuous", yAxis)
            for (extension in config$Extension){
              fileName <- paste(rootPath, model, "-", tool, "-changeSet-", changeSet, "-GroupBy-Scenario-", metric, "-", name, ".", extension, sep='')
              print("Got it up to line 75 ")
              print(fileName)
              # savePlot(subData2, settings, phases, fileName)
              # savePlot(subData3, settings, phases, fileName)
            }
            write.csv(subData3, file = paste(rootPath, model, "-", tool, "-Model-", changeSet, "-GroupBy-Scenario-", metric, "-", name, ".csv", sep=''))
            print("Got it up to line 78 ")
            print(changeSet)
            print(tool)
          }
        } 
      }
    }
  }
  
  if (config$Dimensions$Groups$Tool){
    metric <- "Time"
    subData1 <- subset(results, MetricName == metric)
    subData1$MetricValue <- subData1$MetricValue * (10**-6)
    
    uniqueScenarios <- unique(subData1$Scenario)
    settings <- setGroup(settings, "Tool")
    for(view in uniqueScenarios){
      subData2 <- subset(subData1, Scenario==view)
      
      if (config$Dimensions$X_Dimensions$Model){
        title <- paste(view, ", Function: ", concatPhases(phases), sep='')
        settings <- setTitle(settings, title)
        settings <- setDimensions(settings, "Model", "MetricValue")
        settings <- setLabels(settings, "Model", "Time (ms)")
        settings <- setAxis(settings, "Discrete", yAxis)
        for (extension in config$Extension){
          fileName <- paste(rootPath, view, "-GroupBy-Tool-",metric, "-", name, ".", extension, sep='')
          savePlot(subData2, settings, phases, fileName)
        }
        write.csv(ddply(subData2, c("Tool", "Model"), summarise, N=length(MetricValue), mean=mean(MetricValue), sd=sd(MetricValue)), file = paste(rootPath, view, "-GroupBy-Tool-",metric, "-", name, ".csv", sep=''))
      }
      
      if (config$Dimensions$X_Dimensions$Iteration){
        uniqueSizes <-unique(subData2$Model)
        settings <- setDimensions(settings, "Iteration", "MetricValue")
        settings <- setLabels(settings, "Iterations", "Time (ms)")
        settings <- setAxis(settings, "Continuous", yAxis)
        for(size in uniqueSizes){
          subData3 <- subset(subData2, Model==size)
          title <- paste(view, ", Model: ", size, ", Function:  ", concatPhases(phases), sep='')
          for (extension in config$Extension){
            fileName <- paste(rootPath, view, "-Model-", size, "-GroupBy-Tool-", metric, "-", name, ".", extension, sep='')
            settings <- setTitle(settings, title)
            # savePlot(subData3, settings, phases, fileName)
          }
          write.csv(subData3, file = paste(rootPath, view, "-Model-", size, "-GroupBy-Tool-", metric, "-", name, ".csv", sep=''))
        }
      }     
    }
  }
}
