# Autor: Wojciech Pakulski (250350)
"""
Funkcja f z zadania
"""
function f(x::Float64)
    return Float64(sqrt(x^2 + 1) - 1)
end

"""
Funkcja g z zadania
"""
function g(x::Float64)
    return Float64(x^2 / (sqrt(x^2 + 1) + 1))
end

"""
Wypisuje wartości funkcji f i g dla wartości z zadania
"""
function zad6()
    for i = 1:24
        x::Float64 = 8^(-Float64(i))
        println("Dla x = ", x, " f(x) = ", f(x))
    end
    for i = 1:24
        x::Float64 = 8^(-Float64(i))
        println("Dla x = ", x, " g(x) = ", g(x))
    end
end

zad6()