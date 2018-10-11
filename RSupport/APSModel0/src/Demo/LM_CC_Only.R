# Create a linear model using only CC and no intercept

source("src/modeling/SimpleStoreCC.R")

# Use LM to fit a model with CC and no intercept term
fitJustCC <- fitCCOnly(sampleData)
