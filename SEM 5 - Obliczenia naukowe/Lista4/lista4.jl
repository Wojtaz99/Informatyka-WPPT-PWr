# Autor: Wojciech Pakulski (250350)
module lista4

export ilorazyRoznicowe, warNewton, naturalna
export rysujNnfx, rysujPrawdziwa

"""
Funkcja licząca ilorazy różnicowe
# Arguments
    `x`: wektor składający się z węzłów x0, ..., xn długości n+1
    `f`: wektor składający się z wartości interpolowanej funkcji w węzłach f(x0), ..., f(xn) długości n+1
"""
function ilorazyRoznicowe(x::Vector{Float64}, f::Vector{Float64})

    n = length(f)
    fx = Vector{Float64}(undef, n)

    for i = 1:n
        fx[i] = f[i]
    end

    # Liczy iloraz różnicowy
    for i = 2:n
        for j = n:-1:i
            fx[j] = (fx[j] - fx[j-1]) / (x[j] - x[j - i + 1])
        end
    end
    return fx
end

"""
Funkcja licząca wartości wielomianu interpolacyjnego w postaci
Newtona w punkcie
# Arguments
    `x`: wektor długości n+1 zawierający węzły
    `fx`: wektor długości n+1 zawierający ilorazy różnicowe
    `t`: punkt, w którym należy obliczyć wartość wielomianu
"""
function warNewton(x::Vector{Float64}, fx::Vector{Float64}, t::Float64)
    n = length(fx)
    nt = fx[n]
    for i = n-1:-1:1
        nt = fx[i] + (t - x[i]) * nt
    end
    return nt
end

"""
Funkcja licząca wektor zawierający współczynniki postaci naturalnej
wielomianu interpolacyjnego w postaci Newtona w czasie O(n^2)
# Arguments
    `x`: wektor zawierający węzły x0, ..., xn
    `fx`: wektor zawierający ilorazy róznicowe
"""
function naturalna(x::Vector{Float64}, fx::Vector{Float64})
    n = length(x)
    a = Vector{Float64}(undef, n)
    a[n] = fx[n]

    for i = n-1:-1:1
        a[i] = fx[i]
        for j = i:n-1
            a[j] = a[j] - x[i]*a[j+1]
        end
    end
    return a
end
using PyPlot

"""
Funkcja rysująca w bibliotece PyPlot funkcje za pomocą wielomianu
interpolacyjnego stopnia n
# Arguments:
    `f`: funkcja zadana jako anonimowa
    `a`: początek przedziału
    `b`: koniec przedziału
    `n`: stopień wielomianu interpolacyjnego
"""
function rysujNnfx(f, a::Float64, b::Float64, n::Int)
    
    x = zeros(n+1)
    y = zeros(n+1)

    # Liczenie węzłów interpolacyjnych i ich wartości
    for k = 0:n
        h = (b-a)/n
        x[k+1] = a + k*h
        y[k+1] = f(x[k+1])
    end

    fx = ilorazyRoznicowe(x, y)

    # ilosc - liczba punktów do rysowania wykresu
    # 0.0001 to odleglosc miedzy punktami
    ilosc = (b-a)/0.0001
    ilosc = Int(ilosc)

    x2 = zeros(ilosc)

    for i = 0:ilosc-1
        x2[i+1] = a + i*0.0001
    end

    fx2 = zeros(ilosc)

    for i = 1:ilosc
        fx2[i] = warNewton(x, fx, x2[i])
    end

    plot(x2, fx2)
end

"""
Funkcja rysująca w bibliotece PyPlot funkcje 
# Arguments:
    `f`: funkcja zadana jako anonimowa
    `a`: początek przedziału
    `b`: koniec przedziału
"""
function rysujPrawdziwa(f, a::Float64, b::Float64)
    ilosc = (b-a)/0.0001
    ilosc = Int(ilosc)

    x1 = zeros(ilosc)

    for i = 0:ilosc-1
        x1[i+1] = a + i*0.0001
    end

    fx1 = zeros(ilosc)

    for i = 1:ilosc
        fx1[i] = f(x1[i])
    end
    plot(x1, fx1)
end

end
