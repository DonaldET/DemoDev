# CompareSums.R - compute summation by various standard R methods and
# compare the relative error of each method using exact sum.
source("GenerateData.R")
source("SimpleSums.R")

TEST_SIZE <- 5.0e+07
labels <- c("stats.lm ", "for.loop ", "moment.u ", "R.std.sum")
funcs <- c(simsum.lm, simsum.loop, simsum.moment, simsum.sum)

run_compare <- function(tst.df, exp.sum, FUN) {
  act.sum <- FUN(tst.df)
  del <- act.sum - exp.sum
  return(c(act.sum, del, del/exp.sum))
}

run_analysis <- function(tst.df, exp.sum, funcs) {
  cmpr <- lapply(funcs, function(f) run_compare(tst.df, exp.sum, f))
  return(cmpr)
}

collect_result <- function(results) {
  result.df <- data.frame(Method = character(), Computed.sum = double(),
                          Delta = double(), Rel.Err = double(), stringsAsFactors = FALSE)
  i <- 0
  for (rslt in results) {
    i <- i + 1
    result.df <- rbind(result.df, data.frame(Method = labels[i], Computed.sum = rslt[1],
                                             Delta = rslt[2], Rel.Err = rslt[3], stringsAsFactors = FALSE))
  }
  return(result.df)
}

#### Main Entry ####

print("")
print("**** Comparing R Summation Methodologies for Accurracy ****")
print("  -- Generate data:")
test.df <- genData(TEST_SIZE)
true.sum <- exact.sum(TEST_SIZE)
print(paste("  -- generated", NROW(test.df), " rows, sum is", true.sum))
results <- run_analysis(test.df, true.sum, funcs)
print(collect_result(results))
