# FitTransform.R - Provide transformations for the predicted variable
# (Y) that make a linear model achieve better goodness-of-fit results.
# Also, perform LM model fit against differing data and differing
# transformation of the predictor variable

transIdentity <- function(x) {
  return(x)
}

transInv <- function(x) {
  return(ifelse(abs(x) < 1e-12, 1e+12, 1/x))
}

transLog <- function(x) {
  safeLog <- function(x) {
    return(ifelse(abs(x) < 1e-12, 1e+12, log(x)))
  }

  return(safeLog(abs(x)))
}

transSqrt <- function(x) {
  return(sqrt(abs(x)))
}

# Collects together the transformations to apply to predictor variable
defineTransformations <- function() {
  trans <- c(transIdentity, transInv, transLog, transSqrt)
  names(trans) <- c("transIdentity", "transInv", "transLog", "transSqrt")

  return(trans)
}

###########################################################

# Represents standard options for a LM model run
linearModel <- function(formula, df) {

  stopifnot(!is.null(formula), typeof(formula) == "language")
  stopifnot(!is.null(df), !is.na(df), is.data.frame(df))

  # Note, qr=FALSE for production; singular.ok = FALSE stops bad models
  # from fitting and getting poor R-Squared values, should only be FALSE
  # when model and data are expected to be good lmFit <- lm(formula =
  # formula, data = df, singular.ok = TRUE, model = FALSE, x = FALSE, y =
  # FALSE, qr = TRUE)
  lmFit <- lm(formula = formula, data = df)

  return(lmFit)
}

# Fit to an input model with y ~ x and return adjusted r-squared
fitWithTransform <- function(df) {
  stopifnot(!is.null(df), !is.na(df), is.data.frame(df))
  stopifnot(ncol(df) > 0)

  fit <- linearModel(formula = y ~ x, df = df)
  sum <- summary(fit)

  return(sum[["adj.r.squared"]])
}
