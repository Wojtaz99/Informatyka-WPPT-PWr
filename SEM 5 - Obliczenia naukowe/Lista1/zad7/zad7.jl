# Autor: Wojciech Pakulski (250350)
"""
Funkcja f z zadania
"""
function f(x)
    return sin(x) + cos(3*x)
end

"""
Funkcja licząca wartość pochodnej funkcji f
"""
function derivative(x)
    return cos(x) - 3*sin(3*x)
end

"""
Funkcja licząca wartość przybliżonej pochodnej funkcji f
"""
function approximation(x, h)
    return (f(x + h) - f(x)) / h
end

"""
Funkcja wypisująca dane z treści: wartość przybliżonej pochodnej,
wartość pochodnej i błąd dla h = 2^-n dla n = (0, 54)
"""
function zad7()
    for n = 0:54
        h::Float64 = 2^(-Float64(n))
        println(n, " Dla 1 + h = ", 1 + h)
        println("Błąd wyniósł: ", abs(derivative(1.0) - approximation(1.0, h)))
        println("Wartość przybliżonej pochodnej: ", approximation(1.0, h))
        println("Wartość pochodnej: ", derivative(1.0))
        println()
    end
end

zad7()