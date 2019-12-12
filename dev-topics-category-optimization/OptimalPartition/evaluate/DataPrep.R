# DataPrep.R

data_prep <- function(label, values, scale.factor=1.0e-8) {
  stopifnot(!is.null(values))
  stopifnot(is.vector(values), mode(values) == 'numeric')
  n = length(values)
  stopifnot(n > 3)

  values <- sort(values, na.last = FALSE)
  checked <- vector(mode = 'numeric', n)
  mxv = 1.0e+38
  last = -mxv
  eql_block_cnt <- 0
  laste <- NULL
  altered.count <- 0
  for (i in 1:(n + 1)) {
    x <- if(i <= n) values[i] else mxv
    if (x > last) {
      if (eql_block_cnt > 0) {
        delta <- if(laste == 0.0) scal.factor else abs(laste) * scale.factor
        for (j in 1:(eql_block_cnt + 1)) {
          checked[i - j] <- laste + (eql_block_cnt + 1 - j) * delta
        }
        eql_block_cnt <- 0
        laste <- NULL
        altered.count = altered.count + 1
      }
      last <- x
      if (i <= n) checked[i] <- x
    } else if (x == last) {
      eql_block_cnt = eql_block_cnt + 1
      if (eql_block_cnt == 1 ) {
        laste <- last
      }
    } else {
      stop(paste0('entry', i, 'out of order, x: ', x, ', last: ', last))
    }
  }

  if(altered.count > 0) {
    checked <- sort(checked, na.last = FALSE)
    cat(paste0('\n---- Note: altered ', altered.count, ' blocks of equal values with scale factor ', scale.factor, '\n'))
  }
  dp_vals <- data.frame(xvals=checked, fix.empty.names = FALSE, stringsAsFactors = FALSE)
  return(dp_vals)
}
