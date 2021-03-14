# Autor: Wojciech Pakulski (250350)

using PyPlot

# Tablica wyników bez stopu
p = Array{Float32}(undef, 41) 
# Tablica wyników ze stopem
p2 = Array{Float32}(undef, 41) 

p[1] = Float32(0.01)
p2[1] = Float32(0.01)
r = Float32(3)

# Obliczanie 40 iteracji dla obu tablic
for i in 2:41
    p[i] = Float32(p[i-1] + r*p[i-1]*(1-p[i-1]))
    p2[i] = p[i]
end
# Ucięcie 3 miejsc po przecinku, po 10 operacjach
p2[11] *= 1000
p2[11] = Float32(floor(p2[11])/1000)
# Kontynuowanie od obciętej wartości
for i in 12:41
    p2[i] = Float32(p2[i-1] + r*p2[i-1]*(1-p2[i-1]))
end

# W arytmetyce Float64
p3 = Array{Float64}(undef, 41)
p3[1] = Float64(0.01)
r = Float64(3)

for i in 2:41
    p3[i] = Float64(p3[i-1] + r*p3[i-1]*(1-p3[i-1]))
end

println("Eksperyment nr 1")
for i in 1:41
    println("Operacja $(i-1) $(p[i]) $(p2[i]) błąd: $(abs(p[i]-p2[i]))")
end

println("Eksperyment nr 2")
for i in 1:41
    println("Operacja $(i-1) Float32:$(p[i]) Float64:$(p3[i]) błąd: $(Float64(abs(Float64(p[i])-p3[i])))")
end


"""Rysowanie wykresów"""
function experiment_on_plot()
    x = [i for i in 0:40]
    plot(x, p)
    plot(x, p2)
    plot(x, p3)
    legend(["Float32", "Zaburzone", "Float64"])
    xlabel("Liczba iteracji")
    ylabel("Wartości ciągu")
end

experiment_on_plot()
