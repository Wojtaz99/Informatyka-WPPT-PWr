module zera_funkcji
export mbisekcji, msiecznych, mstycznych
# Autor: Wojciech Pakulski
"""
Funkcja licząca pierwiastki funkcji f metodą bisekcji
# Arguments
- `f`: funkcja f, dla której liczmy pierwiastki, zadana jako anonimowa
- `a`: jeden koniec przedziału początkowego
- `b`: drugi koniec przedziału początkowego
- `epsilon`: dokładność obliczeń epsilon
- `delta`: dokładność obliczeń delta
"""
function mbisekcji(f, a::Float64, b::Float64, 
                    delta::Float64, epsilon::Float64)
    u = f(a)
    v = f(b)
    e = b-a
    iter = 0
    if(sign(u) == sign(v))
        return NaN, NaN, NaN, 1
    end
    while e > epsilon
        iter += 1
        e = e/2
        c = a+e
        w = f(c)

        if abs(e) < delta || abs(w) < epsilon
            return c, w, iter, 0
        end
        if sign(w) != sign(u)
            b = c
            v = w
        else
            a = c
            u = w
        end
    end
end

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

"""
Funkcja licząca pierwiastki funkcji f metodą siecznych
# Arguments
- `f`: funkcja f, dla której liczmy pierwiastki, zadana jako anonimowa
- `x0`: przybliżenie początkowe
- `x1`: przybliżenie początkowe
- `epsilon`: dokładność obliczeń epsilon
- `delta`: dokładność obliczeń delta
- `maxit`: maksymalna liczba iteracji
"""
function msiecznych(f, x0::Float64, x1::Float64, delta::Float64,
                    epsilon::Float64, maxit::Int)
    fx0 = f(x0)
    fx1 = f(x1)
    for k in 1:maxit
        if abs(fx0) > abs(fx1)
            x0, x1, = x1, x0
            fx0, fx1 = fx1, fx0
        end
        s = (x1-x0)/(fx1-fx0)
        x1 = x0
        fx1 = fx0
        x0 = x0 - fx0*s
        fx0 = f(x0)
        if abs(x1-x0) < delta || abs(fx0) < epsilon
            return x0, fx0, k, 0
        end
    end
    return NaN, NaN, NaN, 1
end

end