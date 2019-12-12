# GenerateBeta.R

library('stats')

script.dir <- dirname(sys.frame(1)$ofile)
cat(paste('\n-- Generate Sorted Beta Distribution in:\n  ', script.dir))

source(paste0(script.dir, '/DataPrep.R'))

gc()

label <- 'BETA'
n <- 800
set.seed(5147)

send_beta_out <- function(lth, label, fname) {
  s.alpha <- 0.5
  s.beta <- 0.5
  cat('\n--- output', label, 'to', fname, 'with alph', s.alpha, 'and', s.beta, '\n')
  values <- data_prep(label, rbeta(lth, s.alpha, s.beta))
  file_path <- paste0(script.dir, '/', fname)
  colnm <- names(values)
  write.table(x = values, file = file_path, row.names = TRUE, col.names = FALSE, sep = ",")
  return(summary(values))
}

trigger_beta <- function(lth, label, fname) {
  cat(paste0('\n\n--- Generate ', lth, ' scores of type ', label, ' into file ', fname))
  smry <- send_beta_out(n, label, fname)
  cat(smry)
}

trigger_beta(n, label, 'BETA1.csv')
trigger_beta(n, label, 'BETA2.csv')
trigger_beta(n, label, 'BETA3.csv')
