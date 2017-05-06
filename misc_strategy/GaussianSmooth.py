from scipy.signal import gaussian
from scipy.ndimage import filters

"""
	x
	y
	var_x: 75
	var_y: variance of y variable
""" 
def GaussSmooth(x, y, var_x, var_y):
    b = gaussian(var_x, var_y)
    ga = filters.convolve1d(y, b/b.sum())
    return ga