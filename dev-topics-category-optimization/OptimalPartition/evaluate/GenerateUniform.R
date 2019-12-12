# GenerateUniform.R

script.dir <- dirname(sys.frame(1)$ofile)
cat(paste('\n-- Generate Sorted Uniform Distribution in:\n  ', script.dir))

source(paste0(script.dir, '/DataPrep.R'))

gc()

label <- 'UNIFORM'
n <- 800
set.seed(5147)

send_uniform_out <- function(lth, label, fname) {
  values <- data_prep(label, runif(lth))
  file_path <- paste0(script.dir, '/', fname)
  colnm <- names(values)
  write.table(x = values, file = file_path, row.names = TRUE, col.names = FALSE, sep = ",")
  return(summary(values))
}

trigger_uniform <- function(lth, label, fname) {
  cat(paste0('\n\n--- Generate ', lth, ' scores of type ', label, ' into file ', fname, '\n'))
  smry <- send_uniform_out(n, label, fname)
  cat(smry)
}

trigger_uniform(n, label, 'UNIF1.csv')
trigger_uniform(n, label, 'UNIF2.csv')
trigger_uniform(n, label, 'UNIF3.csv')
