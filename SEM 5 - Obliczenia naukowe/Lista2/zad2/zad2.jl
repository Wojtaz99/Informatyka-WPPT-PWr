import Pkg
# Dodawanie pakietów
Pkg.add("PyPlot")

# Rysowanie funkcji dla pakietu PyPlot
using PyPlot
x = range(-10, stop=50, length=1000)
y = ((ℯ).^x) .* log.(1 .+ (ℯ).^-x)
plot(x, y)