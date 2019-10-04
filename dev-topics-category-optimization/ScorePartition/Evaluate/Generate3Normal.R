# Generate3Normal.R

script.dir <- dirname(sys.frame(1)$ofile)
cat(paste('\n-- Generate Sorted 3 Normal Distributions, Centered Modes, in:\n  ', script.dir))

source(paste0(script.dir, '/DataPrep.R'))

label <- '3Normal'
n <- 800
set.seed(5147)

gc()

truncated_normal <- function(lth, low.bound, upper.bound, mean, stdev) {
  stopifnot(lth >= 0)
  stopifnot(low.bound < upper.bound)
  stopifnot(mean > low.bound, mean < upper.bound)
  stopifnot(stdev > 0)
  
  x <- vector('numeric', lth)
  found <- 0
  collect.limit <- 1000
  for(i in 1:collect.limit) {
    needed <- lth - found
    if (needed < 1) {
      break
    }
    
    q <- rnorm(needed, mean, stdev)
    q <- q[q >= low.bound & q <= upper.bound]
    grab <- min(needed, length(q))
    if (grab > 0) {
      for (j in 1:grab) {
        found <- found + 1
        x[found] <- q[j]
      }
    }
  }
  
  return(x)
}

generate_multi_modal <- function(lth, fract.low, fract.mid) {
  stopifnot(lth > 2)
  stopifnot(fract.low >= 0, fract.low <= 1)
  stopifnot(fract.mid >= 0, fract.mid <= 1)
  fract.upper <- 1 - fract.low - fract.mid
  stopifnot(fract.upper >= 0, fract.upper <= 1)
  
  cnt.low <- floor(fract.low * lth)
  cnt.mid <- floor(fract.mid * lth)
  cnt.upper <- lth - cnt.low - cnt.mid
  
  lb <- 0
  up <- 0.4
  q.low <- truncated_normal(lth = cnt.low, low.bound = lb, upper.bound = up, mean = (lb + up)/ 2, stdev = 0.2)
  cat('\nLow', length(q.low), '\n')
  print(summary(q.low))
  
  lb <- 0.35
  up <- 0.65
  q.mid <- truncated_normal(lth = cnt.mid, low.bound = lb, upper.bound = up, mean = (lb + up)/ 2, stdev = 0.2)
  cat('\nMid', length(q.mid), '\n')
  print(summary(q.mid))

  lb <- 0.50
  up <- 0.9999
  q.upper <- truncated_normal(lth = cnt.upper, low.bound = lb, upper.bound = up, mean = (lb + up)/ 2, stdev = 0.2)
  cat('\nUpper', length(q.upper), '\n')
  print(summary(q.upper))
  
  return(c(q.low, q.mid, q.upper))
}


send_multimodal_out <- function(lth, label, fname) {
  values <- data_prep(label, generate_multi_modal(lth, 0.10, 0.80))
  file_path <- paste0(script.dir, '/', fname)
  colnm <- names(values)
  write.table(x = values, file = file_path, row.names = TRUE, col.names = FALSE, sep = ",")
  return(summary(values))
}


trigger_multi_modal <- function(lth, label, fname) {
  cat(paste0('\n\n--- Generate ', lth, ' scores of type ', label, ' into file ', fname, '\n'))
  smry <- send_multimodal_out(n, label, fname)
  cat(smry)
}


trigger_multi_modal(n, label, 'ThreeNorm1.csv')
trigger_multi_modal(n, label, 'ThreeNorm2.csv')
trigger_multi_modal(n, label, 'ThreeNorm3.csv')
