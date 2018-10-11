# Fit Aggregation and all LM models

# Load sample data
load(verbose = FALSE, file = "resources/SampleData/Sample2.rda")

# Fit model with LM using CC only as predictor
source("src/Demo/LM_CC_Only.R")

# Fit model with LM using store only as predictor
source("src/Demo/LM_Store_Only.R")

# Fit model with LM and add intercept
source("src/Demo/LM_Inter.R")

# Fit model with LM and no intercept, model with means
source("src/Demo/LM_NoInter.R")

# Fit with Aggregation
source("src/Demo/LM_Agg.R")
