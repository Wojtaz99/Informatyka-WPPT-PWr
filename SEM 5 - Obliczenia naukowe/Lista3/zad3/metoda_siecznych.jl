# Autor: Wojciech Pakulski
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

function test()
    f(x)=-(1/20)*(x-10)*(x+1)*(x+3)
    println(msiecznych(f, 7.0, 9.8, 10.0^-8.0, 10.0^-8.0, 1000))
end
    
test()