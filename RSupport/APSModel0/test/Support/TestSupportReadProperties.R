source("src/Support/ReadProperties.R")

propBase <- paste(getwd(), "test/resources/properties", sep = "/")

pv <- readProperties(paste(propBase, "propTest1.properties", sep = "/"))

rm(propBase)
