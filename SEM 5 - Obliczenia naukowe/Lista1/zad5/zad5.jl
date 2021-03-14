# Autor: Wojciech Pakulski (250350)
"""
Funkcja licząca iloczyny skalarne wektorów różnymi sposobami
# Arguments
- `type_arg`: typ zmienych dla jakich chcemy liczyć zadanie
"""
function zad5(type_arg)
	a::Vector{type_arg} = [2.718281828, -3.141592654, 1.414213562, 0.5772156649, 0.3010299957]
	b::Vector{type_arg} = [1486.2497, 878366.9879, -22.37492, 4773714.647, 0.000185049]
	c::Vector{type_arg} = a .* b # Wektor po wymnożeniu ai * bi
	# Obliczanie sumy wg pp a
	sum_a::type_arg = 0 # Suma wyliczona wg podpunktu a
	for i = 1:length(a)
		sum_a += c[i]
	end
	println("Suma pierwsza: ", sum_a)
	
	# Obliczanie sumy wg pp b
	sum_b::type_arg = 0 # Suma wyliczona wg podpunktu b
	i = length(a)
	while i > 0
		sum_b += c[i]
		i -= 1
	end
	println("Suma druga: ", sum_b)
	
	# Obliczanie sumy wg pp c
	c1 = sort(c, rev=true) # Wektor c posortowany 
	sum_c::type_arg = 0 # Suma wyliczona wg podpunktu c
	sum_c_pos::type_arg = 0 # Suma cząstkowa liczb dodatnich do podpunktu c
	sum_c_neg::type_arg = 0 # Suma cząstkowa liczb ujemnych do podpunktu c
	# Zliczanie dodatnich
	for i = 1:length(a)
		if c1[i] > 0
			sum_c_pos += c1[i]
		else
			break
		end
	end
	# Zliczanie ujemnych
	i = length(a)
	while i > 0
		if c1[i] < 0
			sum_c_neg += c1[i]
		else
			break
		end	
		i -= 1
	end
	sum_c = sum_c_pos + sum_c_neg
	println("Suma trzecia: ", sum_c)
	
	# Obliczanie sumy wg pp d
	sum_d::type_arg = 0 # Suma wyliczona wg podpunktu d
	sum_d_neg::type_arg = 0 # Suma cząstkowa liczb ujemnych do podpunktu d
	sum_d_pos::type_arg = 0 # Suma cząstkowa liczb dodatnich do podpunktu d
	c2 = sort(c)
	i = length(a)
	# Zliczanie dodatnich
	while i > 0
		if c2[i] > 0
			sum_d_pos += c2[i]
		else
			break
		end
		i -= 1
	end
	# Zliczanie ujemnych
	for i = 1:length(a)
		if c2[i] < 0
			sum_d_neg += c2[i]
		else
			break
		end
	end
	sum_d = sum_d_neg + sum_d_pos
	println("Suma czwarta: ", sum_d)
end

# Wyliczamy 4 sumy dla każdego z typów
for i = [Float32, Float64]
	println("Dla ", i)
	zad5(i)
end
