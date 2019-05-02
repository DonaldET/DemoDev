#
# Test summation methods for a specific sequence size. This is the test bed for
# the addition checker utility program called <em>Bad Addr</em>. Note that Java
# does not exactly support IEEE math, and a summary found at
# "http://www6.uniovi.es/java-http/1.0alpha3/doc/javaspec/javaspec_10.html".
#
# @author Donald Trummell
#

RAND_SEED = 607
LARGE_PRIME = 7919
SEQUENCE_SIZE = 50000000.0

#
# Given: abs(re) <= 0.5 * 10**(-M) for M significant digits. As a result, M
#              M <= log10(0.5) - log10(abs(re))
# 
# param rel: relative error.
# return: the fractional number of significant digits.
#
  estimateSignificantDigits <- function(rel) {
    absrel = abs(rel)
    
    if (absrel < 1.0E-15)
      return(16.0);
    
    SIG_DIGIT_OFFSET = log10(0.5)
    
    sd = SIG_DIGIT_OFFSET - Math.log10(absrel);
    stopifnot(sd >= 0.0)
    
    return(round(sd, digits = 1))
  }

displayOf <- function(x) {
  return(format(x, trim = TRUE, digits = 16, nsmall = 8))
}

QuickAddChecker <- function(n) {
  stopifnot(!is.null(n), is.numeric(n), n > 0)

        cat("\nR Language Addition Accuracy Test for sequence", n, "long");
        test_seq <- seq(1, n);
        cat("\n  forward:", test_seq[1:6], ". . . for", length(test_seq)," entries.")
        sum_exp = if(n %% 2 == 0) (n / 2) * (n + 1) else n * ((n + 1) / 2)
        cat("\n  exp sum:", displayOf(sum_exp))
        stopifnot(sum_exp == trunc(sum_exp))
        
        sum_f <- sum(test_seq)
        cat("\n  act sum:", displayOf(sum_f), "  -- by BuiltIn SUM")
        stopifnot(sum_f == trunc(sum_f), sum_f == sum_exp)

        # 
        # Divide by a large prime to introduce representational error
        #   

        cat("\n\n--- Now divide by large prime ", LARGE_PRIME, " ---")
        test_seq <- sapply(test_seq, function(x){x / LARGE_PRIME})
        cat("\nWhile operating on", test_seq[1:6], "...")
        sum_exp <- sum_exp / LARGE_PRIME;
        cat("\nTrue sum is now ", displayOf(sum_exp))
        
        sum_f <- sum(test_seq)
        delta <- sum_f - sum_exp;
        re <- delta / sum_exp;
        cat("\n\nNo-shuffle BuiltIn sum is ", displayOf(sum_f), " delta:", displayOf(delta),
            " relative error:", displayOf(re), " sigd:", estimateSignificantDigits(re))

        sum_f <- 0.0
        for (i in 1:n) {
          sum_f <- sum_f + test_seq[(n + 1) - i];
        }
        delta <- sum_f - sum_exp;
        re<- delta / sum_exp;
        cat("\nLargest to smallest sum is", displayOf(sum_f), " delta:", displayOf(delta),
            " relative error:", displayOf(re), " sigd:", estimateSignificantDigits(re))
        # 
        # Shuffle to introduce randomization in summation
        #

        #   cat("\n--- Now Shuffle ---");
        # Collections.shuffle(test_seq, new Random(3677));
        # cat("Randomized");
        # 
        # sum_f = test_seq.stream().reduce(0.0, Double::sum);
        # delta = sum_f - sum_exp;
        # re = delta / sum_exp;
        # cat(String.format("\nStream sum is %f;  delta: %f;" + "  relative error: %e;  sigd: %.1f", sum_f,
        #                                  delta, re, GeneratorUtil.estimateSignificantDigits(re)));
        # 
        # sum_f = test_seq.stream().parallel().reduce(0.0, Double::sum);
        # delta = sum_f - sum_exp;
        # re = delta / sum_exp;
        # System.out
        # .println(String.format("\nParallel stream sum is %f;  delta: %f;" + "  relative error: %e;  sigd: %.1f",
        #                        sum_f, delta, re, GeneratorUtil.estimateSignificantDigits(re)));
        # 
        # sum_f = 0.0;
        # for (int i = 0; i < n; i++) {
        #   sum_f += test_seq.get(i);
        # }
        # delta = sum_f - sum_exp;
        # re = delta / sum_exp;
        # cat(String.format("\nSimple sum is %f;" + "  delta: %f;  relative error: %e;  sigd: %.1f", sum_f,
        #                                  delta, re, GeneratorUtil.estimateSignificantDigits(re)));
        # 
        cat("\n\nAccuracy test complete.\n");
}
