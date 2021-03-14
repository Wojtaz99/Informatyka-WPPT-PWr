# Autor: Wojciech Pakulski
"""
Funkcja licząca pierwiastki funkcji f metodą Newtona
# Arguments
- `f`: funkcja f, dla której liczmy pierwiastki, zadana jako anonimowa
- `pf`: pochodna funkcji f, zadana jako anonimowa
- `x0`: przybliżenie początkowe
- `epsilon`: dokładność obliczeń epsilon
- `delta`: dokładność obliczeń delta
- `maxit`: maksymalna liczba iteracji
"""
function mstycznych(f,pf,x0::Float64, delta::Float64,
                    epsilon::Float64, maxit::Int)
    v = f(x0)
    if abs(v) < epsilon
        return x0, v, 0, 0
    end

    # Jeśli pf jest bliska zera
    if abs(pf(x0)) < epsilon
        return NaN, NaN, NaN, 2
    end

    for k in 1:maxit
        x1 = x0 - (v/pf(x0))
        v = f(x1)
        if abs(x1-x0) < delta || abs(v) < epsilon
            return x1, v, k, 0
        end
        x0 = x1
    end
    return NaN, NaN, NaN, 1
end

function test()
    f(x) = x^2-16
    pf(x) = 2x
    println(mstycznych(f, pf, 5.0, 10.0^-8.0, 10.0^-8.0, 60))
end