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
    if sign(u) == sign(v)
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

function test()
    f(x) = 2*(x-2)*(x-4)*(x-5)
    println(mbisekcji(f, 3.3, 4.22, 10.0^-8.0, 10.0^-8.0))
end

