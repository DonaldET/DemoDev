# analyze.R - Analyze call data

CALL_DATA_FILE <- "runcallgen.csv"

readData <- function(fname) {
  cat("\nReading", fname, "\n")
  dfin <- read.csv(fname)
  cat(". . . read", nrow(dfin), "rows\n")
  return(dfin)
}

analyz <- function(fln = CALL_DATA_FILE) {
  df <- readData(fln)
  cat("\nHEAD", fln, "\n")
  head(df)
  cat("\nSummary", fln, "\n")
  summary(df)
  return(df)
}

infile <- CALL_DATA_FILE
call.df <- analyz(infile)
cat("\nHEAD", infile, "\n")
head(call.df)
cat("\nSummary", infile, "\n")
summary(call.df)

