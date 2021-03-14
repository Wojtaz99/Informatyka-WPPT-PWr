# Autor: Wojciech Pakulski (250350)
module blocksys
export wczytaj_wektor_prawych_stron_z_pliku, wczytaj_macierz_z_pliku, zapisz_wyniki_do_pliku,
eliminacja_gaussa, eliminacja_gaussa2, generuj_wektor
using SparseArrays
using LinearAlgebra # TYLKO DO LICZENIA BŁĘDU!
"""
Funkcja wczytująca macierz z pliku w postaci:
    n  l  <--- rozmiar macierzy   A,  rozmiar macierzy Ak, Bk, Ck
    i1  j1   A[i1,j1] <--- indeksy i1,j1 i niezerowy element a[i1,j1] macierzy A
    i2  j2   A[i2,j2] <--- indeksy i2,j2 i niezerowy element a[i2,j2] macierzy A
    i3  j3   A[i3,j3] <--- indeksy i3,j3 i niezerowy element a[i3,j3] macierzy A
    ...
    EOF

# Arguments
    `path`: ścieżka do pliku
"""
function wczytaj_macierz_z_pliku(path::String)
    open(path) do f
        while ! eof(f)
            line = split(readline(f), " ")
            n = parse(Int64, line[1])

            l = parse(Int64, line[2])

            A = spzeros(Float64, n, n)
            while ! eof(f)
                line = split(readline(f), " ")

                i = parse(Int64, line[1])
                j = parse(Int64, line[2])

                value = parse(Float64, line[3])
                A[i, j] = value
            end
            return A, n, l
        end
    end
end


"""
Funkcja wczytująca wektor prawych stron z pliku w postaci:
    n     <--- rozmiar wektora 
    bb[1] <--- skladowa nr 1 wektora 
    bb[2] <--- skladowa nr 2 wektora b
    ...
    b[n] <--- skladowa nr n wektora b

# Arguments
    `path`: ścieżka do pliku
"""
function wczytaj_wektor_prawych_stron_z_pliku(path::String)
    open(path) do f
        line = readline(f)
        n = parse(Int64, line)
        b = Vector{Float64}(undef, n)

        i = 1
        while i <= n
            line = readline(f)
            b[i] = parse(Float64, line)
            i += 1
        end
        return b
    end
end


"""
Funkcja zapisująca rozwiązanie do pliku tekstowego w postaci:
    blad <--- blad wzgledny (jeśli wymagany)
    x[1] <--- skladowa nr 1 wektora x
    x[2] <--- skladowa nr 2 wektora x
    ...
    x[n] <--- skladowa nr n wektora x

# Arguments
	`path`: ścieżka do pliku wynikowego
	`x`: wektor wynikowy
	`n`: rozmiar wektora x
	`vec_b_generated`: czy vector b był generowany
"""
function zapisz_wyniki_do_pliku(path::String, x::Vector{Float64}, n::Int64, vec_b_generated::Bool)
    open(path, "w") do file
        if vec_b_generated
            one = ones(n)
            err = norm(one - x) / norm(x)
            println(file, err)
        end

        for i = 1:n
            println(file, x[i])
        end
    end
end

"""
Generuje wektor prawych stron b, gdzie b = Ax
x = (1, ..., 1)^T
# Arguments
    `A`: sparse matrix
    `n`: size of A
    `l`: size of block of A
# Out
    `b`: computed right-side vector
"""
function generuj_wektor(A::SparseMatrixCSC{Float64, Int64}, n::Int64, l::Int64)
    x = ones(Float64, n)
    b = zeros(Float64, n)
    v = Int64(n / l)

    for block in 1 : v
        for i in (block - 1)*l + 1 : block*l
            sum = 0

            if block == 1
                for j in 1 : block*l
                    sum += A[i, j]*x[j]
                end
            else
                for j in (block - 1)*l : block*l
                    sum += A[i, j]*x[j]
                end
            end

            if block != v
                sum += A[i, i + l]*x[i + l]
            end

            b[i] = sum
        end
    end
    return b
end

"""
Funkcja przeprowadzająca pierwszy etap eliminacji Gaussa bez wyboru elementu głównego
# Arguments
    `A`: macierz wejściowa
    `b`: wektor prawych stron
    `n`: rozmiar macierzy
    `l`: rozmiar bloku

# Out:
    A - macierz po eliminacji Gaussa
    b - wektor prawych stron po eliminacji
"""
function eliminacja_gaussa(A::SparseMatrixCSC{Float64, Int64}, b::Vector{Float64}, n::Int64, l::Int64)
    
    for k = 1:n-1 # wiersze
        last_col = min(k+l, n)
        last_row = Int(min(n, l + l*floor(k / l)))

        for i = k + 1:last_row
            multiplier = A[i, k] / A[k, k]

            A[i, k] = Float64(0.0)

            for j = k+1 : last_col
                A[i, j] -= multiplier * A[k, j]
            end
            b[i] -= multiplier * b[k]
        end
    end
    return podstaw_od_tylu_bez_wyboru(A, b, n, l)
end

"""
Znajduje rozwiązanie układu równań Ax = b
# Arguments
	`A`: macierz górna trójkątna
	`b`: wektor prawych stron
	`n`: rozmiar macierzy
	`l`: rozmiar bloku
# Out:
	`x`: wektor rozwiązania
"""
function podstaw_od_tylu_bez_wyboru(A::SparseMatrixCSC{Float64, Int64}, b::Vector{Float64}, n::Int64, l::Int64)
    x = zeros(n)

    for i in n : -1 : 1
        sum = 0
        last_col = min(n, i + l)

        for col in i + 1 : last_col
            sum += A[i, col] * x[col]
        end

        x[i] = (b[i] - sum) / A[i, i]
    end

    return x
end

"""
Funkcja przeprowadzająca pierwszy etap eliminacji Gaussa 
z częściowym wyborem elementu głównego
# Arguments
    `A`: macierz wejściowa
    `b`: wektor prawych stron
    `n`: rozmiar macierzy
    `l`: rozmiar bloku

# Out:
    A - macierz po eliminacji Gaussa
    b - wektor prawych stron po eliminacji
"""
function eliminacja_gaussa2(A::SparseMatrixCSC{Float64, Int64}, b::Vector{Float64}, n::Int64, l::Int64)
    perm = collect(1 : n)

    for k in 1:n-1
        lastColumn = 0
        lastRow = 0

        for i in k:min(n, k + l)
            if abs(A[perm[i], k]) > lastColumn
                lastColumn = abs(A[perm[i], k])
                lastRow = i
            end
        end

        perm[lastRow], perm[k] = perm[k], perm[lastRow]

        for i in k+1:min(n, k + l)
            z = A[perm[i], k] / A[perm[k], k]
            A[perm[i], k] = 0.0

            for j in k+1:min(n, k + 2*l)
                A[perm[i], j] = A[perm[i], j] - z * A[perm[k], j]
            end
            b[perm[i]] = b[perm[i]] - z*b[perm[k]]
        end
    end
	return podstaw_od_tylu_bez_wyboru(A, b, n, l, perm)
end

function podstaw_od_tylu_bez_wyboru(A::SparseMatrixCSC{Float64, Int64}, b::Vector{Float64}, n::Int64, l::Int64, perm::Vector{Int64})::Vector{Float64}
    result = zeros(Float64, n)
    
    for k in 1:n-1
        for i in k+1:min(n, k + 2*l)
            b[perm[i]] = b[perm[i]] - A[perm[i], k] * b[perm[k]]
        end
    end

    for i in n:-1:1
        currentSum = 0
        for j in i+1:min(n, i + 2 * l)
            currentSum += A[perm[i], j] * result[j]
        end
        result[i] = (b[perm[i]] - currentSum) / A[perm[i], i]
    end

    return result
end


end
