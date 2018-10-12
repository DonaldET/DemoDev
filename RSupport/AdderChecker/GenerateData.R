# GenerateData.R - Build a dataframe with randomized scaled values.
RAND_SEED <- 2699
LARGE_PRIME <- 7919

set.seed(RAND_SEED)

genData <- function(n) {
  stopifnot(!is.na(n), mode(n) == "numeric", n > 0)
  return(data.frame(value = sample(1:n)/LARGE_PRIME))
}

exact.sum <- function(n) {
  stopifnot(!is.na(n), mode(n) == "numeric", n > 0)
  int.sum <- if (n%%2 == 0)
    (n/2) * (n + 1) else ((n + 1)/2) * n
  return(int.sum/LARGE_PRIME)
}
