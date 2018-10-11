## Read properties from a properties file; name=value.  Note, only one
## equals sign (=) allowed in file

readProperties <- function(propPath) {

  stopifnot(!is.null(propPath), !is.na(propPath), is.character(propPath))

  dataFrame <- read.table(file = propPath, header = FALSE, sep = "=",
                          colClasses = c("character"), stringsAsFactors = FALSE, strip.white = TRUE,
                          nrows = 1000)

  if (is.null(dataFrame) || class(dataFrame) != "data.frame")
    return(NULL)

  colnames(dataFrame) <- c("key", "value")

  pvList <- as.list(as.character(dataFrame$value))
  names(pvList) <- as.character(dataFrame$key)

  return(pvList)
}
