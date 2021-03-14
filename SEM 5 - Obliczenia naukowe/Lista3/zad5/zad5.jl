# Autor: Wojciech Pakulski (250350)
include("zera_funkcji.jl")

f(x) = exp(x) - 3*x

println("W przedziale [0, 1]: ", 
    zera_funkcji.mbisekcji(f, 0.0, 1.0, 10^-4, 10^-4))

println("W przedziale [1, 2]: ", 
    zera_funkcji.mbisekcji(f, 1.0, 2.0, 10^-4, 10^-4))