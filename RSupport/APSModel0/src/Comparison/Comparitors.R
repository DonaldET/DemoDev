# Compare LM fit derived coefficients to manually generated
# coefficients using aggregation

source("src/Support/Utilities.R")


# In this comparison, fit is 'observed' (actual) set of values and agg
# is the 'true' (expected) set of values for the means defined by the
# same input data
compareFit2Agg <- function(fit, agg) {

  # validate parameters
  zzp_checkComparitorParams(fit, agg)

  # var names and values from LM fit
  coef <- fit$coefficients
  n <- length(coef)
  cvars <- as.character(names(coef))
  cvals <- as.vector(coef)

  # var names and values from aggregation values
  avars.store <- as.character(agg$store)
  stopifnot(length(avars.store) == n)

  avars.stylecolor <- as.character(agg$stylecolor)
  stopifnot(length(avars.stylecolor) == n)

  avals.ucc <- as.vector(agg$uCC)
  stopifnot(length(avals.ucc) == n)

  # check keys: keys are expected in same order and should match compute
  # diffs
  diffs <- numeric(length = n)
  eps <- numeric(length = n)
  dkey <- character(length = n)
  for (i in 1:n) {
    dkey[i] <- zzp_compareAggCompKeys(avars.store[i], avars.stylecolor[i],
                                      cvars[i])

    exp <- avals.ucc[i]
    obs <- cvals[i]
    delta <- obs - exp

    diffs[i] <- delta
    eps[i] <- delta
    if (exp != 0) {
      eps[i] <- delta/abs(exp)
    }
  }

  df <- data.frame(storeCCu = dkey, aggCCu = agg$uCC, lmCCu = cvals,
                   delta = diffs, eps = eps, stringsAsFactors = FALSE)

  return(df)
}


# Validate input parameter aspects
zzp_checkComparitorParams <- function(fit, agg) {
  stopifnot(!is.null(fit), is.list(fit))
  stopifnot(!is.null(agg), is.list(agg))

  # get/check coefficients
  coef <- fit$coefficients
  stopifnot(!is.null(coef), is.vector(coef))
  n <- length(coef)
  stopifnot(n > 0)

  # get/check aggregation
  stopifnot(length(agg$store) == n)
  stopifnot(length(agg$stylecolor) == n)
  stopifnot(length(agg$uCC) == n)

  # var names and values from regression
  stopifnot(length(as.character(names(coef))) == n)
  stopifnot(length(as.vector(coef)) == n)

  return(n)
}


# Compare string-value keys for aggreggation and computed coefficients
zzp_compareAggCompKeys <- function(store, stylecolor, compKey) {

  stopifnot(!is.null(store), !is.na(store), is.character(store), length(store) >
              0)
  stopifnot(!is.null(stylecolor), !is.na(stylecolor), is.character(stylecolor),
            length(stylecolor) > 0)
  stopifnot(!is.null(compKey), !is.na(compKey), is.character(compKey),
            length(compKey) > 0)

  akey <- paste(store, stylecolor, sep = ":")
  ckey <- storeCCFactor2String(compKey)

  stopifnot(akey == ckey)

  return(akey)
}
