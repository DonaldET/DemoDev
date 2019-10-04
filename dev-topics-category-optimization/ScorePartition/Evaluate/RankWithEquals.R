# RankWithEquals.R

script.dir <- dirname(sys.frame(1)$ofile)
cat(paste('\n-- Demonstrate Equal Values Ranking in:\n  ', script.dir))

source(paste0(script.dir, '/DataPrep.R'))

scale.factor <- 1.0e-4
specify_decimal <- function(x, k=5) format(round(x, k), nsmall=k)
format_nums <- function(x) {
  paste0(specify_decimal(x), collapse = ',')
}

# Short, only at end
#uni_vals <- c(0.1,0.2,0.3,0.3,0.3)
# Beginning, middle, and end
uni_vals <- c(0.006, 0.999, 0.561, 0.562, 0.560, 0.561, 0.111, 0.222, 0.1112, 0.998, 0.111, 0.999, 0.999, 0.111)
n <- length(uni_vals)
cat(paste('\n\n-- Processing', n, 'values with scale factor', scale.factor))
label = 'U'

cat(paste('\n-- initial  :', label, ':'))
cat(format_nums(uni_vals))

cat('\n-- summary  : ')
cat(paste0(label,': '))
smry <- summary(data.frame(xvals=uni_vals, fix.empty.names = FALSE, stringsAsFactors = FALSE))
cat(smry)

srt_uni_vals <- sort(uni_vals, na.last = FALSE)
cat(paste('\n\n-- sorted   :', label, ':'))
cat(format_nums(srt_uni_vals))

u_dp <- data_prep(label, uni_vals, scale.factor)
cat('\n-- processed: ')
cat(paste0(label,': '))
cat(format_nums(u_dp$xvals))

cat('\n-- summaryDP: ')
cat(paste0(label,': '))
smry <- summary(u_dp)
cat(smry)

cat('\n\nSmaller Scale Factor\n-- proc-def : ')
u_dp <- data_prep(label, uni_vals)
cat(paste0(label,': '))
cat(format_nums(u_dp$xvals))

cat('\n-- sryDEF-DP: ')
cat(paste0(label,': '))
smry <- summary(u_dp)
cat(smry)
