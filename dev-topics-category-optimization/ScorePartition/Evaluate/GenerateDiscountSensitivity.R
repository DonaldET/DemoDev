# GenerateUniform.R

script.dir <- dirname(sys.frame(1)$ofile)
cat(paste('\n-- Capture Sorted Discount Sensitivity Distribution in:\n  ', script.dir))

source(paste0(script.dir, '/DataPrep.R'))

gc()

raw.sample <- 'ScorePartition/Evaluate/DS_Sample/SampleBLscoreNonZero.csv'
label <- 'DSENS'
n <- NULL
set.seed(5147)

read_captured_sample <- function(fname) {
  scores <- read.csv(fname, header = FALSE, stringsAsFactors = FALSE)
  names(scores) <- c('masterkey', 'bl_score')
  return (scores)
}

send_dsensitivity_out <- function(label, fname) {
  dsens.df <- read_captured_sample(raw.sample)
  values <- as.vector(data_prep(label, dsens.df$bl_score))
  n <- nrow(values)
  file_path <- paste0(script.dir, '/', fname)
  colnm <- names(values)
  cat('--- loaded', n, ' records from', raw.sample, '\n')
  write.table(x = values, file = file_path, row.names = TRUE, col.names = FALSE, sep = ",")
  return(summary(values))
}

trigger_dsensitivity <- function(label, fname) {
  cat(paste0('\n\n--- Ingest sampled scores of type ', label, ' and save into file ', fname, '\n'))
  smry <- send_dsensitivity_out(label, fname)
  cat(smry)
}


trigger_dsensitivity(label, 'DS1.csv')
