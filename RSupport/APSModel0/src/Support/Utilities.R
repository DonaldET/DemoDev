# Utilities for modeling support

library(stringr)
library(faraway)


# package global assignment for use in timming function execution
timeFit <- function(result, timedFunction, df) {

  stopifnot(!is.null(result), typeof(result) == "character")
  stopifnot(!is.null(df), is.data.frame(df))
  stopifnot(!is.null(timedFunction), typeof(timedFunction) == "closure")

  return(system.time(assign(result, timedFunction(df), envir = .GlobalEnv),
                     gcFirst = TRUE))
}


# Convert a raw string representation of a factor to a string, removing
# the factor designator at the beginning
factor2String <- function(strVal, flag) {

  stopifnot(!is.null(strVal), !is.na(strVal), is.character(strVal))
  stopifnot(!is.null(flag), !is.na(flag), is.character(flag))

  ret <- str_trim(strVal)
  if (length(ret) < 1)
    return(ret)

  flg <- str_trim(flag)
  if (length(flg) < 1 || length(ret) < length(flg))
    return(ret)

  return(str_replace(ret, flg, ""))
}


# Convert string version of Store factor into a string without the
# store elements
zzp_storeFactor2String <- function(strVal) {
  return(factor2String(strVal, flag = "store"))
}


# Convert string version of Stylecolor factor into a string without the
# stylecolor elements
styleColorFactor2String <- function(strVal) {
  return(factor2String(strVal, flag = "stylecolor"))
}


# Convert string version of Store/Stylecolor factor into a string
# without the store and stylecolor elements
storeCCFactor2String <- function(strVal) {
  return(styleColorFactor2String(zzp_storeFactor2String(strVal)))
}


# Return goodness-of-fit, coefficient estimates, SE, t-value, p-value
getFitInfo <- function(fit, formatCoef = storeCCFactor2String) {

  stopifnot(!is.null(fit), is.list(fit), length(fit) > 0)
  stopifnot(!is.null(formatCoef), typeof(formatCoef) == "closure")

  summ <- summary(fit)
  df <- zzp_goodnessOfFit(summ)
  df <- zzp_addCoefficients(df, summ$coefficients, formatCoef)

  return(df)
}


# Add coefficients of fit
zzp_addCoefficients <- function(df, coef, formatCoef) {
  n <- dim(coef)[1]
  coefKeys <- row.names(coef)
  for (i in 1:n) {
    cent <- coef[i, ]
    df <- rbind(df, data.frame(key = formatCoef(coefKeys[i]), value = c(as.numeric(cent[1])),
                               std.error = c(as.numeric(cent[2])), t.value = c(as.numeric(cent[3])),
                               p.value = c(as.numeric(cent[4])), check.names = TRUE, stringsAsFactors = FALSE))
  }

  return(df)
}


# Add goodness of fit
zzp_goodnessOfFit <- function(summ) {
  n <- summ$df[1] + summ$df[2]
  pdf <- summ$df[1]
  rdf <- n - pdf
  sigma.hat <- summ$sigma
  r.squared <- summ$r.squared
  adj.r.squared <- summ$adj.r.squared
  nsingular <- summ$df[3] - summ$df[1]

  fitValues <- c(n, pdf, rdf, sigma.hat, r.squared, adj.r.squared, nsingular)
  nf <- length(fitValues)
  fitNames <- c("FIT.N", "FIT.dfP", "FIT.dfR", "FIT.Residual_SE", "FIT.R-Squared",
                "FIT.Adj-R-Squared", "FIT.Singular")
  stopifnot(nf == length(fitNames))

  nonum <- numeric(length = nf)
  df <- data.frame(key = fitNames, value = fitValues, std.error = nonum,
                   t.value = nonum, p.value = nonum, check.names = TRUE, stringsAsFactors = FALSE)

  return(df)
}
