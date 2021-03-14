# Autor: Wojciech Pakulski (250350)
include("zera_funkcji.jl")

f(x) = sin(x) - ((1/2)*x)^2
pf(x) = cos(x) - (1/2)*x

println("Metoda bisekcji: ", 
    zera_funkcji.mbisekcji(f, 1.5, 2.0, 0.5*10^-5, 0.5*10^-5))

println("Metoda Newtona: ", 
    zera_funkcji.mstycznych(f, pf, 1.5, 0.5*10^-5, 0.5*10^-5, 20))

println("Metoda siecznych: ", 
    zera_funkcji.msiecznych(f, 1.0, 2.0, 0.5*10^-5, 0.5*10^-5, 20))
