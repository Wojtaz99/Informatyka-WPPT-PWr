include("lista4.jl")
using PyPlot

function a()
    for n = [5, 10, 15]
        lista4.rysujNnfx(x-> abs(x), -1.0, 1.0, n)
    end
    lista4.rysujPrawdziwa(x-> abs(x), -1.0, 1.0)
    PyPlot.title("f(x) = |x|")
    PyPlot.legend(["Dla n=5", "Dla n=10", "Dla n=15", "Dokładna"])
end

function b()
    for n = [5, 10, 15]
        lista4.rysujNnfx(x-> 1/(1+x^2), -5.0, 5.0, n)
    end
    lista4.rysujPrawdziwa(x-> 1/(1+x^2), -5.0, 5.0)
    PyPlot.title("f(x) = 1/(1+x^2)")
    PyPlot.legend(["Dla n=5", "Dla n=10", "Dla n=15", "Dokładna"])
end
