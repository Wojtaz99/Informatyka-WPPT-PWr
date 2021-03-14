# Autor: Wojciech Pakulski (250350)

using PyPlot

"""
Funkcja licząca wyrazy ciągu zdefiniowanego rekurencyjnie
x(n+1) = x(n)^2 + c
# Arguments
- `c`: 'c' z wyrażenia
- `x0`: pierwszy element ciągu
"""
function recurency(c, x0)
    x = Array{Float64}(undef, 41)
    x[1] = x0
    for i in 2:41
        x[i] = x[i-1]^2 + c
    end
    return x
end

"""
Funkcja wypisująca tablice elementów dla 
wszystkich eksperymentów z zadania
"""
function experiments()
    ex1 = recurency(-2, 1)
    ex2 = recurency(-2, 2)
    ex3 = recurency(-2, 1.99999999999999)
    ex4 = recurency(-1, 1)
    ex5 = recurency(-1, -1)
    ex6 = recurency(-1, 0.75)
    ex7 = recurency(-1, 0.25)
    println("ex1 ", ex1)
    println("ex2 ", ex2)
    println("ex3 ", ex3)
    println("ex4 ", ex4)
    println("ex5 ", ex5)
    println("ex6 ", ex6)
    println("ex7 ", ex7)
end

"""
Funkcja rysująca wykresy wartości elementów od iteracji
dla wszystkich eksperymentów z zadania
"""
function experiments_on_plot()
    ex1 = recurency(-2, 1)
    ex2 = recurency(-2, 2)
    ex3 = recurency(-2, 1.99999999999999)
    ex4 = recurency(-1, 1)
    ex5 = recurency(-1, -1)
    ex6 = recurency(-1, 0.75)
    ex7 = recurency(-1, 0.25)
    x = [i for i in 0:40]
    plot(x, ex1)
    plot(x, ex2)
    plot(x, ex3)
    plot(x, ex4)
    plot(x, ex5)
    plot(x, ex6), 
    plot(x, ex7)
    legend(["1", "2", "3", "4", "5", "6", "7"])
    xlabel("Liczba iteracji")
    ylabel("Wartości ciągu")
end
