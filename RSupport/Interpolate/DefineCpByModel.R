# The coefficient of performance, CP, also called the power coefficient of a wind
# turbine, is defined as the ratio of the power captured by the rotor of the wind
# turbine, PR, divided by the total power available in the wind, P, just before
# it interacted with the rotor of the turbine.
#
# Taken from page 3 of:
# http://www.wiete.com.au/journals/WTE&TE/Pages/Vol.11,%20No.1%20(2013)/06-Libii-J-N.pdf
#
# Uses ENERCON model wind turbines
#
WEP.sample.cp <- function() {
  turbines <- c("E33", "E44", "E48", "E53", "E70",
                "E82E2", "E82E2B", "E82E3", "E101", "E126")
  nlth <- length(turbines)
  power_KW <- c(330, 900, 800, 800, 2300, 2000, 2300, 3000, 3000, 7500)
  stopifnot(length(power_KW) == nlth)
  
  nentries <- 0
  E33 <- c(0, 0, 0.35, 0.4, 0.45, 0.47, 0.5, 0.5,
           0.5, 0.47, 0.41, 0.35, 0.28, 0.23,
           0.18, 0.15, 0.13, 0.11, 0.09, 0.08,
           0.07, 0.06, 0.05, 0.05, 0.04)
  nentries <- nentries + 1
  nlth <- length(E33)
  E44 <- c(0, 0, 0.16, 0.34, 0.43, 0.48, 0.49,
           0.5, 0.5, 0.5, 0.48, 0.44, 0.39, 0.33,
           0.28, 0.24, 0.20, 0.17, 0.14, 0.12,
           0.11, 0.09, 0.08, 0.07, 0.06)
  nentries <- nentries + 1
  stopifnot(length(E44) == nlth)
  E48 <- c(0, 0, 0.17, 0.35, 0.43, 0.46, 0.47,0.48,
           0.5, 0.5, 0.45, 0.39, 0.32, 0.27, 0.22,
           0.18, 0.15, 0.13, 0.11, 0.09, 0.08, 0.07,
           0.06, 0.05, 0.05)
  nentries <- nentries + 1
  stopifnot(length(E48) == nlth)
  E53 <- c(0, 0.19, 0.39, 0.44, 0.46, 0.48, 0.49, 0.49, 0.49,
           0.48, 0.42, 0.34, 0.27, 0.22, 0.18, 0.15, 0.12, 0.1,
           0.09, 0.08, 0.06, 0.06, 0.05, 0.04, 0.04)
  nentries <- nentries + 1
  stopifnot(length(E53) == nlth)
  E70 <- c(0, 0.1, 0.27, 0.36, 0.42, 0.46, 0.48, 0.5, 0.5, 0.5,
           0.49, 0.45, 0.39, 0.34, 0.28, 0.23, 0.19, 0.16, 0.14, 0.12,
           0.1, 0.09, 0.08, 0.07, 0.06)
  nentries <- nentries + 1
  stopifnot(length(E70) == nlth)
  E82E2 <- c(0, 0.12, 0.29, 0.4, 0.43, 0.46, 0.48, 0.49, 0.5, 0.49, 0.42,
             0.35, 0.29, 0.23, 0.19, 0.15, 0.13, 0.11, 0.09, 0.08,
             0.07, 0.06, 0.05, 0.05, 0.04)
  nentries <- nentries + 1
  stopifnot(length(E82E2) == nlth)
  E82E2B <- c(0, 0.12, 0.29, 0.4, 0.43, 0.46, 0.48, 0.49, 0.5, 0.49, 0.44,
              0.38, 0.32, 0.26, 0.22, 0.18, 0.15, 0.12, 0.11, 0.09,
              0.08, 0.07, 0.06, 0.05, 0.05)
  nentries <- nentries + 1
  stopifnot(length(E82E2B) == nlth)
  E82E3 <- c(0, 0.12, 0.29, 0.4, 0.43, 0.46, 0.48, 0.49, 0.5, 0.49, 0.44,
             0.39, 0.35, 0.3, 0.26, 0.22, 0.19, 0.16, 0.14, 0.12,
             0.1, 0.09, 0.08, 0.07, 0.06)
  nentries <- nentries + 1
  stopifnot(length(E82E3) == nlth)
  E101 <- c(0, 0.076, 0.279, 0.376, 0.421, 0.452, 0.469, 0.478, 0.478,
            0.477, 0.439, 0.358, 0.283, 0.227, 0.184, 0.152, 0.127, 0.107,
            0.091, 0.078, 0.067, 0.058, 0.051, 0.045, 0.04)
  nentries <- nentries + 1
  stopifnot(length(E101) == nlth)
  E126 <- c(0, 0, 0.263, 0.352, 0.423, 0.453, 0.47, 0.478, 0.477, 0.483,
            0.47, 0.429, 0.381, 0.329, 0.281, 0.236, 0.199, 0.168, 0.142,
            0.122, 0.105, 0.092, 0.08, 0.071, 0.063)
  nentries <- nentries + 1
  stopifnot(length(E126) == nlth)
  
  dta <- c(1:25)
  dta <- append(dta, E33)
  dta <- append(dta, E44)
  dta <- append(dta, E48)
  dta <- append(dta, E53)
  dta <- append(dta, E70)
  dta <- append(dta, E82E2)
  dta <- append(dta, E82E2B)
  dta <- append(dta, E82E3)
  dta <- append(dta, E101)
  dta <- append(dta, E126)
  
  col.names <- append(c("Speed"), turbines)
  gen.data <- matrix(dta, nrow=nlth, ncol=nentries + 1, byrow=FALSE,
                     dimnames=list(paste("T", 1:nlth, sep='.'), col.names))
  
  return(gen.data)
}
