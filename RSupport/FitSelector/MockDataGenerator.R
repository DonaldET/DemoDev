# MockDataGenerator.R - create working mock data sets that will be used
# to fit (in a linear model) transformed predicted variables to
# optimize goodness-of-fit values.

# Allows for reproducability
set.seed(7573)

# A polynomial (power of 2) - transform with SQRT
mockPower <- function(x) {
  return(0.027 * x * x + rnorm(1, mean = 0, sd = 1))
}

# An exponential - transform with LOG
mockExp <- function(x) {
  return(0.313 * exp(0.27 * x) + rnorm(1, mean = 0, sd = 1))
}

# An inverse - transform with 1/X
mockInv <- function(x) {
  return(37/(x + 1) + rnorm(1, mean = 0, sd = 1))
}

# Create a data frame with multiple predicted (Y) values for the same
# set of predictor (X) values
buildData <- function(n) {
  x <- 1:n
  y.inv <- sapply(x, FUN = mockInv)
  y.ex <- sapply(x, FUN = mockExp)
  y.pow <- sapply(x, FUN = mockPower)

  df <- data.frame(x = x, y.inv = y.inv, y.ex = y.ex, y.pow = y.pow,
                   stringsAsFactors = FALSE)

  return(df)
}

# Quick plot of constructed data
showData <- function(n) {
  df <- buildData(n)

  plot(y.inv ~ x, data = df)
  plot(y.ex ~ x, data = df)
  plot(y.pow ~ x, data = df)

  return(df)
}

########################################################################

########################################################################

########################################################################

# This is data for a logistic regression example. We will not use it in
# this demonstration.  See
# http://www.inside-r.org/packages/cran/arm/docs/invlogit
logEx <- function() {
  library(arm)

  n <- 100
  x1 <- rnorm(n)
  x2 <- rbinom(n, 1, 0.5)
  b0 <- 1
  b1 <- 1.5
  b2 <- 2
  Inv.logit <- invlogit(b0 + b1 * x1 + b2 * x2)
  plot(b0 + b1 * x1 + b2 * x2, Inv.logit)
}

# This is data for a logistic regression example. We will not use it in
# this demonstration.  See
# http://ww2.coastal.edu/kingw/statistics/R-tutorials/logistic.html
logEx2 <- function() {
  library("MASS")
  data(menarche)

  cat("\nMenarche Data\n")
  print(menarche)

  cat("\n==Menarche Data Summary==\n")
  print(summary(menarche))

  cat("\nPlot Menarche\n")
  plot(Menarche/Total ~ Age, data = menarche)

  return(menarche)
}
