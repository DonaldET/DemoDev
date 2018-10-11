# Create zero-orgin, no intercept, linear model fit

source("src/modeling/SimpleStoreCC.R")

# Use LM to fit a model with no Intercept term (pure averages)
fit <- fitStoreCC(sampleData)
