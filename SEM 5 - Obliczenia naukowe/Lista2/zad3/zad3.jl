# Autor: Wojciech Pakulski (250350)

using LinearAlgebra

function matcond(n::Int, c::Float64)
# Function generates a random square matrix A of size n with
# a given condition number c.
# Inputs:
#	n: size of matrix A, n>1
#	c: condition of matrix A, c>= 1.0
#
# Usage: matcond(10, 100.0)
#
# Pawel Zielinski
        if n < 2
         error("size n should be > 1")
        end
        if c< 1.0
         error("condition number  c of a matrix  should be >= 1.0")
        end
        (U,S,V)=svd(rand(n,n))
        return U*diagm(0 =>[LinRange(1.0,c,n);])*V'
end

function hilb(n::Int)
    # Function generates the Hilbert matrix A of size n,
    #  A (i, j) = 1 / (i + j - 1)
    # Inputs:
    #	n: size of matrix A, n>=1
    #
    #
    # Usage: hilb(10)
    #
    # Pawel Zielinski
            if n < 1
             error("size n should be >= 1")
            end
            return [1 / (i + j - 1) for i in 1:n, j in 1:n]
    end

"""
Test do zadania polegający na obliczeniu wyniku Ax=b
używając macierzy Hilberta
"""
function macierz_hilberta(i)
    A = hilb(i)
    x = ones(Float64, i)
    b = A * x

    result = A \ b
    inversed_result = inv(A) * b

    println("$i x $i cond: $(cond(A))")
    println(" Błąd eliminacji Gaussa: $(norm(result - x) / norm(x))")
    println(" Błąd inwersji macierzy: $(norm(inversed_result - x) / norm(x))")
end

"""
Test do zadania polegający na obliczeniu wyniku Ax=b
używając losowej macierzy
"""
function losowa_macierz(n, c)
    A = matcond(n, c)
    x = ones(Float64, n)
    b = A * x

    result = A \ b
    inversed_result = inv(A) * b

    println("$n x $n cond: $c")
    println(" Błąd eliminacji Gaussa: $(norm(result - x) / norm(x))")
    println(" Błąd inwersji macierzy: $(norm(inversed_result - x) / norm(x))")
end

println("Test dla macierzy Hilberta:")
for i in 2:20
    macierz_hilberta(i)
end

println("Test dla losowej macierzy:")
for n in [5, 10, 20]
    for c in [1.0, 10.0, 10.0^3, 10.0^7, 10.0^12, 10.0^16]
        losowa_macierz(n, c)
    end
end