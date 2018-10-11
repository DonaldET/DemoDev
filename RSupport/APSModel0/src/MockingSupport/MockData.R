# Create simulated data for departments, stores, stylecolors, and units
# sold over a period of time. Units sold are predicted by department,
# store, and stylecolor


# set to ensure repeatable results
getStandardSeed <- function() {
  return(7877)
}


# Create numbered departments starting with a capital D
makeDepartments <- function(numDepts) {

  stopifnot(!is.null(numDepts), !is.na(numDepts), is.numeric(numDepts))
  stopifnot(numDepts > 0)

  out <- character(length = numDepts)
  for (i in 1:numDepts) {
    out[i] <- sprintf("D%02d", i)
  }

  return(sort.int(out))
}


# Create numbered stores starting with capital S
makeStores <- function(firstnum, numStores) {

  stopifnot(!is.null(firstnum), !is.na(firstnum), is.numeric(firstnum))
  stopifnot(firstnum > 0)
  stopifnot(!is.null(numStores), !is.na(numStores), is.numeric(numStores))
  stopifnot(numStores > 0)

  out <- character(length = numStores)
  for (i in 1:numStores) {
    out[i] <- sprintf("S%04d", i)
  }

  return(sort.int(out))
}


# Create numbered stylecolor (CC) options starting with capital Y
makeCCs <- function(numStyles, minNumColors, maxNumColors) {

  stopifnot(!is.null(numStyles), !is.na(numStyles), is.numeric(numStyles))
  stopifnot(numStyles > 0)

  stopifnot(!is.null(minNumColors), !is.na(minNumColors), is.numeric(minNumColors))
  stopifnot(!is.null(maxNumColors), !is.na(maxNumColors), is.numeric(maxNumColors))
  stopifnot(0 < minNumColors & minNumColors <= maxNumColors)

  set.seed(getStandardSeed())
  out <- character(length = numStyles * maxNumColors)
  styleSet <- sample(1:numStyles, numStyles, replace = FALSE)

  j <- 0
  for (style in styleSet) {
    sty <- sprintf("D%04d", style)

    numColors <- sample(minNumColors:maxNumColors, 1)
    colorSet <- sample(1:maxNumColors, numColors, replace = FALSE)
    for (color in colorSet) {
      clr <- sprintf("Y%03d", color)

      j <- j + 1
      out[j] <- paste(sty, clr, sep = "|")
    }
  }

  return(sort.int(out[1:j]))
}


# Create Sales record with units, departments, stores, and stylecolors
makeSales <- function(numDepts, numStoresPerDept, numStyles, minNumColors,
                      maxNumColors, batchCount = 1) {

  stopifnot(!is.null(numDepts), !is.na(numDepts), is.numeric(numDepts))
  stopifnot(numDepts > 0)

  stopifnot(!is.null(numStoresPerDept), !is.na(numStoresPerDept), is.numeric(numStoresPerDept))
  stopifnot(numStoresPerDept > 0)

  stopifnot(!is.null(numStyles), !is.na(numStyles), is.numeric(numStyles))
  stopifnot(numStyles > 0)

  stopifnot(!is.null(minNumColors), !is.na(minNumColors), is.numeric(minNumColors))
  stopifnot(!is.null(maxNumColors), !is.na(maxNumColors), is.numeric(maxNumColors))
  stopifnot(0 < minNumColors & minNumColors <= maxNumColors)

  stopifnot(!is.null(batchCount), !is.na(batchCount), is.numeric(batchCount))
  stopifnot(batchCount > 0)

  df <- data.frame(time = integer(), dept = character(), store = character(),
                   stylecolor = character(), units = integer(), check.rows = TRUE,
                   check.names = TRUE, stringsAsFactors = TRUE)

  set.seed(getStandardSeed())

  time <- 20150000
  for (batch in 1:batchCount) {
    nerr <- 500
    errorTerm <- rnorm(nerr, 0, 3)
    maxUnits <- -1

    depts <- NULL
    stores <- NULL
    stylecolors <- NULL
    units <- NULL

    firstStoreNum <- 1
    deptNum <- 0
    depts <- makeDepartments(numDepts)
    for (dept in depts) {
      deptNum <- deptNum + 1
      effectDept <- deptNum%%5 + 1

      stores <- makeStores(firstStoreNum, numStoresPerDept)
      firstStoreNum <- firstStoreNum + numStoresPerDept

      storeNum <- 0
      for (store in stores) {
        storeNum <- storeNum + 1
        effectStore <- storeNum%%7 + 1

        stylecolors <- makeCCs(numStyles = numStyles, minNumColors = minNumColors,
                               maxNumColors = maxNumColors)

        ccNum <- 0
        for (stylecolor in stylecolors) {
          ccNum <- ccNum + 1
          effectCC <- 1 + ccNum%%6

          t <- time%%nerr + 1
          effectErr <- errorTerm[t]

          # forces linear model with error term
          units <- max(0, effectDept + effectStore + effectCC +
                         effectErr)

          df <- rbind(df, data.frame(time = time, dept = dept,
                                     store = store, stylecolor = stylecolor, units = units,
                                     check.rows = TRUE, check.names = TRUE, stringsAsFactors = TRUE))

          time <- time + sample(0:3, 1)
        }
      }
    }
  }

  u <- df$units
  minval <- max(u)
  df$units <- round(u/minval * 6)

  return(df)
}
