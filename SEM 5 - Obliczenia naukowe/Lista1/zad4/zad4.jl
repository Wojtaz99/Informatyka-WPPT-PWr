# Autor: Wojciech Pakulski (250350)
"""
Znajduje najmniejszą liczbę x, z przedziału (1, 2)
w formacie Float64 taką, że spełnia równanie: x ∗ (1 / x) != 1
"""
function zad4()
	x::Float64 = 1
    while Float64(x * Float64(1/x)) == 1
		x += eps(Float64)
    end
    println("Liczba: ", x)
end

zad4()