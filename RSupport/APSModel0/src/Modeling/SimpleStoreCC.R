# Fit linear model with LM using Store-StyleColor as 'predictor' to get
# averages; support other models as well


# Represents standard options for LM model run
linearModel <- function(formula, df) {

  stopifnot(!is.null(formula), typeof(formula) == "language")
  stopifnot(!is.null(df), !is.na(df), is.data.frame(df))

  # Note, qr=FALSE for production; singular.ok = FALSE stops bad models
  # from fitting and getting poor R-Squared values, should only be FALSE
  # when model and data are expected to be good
  lmFit <- lm(formula = formula, data = df, singular.ok = TRUE, model = FALSE,
              x = FALSE, y = FALSE, qr = TRUE)

  return(lmFit)
}

fitStoreCC <- function(df) {
  return(linearModel(formula = units ~ store:stylecolor - 1, df = df))
}


fitStoreCCInt <- function(df) {
  return(linearModel(formula = units ~ store:stylecolor, df = df))
}


fitCCOnly <- function(df) {
  return(linearModel(formula = units ~ stylecolor - 1, df = df))
}


fitStoreOnly <- function(df) {
  return(linearModel(formula = units ~ store - 1, df = df))
}
