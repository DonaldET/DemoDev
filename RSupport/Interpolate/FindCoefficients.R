# Find polynomial coefficients for Cp, technique taken from:
# https://datascienceplus.com/fitting-polynomial-regression-r/
#
WEP.cp.fit <- function(generator, x, y) {
  # print('Cubic Fit for', generator)
  # model3 <- lm(y ~ x + I(x^2) + I(x^3))
  # summary(model3)

  print('Fifth Power Fit for', generator)
  model5 <- lm(y ~ x + I(x^2) + I(x^3) + I(x^4) + I(x^5))
  summary(model5)
  
  print('')
  print(paste0('Create plot of Predicted vs Actual for ', generator))
  #plot predicted vs. actual values
  plot(x=predict(model5), y=y,
       xlab='Predicted Values',
       ylab='Actual Values',
       main=paste0('Fifth Power Polynomial: Predicted vs. Actual for ', generator))
  
  #add diagonal line for estimated regression line
  abline(a=0, b=1)
  
  return(coefficients(model5))
}