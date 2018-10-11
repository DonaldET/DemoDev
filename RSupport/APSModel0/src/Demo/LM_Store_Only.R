# Create a linear model using only Store and no intercept

source("src/modeling/SimpleStoreCC.R")

# Use LM to fit a model with CC and no intercept term
fitJustStore <- fitStoreOnly(sampleData)
