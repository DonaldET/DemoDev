# SimpleSums.R - use standard R functions to accumulate sums
library(e1071)
library(stats)

simsum.sum <- function(test.df) {
  stopifnot(!is.na(test.df), class(test.df) == "data.frame")
  return(sum(test.df[, "value"]))
}

simsum.loop <- function(test.df) {
  stopifnot(!is.na(test.df), class(test.df) == "data.frame")
  sum <- 0
  for (e in test.df[, "value"]) {
    sum <- sum + e
  }
  return(sum)
}

simsum.moment <- function(test.df) {
  stopifnot(!is.na(test.df), class(test.df) == "data.frame")
  values <- test.df[, "value"]
  avg <- moment(values, order = 1, center = FALSE, absolute = FALSE,
                na.rm = FALSE)
  names(avg) <- c()
  return(avg * length(values))
}

simsum.lm <- function(test.df) {
  stopifnot(!is.na(test.df), class(test.df) == "data.frame")
  avg <- lm(value ~ 1, test.df)$coefficient
  names(avg) <- c()
  return(avg * NROW(test.df))
}
