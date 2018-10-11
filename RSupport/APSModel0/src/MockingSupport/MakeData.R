# Create named unit-sales samples and save them

source("src/MockingSupport/MockData.R")
source("src/Support/ReadProperties.R")
source("src/Support/Utilities.R")


# A property file as input suppplies a name and parameters, this
# creates and saves everything
buildSample <- function(propertyFile) {

  stopifnot(!is.null(propertyFile), !is.na(propertyFile), is.character(propertyFile))

  pvList <- readProperties(propertyFile)
  stopifnot(!is.null(pvList), is.list(pvList))
  stopifnot(length(pvList) > 6)

  name <- pvList[["name"]]
  numDepts <- as.numeric(pvList[["numDepts"]])
  numStoresPerDept <- as.numeric(pvList[["numStoresPerDept"]])
  numStyles <- as.numeric(pvList[["numStyles"]])
  minNumColors <- as.numeric(pvList[["minNumColors"]])
  maxNumColors <- as.numeric(pvList[["maxNumColors"]])
  batchCount <- as.numeric(pvList[["batchCount"]])

  gc()

  sampleData <- makeSales(numDepts = numDepts, numStoresPerDept = numStoresPerDept,
                          numStyles = numStyles, minNumColors = minNumColors, maxNumColors = maxNumColors,
                          batchCount = batchCount)
  stopifnot(!is.null(sampleData), is.data.frame(sampleData), length(sampleData) >
              0)

  save(sampleData, file = paste("resources/sampledata", paste(name, "rda",
                                                              sep = "."), sep = "/"))

  return(length(sampleData$units))
}


# Create all data samples and return observation count for each one
# created
buildAllSamples <- function() {

  fileList <- paste("resources/SampleData", c("Sample1.properties", "Sample2.properties",
                                              "Sample4.properties", "Sample8.properties",
                                              "Sample23.properties"), sep = "/")

  result <- sapply(X = fileList, FUN = buildSample)

  return(result)
}
