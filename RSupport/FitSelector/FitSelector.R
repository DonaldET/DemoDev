# FitSelector.R - apply different transformations to the predicted (Y)
# variable and assess goodness-of-fit for linear models.  Three kinds
# of mathmatical models are used that correspond to three common
# transformations.  The mock data Y values are transformed and then fit
# to a linear model; then the resulting adjusted R-squared values, a
# goodness-of-fit criterian, are reported.

source("FitTransform.R")
source("MockDataGenerator.R")

# Apply the transformation to the Y value, smoothing it and making it
# more 'predictable' by the X value for a given mathmatical model
applyTransformation <- function(data2fit, tname, tval) {
  stopifnot(!is.null(data2fit), is.data.frame(data2fit), ncol(data2fit) ==
              2, nrow(data2fit) > 0)
  stopifnot(!is.null(tname), is.character(tname), nchar(tname) > 0)
  stopifnot(!is.null(tval), mode(tval) == "function")

  data2fit$y <- tval(data2fit$y)

  return(data2fit)
}


demo <- function(n) {
  stopifnot(!is.null(n), is.numeric(n), n > 0)

  n <- as.numeric(n)
  cat("\n\n**** Demonstrate fitting models with different transformations for",
      n, "observations\n")

  transformations <- defineTransformations()
  testData <- buildData(n)

  ntrans <- length(transformations)
  stopifnot(ntrans > 0)

  ndata <- ncol(testData) - 1
  stopifnot(ndata > 0)
  modelNames <- names(testData[2:ncol(testData)])
  cat("\nThere are ", ndata, "test data models to fit:", paste0(modelNames, collapse = ","))

  transNames <- names(transformations)
  for (idata in 1:ndata) {
    cat("\n\nFitting var ", idata, "==", modelNames[idata], ". . .\n")

    for (itrans in 1:ntrans) {
      tname <- transNames[itrans]

      data2fit <- applyTransformation(testData[, c(1, idata + 1)], tname, transformations[[tname]])

      adj.r.sqr <- fitWithTransform(data2fit)

      cat("\n\tTrans[", itrans, "]:", tname, " == adjusted R-Squared:",
          adj.r.sqr)
    }
  }

  cat("\n\n**** Demo complete\n")

  return(testData)
}
