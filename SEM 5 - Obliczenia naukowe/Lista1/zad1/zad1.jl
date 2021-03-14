# Autor: Wojciech Pakulski (250350)
"""
Funkcja obliczająca iteracyjnie epsilon maszynowy
"""
function macheps(type)
	macheps::type = 1 # Docelowa wartosc machepsa
	while type(1 + macheps/2) != 1
        macheps = macheps / 2
	end
	println("Dla ", type, " obliczony macheps = ", macheps, ", systemowy eps = ", eps(type))
end

"""
Funkcja obliczająca iteracjnie etę
# Arguments
- `type`: typ zmienych dla jakich chcemy liczyć etę
"""
function eta(type)
	eta::type = 1.0 # Wyliczana wartosc ety
	while eta / 2 > 0
		eta = eta / type(2)
	end
	println("Dla ", type, " eta = ", eta, ", nextfloat = ", nextfloat(type(0.0)))
end

"""
Funkcja obliczająca iteracjnie liczbę max
# Arguments
- `type`: typ zmienych dla jakich chcemy liczyć MAX
"""
function max(type)
    test::type = 1.0 # Wartosc testowa maxa
    while isinf(2 * test) == false
		test *= 2
    end
	adder::type = test/2 # Wartosc, ktora bedzie dodawana
	prev::type = test # Wartosc maxa przed testowa
    while isinf(nextfloat(type(test))) == false
        prev = test
		test = test + adder
		if isinf(test) == true
			test = prev
			while isinf(type(test + adder)) == true 
				if test == test + adder
					test = nextfloat(test)
					break
				end
				adder /= 2.0
			end
		end
	end
	println("Dla ", type, " max = ", test, ", floatmax = ", floatmax(type(0)))
end

# Wyliczamy macheps, etę i max dla każdego z typów
for i = [Float16, Float32, Float64]
	macheps(i)
	eta(i)
	max(i)
	println()
end
