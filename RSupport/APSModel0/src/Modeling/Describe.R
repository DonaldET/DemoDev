# Compute elementary descriptive statistis manually on data frames


DsDept <- function(df) {

  stopifnot(!is.null(df), !is.na(df), is.data.frame(df))

  u <- aggregate.data.frame(x = df$units, by = list(dept = df$dept),
                            FUN = mean, simplify = TRUE)

  names(u) <- c("dept", "uDept")

  return(u)
}


DsStore <- function(df) {

  stopifnot(!is.null(df), !is.na(df), is.data.frame(df))

  u <- aggregate.data.frame(x = df$units, by = list(store = df$store),
                            FUN = mean, simplify = TRUE)

  names(u) <- c("store", "uStore")

  return(u)
}


DsStyleColor <- function(df) {

  stopifnot(!is.null(df), !is.na(df), is.data.frame(df))

  u <- aggregate.data.frame(x = df$units, by = list(stylecolor = df$stylecolor),
                            FUN = mean, simplify = TRUE)

  names(u) <- c("stylecolor", "uStylecolor")

  return(u)
}


DsStoreCC <- function(df) {

  stopifnot(!is.null(df), !is.na(df), is.data.frame(df))

  u <- aggregate.data.frame(x = df$units, by = list(store = df$store,
                                                    stylecolor = df$stylecolor), FUN = mean, simplify = TRUE)

  names(u) <- c("store", "stylecolor", "uCC")

  return(u)
}
