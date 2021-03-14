include("lista4.jl")
using PyPlot

function a()
    for n = [5, 10, 15]
        lista4.rysujNnfx(x-> exp(x), 0.0, 1.0, n)
    end
    lista4.rysujPrawdziwa(x-> exp(x), 0.0, 1.0)
    PyPlot.title("f(x) = exp(x)")
    PyPlot.legend(["Dla n=5", "Dla n=10", "Dla n=15", "Dokładna"])
end

function b()
    for n = [5, 10, 15]
        lista4.rysujNnfx(x-> x^2 * sin(x), -1.0, 1.0, n)
    end
    lista4.rysujPrawdziwa(x-> x^2 * sin(x), -1.0, 1.0)
    PyPlot.title("f(x) = x^2 * sin(x)")
    PyPlot.legend(["Dla n=5", "Dla n=10", "Dla n=15", "Dokładna"])
end
